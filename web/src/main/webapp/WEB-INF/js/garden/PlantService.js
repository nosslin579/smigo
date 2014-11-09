function PlantService($http, $window, $timeout, $rootScope, $q, $log, SpeciesService) {

    function Garden(plantDataArray) {
        var gardenSelf = this,
            updatePlants = {addList: [], removeList: []},
            updatePlantsPromise;

        function init(pda) {
            if (!pda || !pda.length) {
                var year = new Date().getFullYear();
                gardenSelf.yearSquareMap[year] = [new Square(new Location(year, 0, 0))];
            }

            for (var i = 0; i < pda.length; i++) {
                var plantData = pda[i];
                var species = SpeciesService.getSpecies(plantData.speciesId);
                gardenSelf.getSquare(plantData.year, plantData.x, plantData.y).setPlant(species);
            }

            gardenSelf.selectedYear = gardenSelf.getAvailableYears().last();
            $log.info("Garden created:", gardenSelf);
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
            var plantSelf = this;
            this.species = species;
            this.location = location;

            function hasRuleHint(rule, location) {
                var radius = 1,
                    fromYear = location.year - rule.yearsBack.min,
                    toYear = location.year - rule.yearsBack.max;
                for (var year = fromYear; year >= toYear; year--) {
                    var squares = gardenSelf.yearSquareMap[year];
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
                $log.log('Get hints for', gardenSelf);
                var ret = [];
                for (var i = 0; i < plantSelf.species.rules.length; i++) {
                    var rule = plantSelf.species.rules[i];
                    if (hasRuleHint(rule, plantSelf.location)) {
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
            var squareSelf = this;
            this.location = location;
            this.plants = {};
            this.setPlant = function (species) {
                squareSelf.plants[species.id] = new Plant(species, squareSelf.location);
            }
            this.addPlant = function (species) {
                if (!squareSelf.plants[species.id]) {
                    squareSelf.plants[species.id] = new Plant(species, squareSelf.location);
                    sendToServer({add: squareSelf.plants[species.id]});
                    $log.debug('Plant added: ' + species.scientificName, squareSelf);
                }
                return squareSelf;
            }
            this.removePlant = function () {
                $log.debug('Removing plant(s)', angular.copy(squareSelf));
                angular.forEach(squareSelf.plants, function (plant, id) {
                    sendToServer({remove: plant});
                });
                squareSelf.plants = {};
            };

            this.containsFamily = function (familyId) {
                for (speciesId in squareSelf.plants) {
                    var plant = squareSelf.plants[speciesId];
                    if (plant.species && plant.species.family && plant.species.family.id === familyId) {
                        return true;
                    }
                }
                return false;
            };
        }

        this.yearSquareMap = {};

        this.getSquare = function (year, x, y) {
            if (!gardenSelf.yearSquareMap[year]) {
                gardenSelf.yearSquareMap[year] = [];
            }
            var squares = gardenSelf.yearSquareMap[year];
            for (var i = 0; i < squares.length; i++) {
                var square = squares[i];
                if (square.location.x === x && square.location.y === y) {
                    return square;
                }
            }
            var ret = new Square(new Location(year, x, y));
            squares.push(ret);
            return ret;
        };

        this.getAvailableYears = function () {
            return Object.keys(gardenSelf.yearSquareMap).map(Number).sort()
        };

        this.getSquares = function () {
            return gardenSelf.yearSquareMap[gardenSelf.selectedYear];
        };

        this.addYear = function (year) {
            if (gardenSelf.yearSquareMap[year]) {
                throw 'Cant add year that already exists';
            }
            var newYearSquareArray = [],
                trailingYear = gardenSelf.yearSquareMap[year - 1] ? year - 1 : gardenSelf.getAvailableYears().last(),
                copyFromSquareArray = gardenSelf.yearSquareMap[trailingYear];

            //add perennial from trailingYear
            angular.forEach(copyFromSquareArray, function (square) {
                angular.forEach(square.plants, function (plant) {
                    if (!plant.species.annual) {
                        var newSquare = new Square(new Location(year, plant.location.x, plant.location.y)).addPlant(plant.species);
                        newYearSquareArray.push(newSquare);
                    }
                });
            });

            gardenSelf.yearSquareMap[year] = newYearSquareArray;
            gardenSelf.selectedYear = year;
        };

        this.save = function () {
            if (updatePlants.addList || updatePlants.removeList) {
                $http.post('/rest/plant', updatePlants);
                updatePlants.addList = [];
                updatePlants.removeList = [];
                $timeout.cancel(updatePlantsPromise);
            }
        };

        this.setPlants = function (pda) {
            gardenSelf.yearSquareMap = {};
            updatePlants = {addList: [], removeList: []};
            init(pda);
        };

        init(plantDataArray);
    }

    function addPlant(species, square) {
        throw 'deprecated 1'
    }

    return {
        getState: function () {
            throw 'use garden instead';//todo
        },
        addYear: function () {
            throw 'use garden.addyear';//todo
        },
        addSquare: function () {
            throw 'use garden.addsquare';//todo
        },
        removePlant: function (square) {
            throw 'deprecated 2'
        },
        getGarden: function (username) {
            return $http.get('/rest/plant/' + username)
                .then(function (response) {
                    $log.info('Plants retrieved successfully. Response:', response);
                    return new Garden(response.data);
                });
        },
        nisse: function () {
            throw 'deprecated 2';
        },
        createGarden: function (plantDataArray) {
            return new Garden(plantDataArray);
        }
    };
}
angular.module('smigoModule').factory('PlantService', PlantService);