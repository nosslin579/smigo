function GardenService($http, $window, $timeout, $rootScope, $q, $log, SpeciesService) {

    function Garden(plantDataArray) {
        var gardenSelf = this;

        function init(pda) {
            if (!pda || !pda.length) {
                var year = new Date().getFullYear();
                gardenSelf.yearSquareMap[year] = [new Square(new Location(year, 14, 2)), new Square(new Location(year, 0, 0))];
            }

            for (var i = 0; i < pda.length; i++) {
                var plantData = pda[i];
                var species = SpeciesService.getSpecies(plantData.speciesId);
                var square = gardenSelf.getSquare(plantData.year, plantData.x, plantData.y);
                square.plantArray.push(new Plant(species, square.location));
            }


            gardenSelf.selectedYear = gardenSelf.getAvailableYears().last();
            $log.info("Garden created:", gardenSelf);
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
            this.plantArray = [];
            this.addPlant = function (species) {
                if (!squareSelf.containsSpecies(species.id) && squareSelf.plantArray.length <= 4) {
                    var plant = new Plant(species, squareSelf.location);
                    squareSelf.plantArray.push(plant);
                    $http.post('/rest/plant', new PlantData(plant))
                        .catch(function (response) {
                            squareSelf.plantArray.splice(squareSelf.plantArray.indexOf(plant));
                        });
                    $log.debug('Plant added: ' + species.scientificName, squareSelf);
                }
                return squareSelf;
            }
            this.removePlant = function () {
                if (squareSelf.plantArray.length === 0) {
                    return;
                }
                var plant = squareSelf.plantArray.pop();
                $http.post('/rest/plant/delete', new PlantData(plant))
                    .catch(function (response) {
                        squareSelf.plantArray.push(plant);
                    });
                $log.debug('Removed plant', plant, new PlantData(plant));
            };

            this.containsFamily = function (familyId) {
                for (i in squareSelf.plantArray) {
                    var plant = squareSelf.plantArray[i];
                    if (plant.species && plant.species.family && plant.species.family.id === familyId) {
                        return true;
                    }
                }
                return false;
            };
            this.containsSpecies = function (speciesId) {
                for (i in squareSelf.plantArray) {
                    var plant = squareSelf.plantArray[i];
                    if (plant.species && plant.species.id === speciesId) {
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
                angular.forEach(square.plantArray, function (plant) {
                    if (!plant.species.annual) {
                        var newSquare = new Square(new Location(year, plant.location.x, plant.location.y)).addPlant(plant.species);
                        newYearSquareArray.push(newSquare);
                    }
                });
            });

            gardenSelf.yearSquareMap[year] = newYearSquareArray;
            gardenSelf.selectedYear = year;
        };

        this.setPlants = function (pda) {
            gardenSelf.yearSquareMap = {};
            init(pda);
        };

        init(plantDataArray);
    }

    return {
        getGarden: function (username) {
            return $http.get('/rest/plant/' + username)
                .then(function (response) {
                    $log.info('plantArray retrieved successfully. Response:', response);
                    return new Garden(response.data);
                });
        },
        createGarden: function (plantDataArray) {
            return new Garden(plantDataArray);
        }
    };
}
angular.module('smigoModule').factory('GardenService', GardenService);