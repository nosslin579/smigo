function PlantService($http, $window, $timeout, $rootScope, $q) {
    var state = {},
        yearSquareMap = {},
        unsavedCounter = 0,
        autoSaveInterval = 60000,
        timedAutoSavePromise = $timeout(sendUnsavedPlantsToServer, autoSaveInterval, false);

    updateState(initData.squares);

    console.log('PlantService', state);

    $rootScope.$on('current-user-changed', function (event, user) {
        if (user) {
            reloadPlants();
        } else {
            var year = new Date().getFullYear();
            var squares = {};
            squares[year] = [new Square(new Location(year, 0, 0))];
            updateState(squares);
        }
    });

    $window.onbeforeunload = function () {
        sendUnsavedPlantsToServer();
        return null;
    };


    function PlantData(plant) {
        this.year = plant.location.year;
        this.y = plant.location.y;
        this.x = plant.location.x;
        this.speciesId = plant.species.id;
    }

    function Location(year, x, y) {
        if (!$.isNumeric(year) || !$.isNumeric(x) || !$.isNumeric(y)) {
            throw "Location takes Numeric parameter only. x:" + x + " y:" + y + " year:" + year;
        }
        this.year = +year;
        this.x = +x;
        this.y = +y;
    }

    function Plant(species, location, flag) {
        this.species = species;
        this.location = location;
        if (flag) {
            this[flag] = true;
        }
    }

    function Square(location, speciesArray, flag) {
        if (!location instanceof Location) {
            throw "Square.location must be a Location object. location:" + location;
        }
        if (!speciesArray instanceof Array) {
            throw "Square.speciesArray must be an Array. speciesArray:" + speciesArray;
        }
        this.location = location;
        this.plants = {};
        if (speciesArray) {
            speciesArray.forEach(function (species) {
                this.plants[species.id] = new Plant(species, location, 'add');
            }, this);
        }
        if (flag) {
            this[flag] = true;
        }
    }

    function getBounds(year) {
        var axisLength = 9999;
        var ret = {
            xmax: -axisLength,
            ymax: -axisLength,
            xmin: axisLength,
            ymin: axisLength
        };
        [year, getTrailingYear(year)].forEach(function (year) {
            angular.forEach(yearSquareMap[year], function (square, index) {
                ret.xmax = Math.max(square.location.x, ret.xmax);
                ret.ymax = Math.max(square.location.y, ret.ymax);
                ret.xmin = Math.min(square.location.x, ret.xmin);
                ret.ymin = Math.min(square.location.y, ret.ymin);
            });
        });
        return ret;
    }

    function selectYear(year) {
        state.selectedYear = year;
        state.squares = yearSquareMap[year];
        state.visibleRemainderSquares = yearSquareMap[year - 1];
        console.log('Year selected:' + year, state);
    }

    function updateState(squares) {
        yearSquareMap = squares;
        var sortedYearsArray = Object.keys(yearSquareMap).sort();
        var firstYear = +sortedYearsArray[0];
        var lastYear = +sortedYearsArray.slice(-1)[0];
        var availableYears = [];
        for (var i = firstYear; i <= lastYear; i++) {
            availableYears.push(i);
        }
        state.availableYears = availableYears;
        state.forwardYear = availableYears.slice(-1).pop() + 1;
        state.backwardYear = availableYears[0] - 1;
        selectYear(state.availableYears.slice(-1).pop());
        console.log('Plants state updated', state);
    }

    function reloadPlants() {
        return $http.get('rest/plant')
            .then(function (response) {
                console.log('Garden retrieve successful. Response:', response);
                updateState(response.data);
            });
    }

    function getTrailingYear(year) {
        var yearSource = year;
        if (yearSquareMap[yearSource - 1]) {
            return yearSource - 1;
        } else if (yearSquareMap[yearSource + 1]) {
            return yearSource + 1;
        } else {
            return Object.keys(yearSquareMap).sort().slice(-1);
        }
    }

    function sendUnsavedPlantsToServer() {
        //reset counter
        unsavedCounter = 0;
        $timeout.cancel(timedAutoSavePromise);
        //get unsaved plants
        var update = { addList: [], removeList: [] };
        angular.forEach(yearSquareMap, function (squareList) {
            angular.forEach(squareList, function (square) {
                angular.forEach(square.plants, function (plant) {
                    if (!plant.species.id) {
                        console.error('Not sending plant where species id is null:', plant);
                    } else if (plant.add && !plant.remove) {
                        update.addList.push(new PlantData(plant));
                        delete plant.add;
                        delete plant.remove;
                    } else if (plant.remove && !plant.add) {
                        update.removeList.push(new PlantData(plant));
                        delete square.plants[plant.species.id];
                    }
                });
            });
        });

        if (!update.addList.length && !update.removeList.length) {
            console.log('No plants added nor removed, skipping send to server!');
            var defer = $q.defer();
            defer.resolve();
            return defer.promise;
        }
        console.log('Sending to server', update);
        //start auto save timer
        timedAutoSavePromise = $timeout(sendUnsavedPlantsToServer, autoSaveInterval, false);
        return $http.post('rest/plant', update);
    }

    function countAutoSave() {
        unsavedCounter++;
        if (unsavedCounter > 13) {
            sendUnsavedPlantsToServer();
        }
    }

    return {
        selectYear: selectYear,
        getState: function () {
            return state;
        },
        getBounds: getBounds,
        addYear: function (year) {
            var newYearSquareArray = [];
            var copyFromSquareArray = yearSquareMap[getTrailingYear(year)];

            //add perennial from mostRecentYear
            angular.forEach(copyFromSquareArray, function (square) {
                angular.forEach(square.plants, function (plant) {
                    if (!plant.species.annual) {
                        var newLocation = new Location(year, plant.location.x, plant.location.y);
                        newYearSquareArray.push(new Square(newLocation, [plant.species]));
                    }
                });
            });

            yearSquareMap[year] = newYearSquareArray;
            updateState(yearSquareMap);
            console.log('Year added:' + year, yearSquareMap);
        },
        addSquare: function (year, x, y, species) {
            var newSquare = new Square(new Location(year, x, y), [species]);
            yearSquareMap[year].push(newSquare);
            countAutoSave();
            console.log('Square and plant added', newSquare);
            return newSquare;
        },
        removePlant: function (square) {
            angular.forEach(square.plants, function (plant, key) {
                if (plant.add) {//undo add
                    delete square.plants[key];
                    console.log('Undo add', plant);
                } else {
                    plant.remove = true;
                    console.log('Plant removed', plant);
                }
            });
            console.log('Plant(s) removed', square);
            countAutoSave();
        },
        addPlant: function (species, square) {
            if (!square.plants[species.id]) {
                square.plants[species.id] = new Plant(species, square.location, 'add');
                console.log('Plant added: ' + species.scientificName, square);
                countAutoSave();
            } else {//undo remove
                delete square.plants[species.id].remove;
                console.log('Undo remove', square);
            }
        },
        save: sendUnsavedPlantsToServer
    };
}
angular.module('smigoModule').factory('PlantService', PlantService);