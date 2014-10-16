function PlantService($http, $window, $timeout, $rootScope, $q, $log, SpeciesService) {
    var state = {},
        garden = new Garden(new Date().getFullYear()),
        unsavedCounter = 0,
        autoSaveInterval = 60000,
        timedAutoSavePromise = $timeout(sendUnsavedPlantsToServer, autoSaveInterval, false);

    updateState(createGarden(initData.plantDataArray));

    $log.log('PlantService', state);

    $rootScope.$on('current-user-changed', function (event, user) {
        if (user) {
            reloadPlants();
        } else {
            var year = new Date().getFullYear();
            updateState(new Garden(year));
        }
    });

    $rootScope.$on('locale-changed', function (event, locale) {
        reloadPlants();
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

    function Garden(year) {
        var yearSquareMap = {};
        if (year) {
            yearSquareMap[year] = [new Square(new Location(year, 0, 0))];
        }

        function getSquare(year, x, y) {
            if (!yearSquareMap[year]) {
                yearSquareMap[year] = [];
            }
            for (var i = 0; i < yearSquareMap[year].length; i++) {
                var square = yearSquareMap[year][i];
                if (square.x === x && square.y === y) {
                    return square;
                }
            }
            var ret = new Square(new Location(year, x, y));
            yearSquareMap[year].push(ret);
            return ret;
        };

        this.yearSquareMap = yearSquareMap;

        this.addPlant = function (plant) {
            var square = getSquare(plant.year, plant.x, plant.y);
            square.plants[plant.speciesId] = new Plant(plant.species, square.location);
        };

        this.getAvailableYears = function () {
            return Object.keys(yearSquareMap).sort()
        };

        this.getSquares = function (year) {
            return yearSquareMap[year];
        };
    }

    function createGarden(plantDataArray) {
        if (!plantDataArray.length) {
            return new Garden(new Date().getFullYear());
        }
        var ret = new Garden();
        for (var i = 0; i < plantDataArray.length; i++) {
            var plantData = plantDataArray[i];
            plantData.species = SpeciesService.getSpecies(plantData.speciesId);
            ret.addPlant(plantData);
        }
        $log.debug("Garden created:", ret);
        return ret;
    }

    function selectYear(year) {
        state.selectedYear = year;
        state.squares = garden.yearSquareMap[year];
        state.visibleRemainderSquares = garden.yearSquareMap[year - 1];
        $log.log('Year selected:' + year, state);
    }

    function updateState(newGarden) {
        garden = newGarden;
        state.availableYears = Object.keys(newGarden.yearSquareMap).map(Number).sort();
        state.forwardYear = state.availableYears.last() + 1;
        state.backwardYear = state.availableYears[0] - 1;
        selectYear(state.availableYears.last());
        $log.log('Plants state updated', state);
    }

    function reloadPlants() {
        getGarden()
            .then(function (garden) {
                updateState(garden);
            });
    }

    function getGarden(username) {
        var url = 'rest/plant/' + (username ? username : '');
        return $http.get(url)
            .then(function (response) {
                $log.info('Plants retrieved successfully. Response:', response);
                return createGarden(response.data);
            });
    }

    function getTrailingYear(year) {
        var yearSource = year;
        if (garden.yearSquareMap[yearSource - 1]) {
            return yearSource - 1;
        } else if (garden.yearSquareMap[yearSource + 1]) {
            return yearSource + 1;
        } else {
            return Object.keys(garden.yearSquareMap).sort().slice(-1);
        }
    }

    function sendUnsavedPlantsToServer() {
        //reset counter
        unsavedCounter = 0;
        $timeout.cancel(timedAutoSavePromise);
        //get unsaved plants
        var update = { addList: [], removeList: [] };
        angular.forEach(garden.yearSquareMap, function (squareList) {
            angular.forEach(squareList, function (square) {
                angular.forEach(square.plants, function (plant) {
                    if (!plant.species.id) {
                        $log.error('Not sending plant where species id is null:', plant);
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
            $log.log('No plants added nor removed, skipping send to server!');
            var defer = $q.defer();
            defer.resolve();
            return defer.promise;
        }
        $log.log('Sending to server', update);
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
        addYear: function (year) {
            var newYearSquareArray = [];
            var copyFromSquareArray = garden.yearSquareMap[getTrailingYear(year)];

            //add perennial from mostRecentYear
            angular.forEach(copyFromSquareArray, function (square) {
                angular.forEach(square.plants, function (plant) {
                    if (!plant.species.annual) {
                        var newLocation = new Location(year, plant.location.x, plant.location.y);
                        newYearSquareArray.push(new Square(newLocation, [plant.species]));
                    }
                });
            });

            garden.yearSquareMap[year] = newYearSquareArray;
            updateState(garden);
            $log.log('Year added:' + year, garden);
        },
        addSquare: function (year, x, y, species) {
            var newSquare = new Square(new Location(year, x, y), species ? [species] : []);
            garden.yearSquareMap[year].push(newSquare);
            countAutoSave();
            $log.log('Square and plant added', newSquare);
            return newSquare;
        },
        removePlant: function (square) {
            angular.forEach(square.plants, function (plant, key) {
                if (plant.add) {//undo add
                    delete square.plants[key];
                    $log.log('Undo add', plant);
                } else {
                    plant.remove = true;
                    $log.log('Plant removed', plant);
                }
            });
            $log.log('Plant(s) removed', square);
            countAutoSave();
        },
        addPlant: function (species, square) {
            if (!square.plants[species.id]) {
                square.plants[species.id] = new Plant(species, square.location, 'add');
                $log.log('Plant added: ' + species.scientificName, square);
                countAutoSave();
            } else {//undo remove
                delete square.plants[species.id].remove;
                $log.log('Undo remove', square);
            }
        },
        save: sendUnsavedPlantsToServer,
        getGarden: getGarden
    };
}
angular.module('smigoModule').factory('PlantService', PlantService);