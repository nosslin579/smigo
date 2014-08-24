function PlantService($http, $window, $timeout) {
    console.log('plantService');
    var garden = <c:out escapeXml="false" value="${f:toJson(garden)}"/>,
        selectedYear = +Object.keys(garden.squares).sort().slice(-1).pop(),
        selectedSpecies = garden.species["28"],
        unsavedCounter = 0,
        autoSaveInterval = 60000,
        timedAutoSavePromise = $timeout(sendUnsavedPlantsToServer, autoSaveInterval, false);

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

    function reloadGarden() {
        var promise = $http.get('rest/garden');
        promise.success(function (data) {
            console.log('Garden reloaded', data);
            garden = data;
        });
        promise.error(function (data, status, headers, config) {
            console.error('Could not reload garden', [data, status, headers, config]);
        });
        return promise;
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
            angular.forEach(garden.squares[year], function (square, index) {
                ret.xmax = Math.max(square.location.x, ret.xmax);
                ret.ymax = Math.max(square.location.y, ret.ymax);
                ret.xmin = Math.min(square.location.x, ret.xmin);
                ret.ymin = Math.min(square.location.y, ret.ymin);
            });
        });
        return ret;
    }

    function getTrailingYear(year) {
        var yearSource = typeof year === 'undefined' ? selectedYear : year;
        if (garden.squares[yearSource - 1]) {
            return yearSource - 1;
        } else if (garden.squares[yearSource + 1]) {
            return yearSource + 1;
        } else {
            return Object.keys(garden.squares).sort().slice(-1);
        }
    }

    function sendUnsavedPlantsToServer() {
        //reset counter
        unsavedCounter = 0;
        $timeout.cancel(timedAutoSavePromise);
        //get unsaved plants
        var update = { addList: [], removeList: [] };
        angular.forEach(garden.squares, function (squareList) {
            angular.forEach(squareList, function (square) {
                angular.forEach(square.plants, function (plant) {
                    if (plant.add && !plant.remove) {
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
        console.log('Sending to server', update);
        //start auto save timer
        timedAutoSavePromise = $timeout(sendUnsavedPlantsToServer, autoSaveInterval, false);
        return $http.post('rest/garden', update);
    }

    function countAutoSave() {
        unsavedCounter++;
        if (unsavedCounter > 13) {
            sendUnsavedPlantsToServer();
        }
    }

    return {
        getTrailingYear: getTrailingYear,
        getBounds: getBounds,
        getSelectedSpecies: function () {
            return selectedSpecies;
        },
        setSelectedSpecies: function (species) {
            selectedSpecies = species;
        },
        getSelectedYear: function () {
            return selectedYear;
        },
        setSelectedYear: function (year) {
            if (!garden.squares.hasOwnProperty(year)) {
                garden.squares[year] = [];
            }
            selectedYear = year;
        },
        getGarden: function () {
            return garden;
        },
        reloadGarden: reloadGarden,
        addYear: function (year) {
            var newYearSquareArray = [];
            var copyFromSquareArray = garden.squares[getTrailingYear(year)];

            //add perennial from mostRecentYear
            angular.forEach(copyFromSquareArray, function (square) {
                angular.forEach(square.plants, function (plant) {
                    if (!plant.species.annual) {
                        var newLocation = new Location(year, plant.location.x, plant.location.y);
                        newYearSquareArray.push(new Square(newLocation, [plant.species]));
                    }
                });
            });

            garden.squares[year] = newYearSquareArray;
            selectedYear = year;
            console.log('Year added:' + year, garden.squares);
        },
        getAvailableYears: function () {
            var sortedYearsArray = Object.keys(garden.squares).sort();
            var firstYear = +sortedYearsArray[0];
            var lastYear = +sortedYearsArray.slice(-1)[0];
            var ret = [];
            for (var i = firstYear; i <= lastYear; i++) {
                ret.push(i);
            }
            console.log('AvailableYears', ret);
            return ret;
        },
        addSquare: function (year, x, y, species) {
            var newSquare = new Square(new Location(year, x, y), [species]);
            garden.squares[year].push(newSquare);
            countAutoSave();
            console.log('Square added', newSquare);
            return newSquare;
        },
        removePlant: function (square) {
            angular.forEach(square.plants, function (plant, key) {
                plant.remove = true;
                if (plant.add) {//undo add
                    delete square.plants[key];
                }
            });
            console.log('Species removed', square);
            countAutoSave();
        },
        addPlant: function (species, square) {
            if (!square.plants[species.id]) {
                square.plants[species.id] = new Plant(species, square.location, 'add');
                console.log('Plant added: ' + species.scientificName, square);
                countAutoSave();
            }
        },
        save: sendUnsavedPlantsToServer
    };
}
angular.module('smigoModule').factory('PlantService', PlantService);