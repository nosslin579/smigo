function PlantService($http, $window, $timeout, $rootScope, $q, $log, SpeciesService) {
    var state = {},
        garden = new Garden(new Date().getFullYear()),
        updatePlants = {addList: [], removeList: []},
        updatePlantsPromise;

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

    function Plant(species, location) {
        this.species = species;
        this.location = location;
    }

    function Square(location, speciesArray) {
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
                this.plants[species.id] = new Plant(species, location);
            }, this);
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

        this.addPlant = function (plant) {
            var square = getSquare(plant.year, plant.x, plant.y);
            square.plants[plant.speciesId] = new Plant(plant.species, square.location);
        };

        this.getAvailableYears = function () {
            return Object.keys(yearSquareMap).map(Number).sort()
        };

        this.getSquares = function (year) {
            return yearSquareMap[year];
        };

        this.addYear = function (year) {
            var newYearSquareArray = [];
            var copyFromSquareArray = yearSquareMap[getTrailingYear(year)];

            //add perennial from mostRecentYear
            angular.forEach(copyFromSquareArray, function (square) {
                angular.forEach(square.plants, function (plant) {
                    if (!plant.species.annual) {
                        var newSquare = new Square(new Location(year, plant.location.x, plant.location.y));
                        newYearSquareArray.push(newSquare);
                        addPlant(plant.species, newSquare);
                    }
                });
            });

            yearSquareMap[year] = newYearSquareArray;
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
        state.squares = garden.getSquares(year);
        state.visibleRemainderSquares = garden.getSquares(year - 1);
        $log.log('Year selected:' + year, state);
    }

    function updateState(newGarden) {
        garden = newGarden;
        state.availableYears = newGarden.getAvailableYears();
        state.forwardYear = state.availableYears.last() + 1;
        state.backwardYear = state.availableYears[0] - 1;
        selectYear(state.availableYears.last());
        $log.log('Plants state updated', state);
    }


    function sendToServer(plant) {

        function removePlantData(plantDataList, plant) {
            for (var i = 0; i < plantDataList.length; i++) {
                var plantData = plantDataList[i];
                if (plantData.year == plant.location.year && plantData.x == plant.location.x &&
                    plantData.y == plant.location.y && plantData.speciesId == plant.species.id) {
                    $log.debug('Found plant in opposite list. Removing from list.', plantDataList)
                    plantDataList.splice(i, 1);
                    return true;
                }
            }
            return false;
        }

        $timeout.cancel(updatePlantsPromise);
        plant.add && !removePlantData(updatePlants.removeList, plant.add) && updatePlants.addList.push(new PlantData(plant.add));
        plant.remove && !removePlantData(updatePlants.addList, plant.remove) && updatePlants.removeList.push(new PlantData(plant.remove));
        if (updatePlants.addList.length == 0 && updatePlants.removeList.length == 0) {
            $log.debug('Both addlist and removelist is empty. Not creating new updatePlantsPromise.');
            return;
        }
        updatePlantsPromise = $timeout(function () {
            $http.post('rest/plant', updatePlants)
                .then(function (response) {
                    $log.debug('Plants saved', angular.copy(updatePlants));
                    updatePlants.addList = [];
                    updatePlants.removeList = [];
                });
        }, 7000);
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
        if (garden.getSquares(yearSource - 1)) {
            return yearSource - 1;
        } else if (garden.getSquares(yearSource + 1)) {
            return yearSource + 1;
        } else {
            return garden.getAvailableYears().last();
        }
    }

    function addPlant(species, square) {
        if (!square.plants[species.id]) {
            square.plants[species.id] = new Plant(species, square.location);
            sendToServer({add: square.plants[species.id]});
            $log.log('Plant added: ' + species.scientificName, square);
        }
    }

    return {
        selectYear: selectYear,
        getState: function () {
            return state;
        },
        addYear: function (year) {
            garden.addYear(year);
            updateState(garden);
            $log.log('Year added:' + year, garden);
        },
        addSquare: function (year, x, y, species) {
            var newSquare = new Square(new Location(year, x, y));
            garden.getSquares(year).push(newSquare);
            addPlant(species, newSquare);
            $log.log('Square and plant added', newSquare);
            return newSquare;
        },
        removePlant: function (square) {
            angular.forEach(square.plants, function (plant, key) {
                sendToServer({remove: plant});
            });
            square.plants = {};
            $log.log('Plant(s) removed', square);
        },
        addPlant: addPlant,
        getGarden: getGarden
    };
}
angular.module('smigoModule').factory('PlantService', PlantService);