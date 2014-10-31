function PlantService($http, $window, $timeout, $rootScope, $q, $log, SpeciesService) {
    var state = {},
        garden = new Garden([]),
        updatePlants = {addList: [], removeList: []},
        updatePlantsPromise;

    updateState(new Garden(initData.plantDataArray));

    $log.log('PlantService', state);

    $rootScope.$on('current-user-changed', function (event, user) {
        if (user) {
            reloadPlants();
        } else {
            updateState(new Garden([]));
        }
    });

    $window.addEventListener("beforeunload", function (event) {
        $http.post('/rest/plant', updatePlants);

    });

    $rootScope.$on('user-logout', function () {
        $http.post('/rest/plant', updatePlants);
        $log.info('Saving plants before logout', updatePlants);
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
        var self = this;
        this.species = species;
        this.location = location;

        function hasRuleHint(rule, location) {
            var radius = 1,
                fromYear = location.year - rule.yearsBack.min,
                toYear = location.year - rule.yearsBack.max;
            for (var year = fromYear; year >= toYear; year--) {
                var squares = garden.getSquares(year);
                if (squares) {
                    for (var j = 0; j < squares.length; j++) {
                        var square = squares[j],
                            xDiff = Math.abs(location.x - square.location.x),
                            yDiff = Math.abs(location.y - square.location.y);
                        if (xDiff <= radius && yDiff <= radius) {
                            if (rule.hasCauser(square)) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }

        this.getHints = function () {
            $log.log('Get hints for', self);
            var ret = [];
            for (var i = 0; i < self.species.rules.length; i++) {
                var rule = self.species.rules[i];
                if (hasRuleHint(rule, self.location)) {
                    ret.push(rule.hint)
                }
            }
            return ret;
        };
    }

    function Square(location) {
        if (!location instanceof Location) {
            throw "Square.location must be a Location object. location:" + location;
        }
        var self = this;
        this.location = location;
        this.plants = {};
        this.setPlant = function (species) {
            self.plants[species.id] = new Plant(species, self.location);
        }
        this.addPlant = function (species) {
            if (!self.plants[species.id]) {
                self.plants[species.id] = new Plant(species, self.location);
                sendToServer({add: self.plants[species.id]});
                $log.debug('Plant added: ' + species.scientificName, self);
            }
            return self;
        }
        this.removePlant = function () {
            $log.debug('Removing plant(s)', angular.copy(self));
            angular.forEach(self.plants, function (plant, id) {
                sendToServer({remove: plant});
            });
            self.plants = {};
        };

        this.containsFamily = function (familyId) {
            for (speciesId in self.plants) {
                var plant = self.plants[speciesId];
                if (plant.species && plant.species.family && plant.species.family.id === familyId) {
                    return true;
                }
            }
            return false;
        };
    }

    function Garden(plantDataArray) {
        var self = this, yearSquareMap = {};

        function init() {
            if (!plantDataArray || !plantDataArray.length) {
                var year = new Date().getFullYear();
                yearSquareMap[year] = [new Square(new Location(year, 0, 0))];
            }

            for (var i = 0; i < plantDataArray.length; i++) {
                var plantData = plantDataArray[i];
                var species = SpeciesService.getSpecies(plantData.speciesId);
                self.getSquare(plantData.year, plantData.x, plantData.y).setPlant(species);
            }
            $log.info("Garden created:", self);
        }

        this.getSquare = function (year, x, y) {
            if (!yearSquareMap[year]) {
                yearSquareMap[year] = [];
            }
            for (var i = 0; i < yearSquareMap[year].length; i++) {
                var square = yearSquareMap[year][i];
                if (square.location.x === x && square.location.y === y) {
                    return square;
                }
            }
            var ret = new Square(new Location(year, x, y));
            yearSquareMap[year].push(ret);
            return ret;
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
                        var newSquare = new Square(new Location(year, plant.location.x, plant.location.y)).addPlant(plant.species);
                        newYearSquareArray.push(newSquare);
                    }
                });
            });

            yearSquareMap[year] = newYearSquareArray;
        };

        init();
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

        function undo(plantDataList, plant) {
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

        function doHttpPost() {
            $http.post('/rest/plant', updatePlants)
                .then(function (response) {
                    $log.debug('Plants saved', angular.copy(updatePlants));
                    updatePlants.addList = [];
                    updatePlants.removeList = [];
                })
                .catch(function (error) {
                    $log.error('Could not send update to server', error);
                    updatePlantsPromise = $timeout(doHttpPost, 10000);
                });
        };

        $timeout.cancel(updatePlantsPromise);
        plant.add && !undo(updatePlants.removeList, plant.add) && updatePlants.addList.push(new PlantData(plant.add));
        plant.remove && !undo(updatePlants.addList, plant.remove) && updatePlants.removeList.push(new PlantData(plant.remove));
        if (updatePlants.addList.length == 0 && updatePlants.removeList.length == 0) {
            $log.debug('Both addlist and removelist is empty. Not creating new updatePlantsPromise.');
            return;
        }
        updatePlantsPromise = $timeout(doHttpPost, 3000);
    }

    function reloadPlants() {
        getGarden()
            .then(function (garden) {
                updateState(garden);
            });
    }

    function getGarden(username) {
        var url = '/rest/plant/' + (username ? username : '');
        return $http.get(url)
            .then(function (response) {
                $log.info('Plants retrieved successfully. Response:', response);
                return new Garden(response.data);
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
        throw 'deprecated 1'
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
        addSquare: garden.getSquare,
        removePlant: function (square) {
            throw 'deprecated 2'
        },
        getGarden: getGarden
    };
}
angular.module('smigoModule').factory('PlantService', PlantService);