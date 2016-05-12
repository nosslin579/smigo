function GardenService($http, $window, $timeout, $rootScope, $q, $log, SpeciesService) {

    function Garden(plantDataArray, mutable) {
        var gardenSelf = this;

        function addRawPlantData(plantDataArray) {
            for (var i = 0; i < plantDataArray.length; i++) {
                var plantData = plantDataArray[i];
                var species = SpeciesService.getSpecies(plantData.speciesId);
                var square = gardenSelf.getSquare(plantData.year, plantData.x, plantData.y);
                species.variety = plantData.varietyId == 0 ? null : SpeciesService.getAllVarieties().smigoFind(plantData.varietyId, 'id');
                square.plantArray.push(new Plant(plantData.id, species, square.location));
            }


            gardenSelf.selectedYear = gardenSelf.getAvailableYears().smigoLast();
            if (!mutable) {
                gardenSelf.getSquare = function () {
                    return new Square(new Location(0, 0, 0));
                };
            }
            $log.info("Garden initialized:", gardenSelf);
        }

        function PlantData(plant) {
            this.year = plant.location.year;
            this.y = plant.location.y;
            this.x = plant.location.x;
            this.speciesId = plant.species.id;
            if (plant.variety) {
                this.varietyId = plant.variety.id;
            }
        }

        function Location(year, x, y) {
            if (!$.isNumeric(year) || !$.isNumeric(x) || !$.isNumeric(y)) {
                throw "Location takes Numeric parameter only. x:" + x + " y:" + y + " year:" + year;
            }
            this.year = +year;
            this.x = +x;
            this.y = +y;
        }

        function Plant(id, species, location) {
            var plantSelf = this;
            this.id = id;
            this.species = species;
            this.location = location;
            this.variety = species.variety;

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
            this.togglePlant = function (species) {
                if (squareSelf.containsSpecies(species.id)) {
                    squareSelf.removePlant(species);
                } else {
                    squareSelf.addPlant(species);
                }
            };
            this.addPlant = function (species) {
                if (squareSelf.plantArray.length <= 4 && mutable) {
                    var plant = new Plant(0, species, squareSelf.location);
                    squareSelf.plantArray.push(plant);
                    $http.post('/rest/plant', new PlantData(plant)).then(function (response) {
                        plant.id = response.data;
                        $log.log('Response from /rest/plant', [response, species, squareSelf]);
                    }).catch(function (response) {
                        squareSelf.plantArray.splice(squareSelf.plantArray.indexOf(plant));
                        $log.error('Plant add failed: ', [response, species, squareSelf]);
                    });
                }
            };
            this.removePlant = function (species) {
                if (squareSelf.plantArray.length === 0 || !mutable) {
                    return;
                }
                var removeObj = {};
                if (species) {
                    squareSelf.plantArray.forEach(function removePlantIter(plant, index, array) {
                        if (species.id === plant.species.id) {
                            this.plant = array.splice(index, 1)[0];
                        }
                    }, removeObj);
                } else {
                    removeObj.plant = squareSelf.plantArray.pop();
                }

                $http.delete('/rest/plant/' + removeObj.plant.id).then(function (response) {
                    $log.log('Response from /rest/plant/' + removeObj.plant.id, [response, removeObj])
                }).catch(function (response) {
                    squareSelf.plantArray.push(removeObj.plant);
                    $log.error('Remove plant failed', [response, removeObj]);
                }).finally(function () {
                });
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
        this.getTrailingSquares = function () {
            return gardenSelf.yearSquareMap[gardenSelf.getTrailingYear()];
        };
        this.getTrailingYear = function () {
            var availableYears = gardenSelf.getAvailableYears();
            if (availableYears.indexOf(gardenSelf.selectedYear - 1) !== -1) {
                return gardenSelf.selectedYear - 1;
            } else if (availableYears.indexOf(gardenSelf.selectedYear + 1) !== -1) {
                return gardenSelf.selectedYear + 1;
            }
        };

        this.addYear = function (year) {
            if (gardenSelf.yearSquareMap[year]) {
                throw 'Cant add year that already exists';
            }

            $http.put('/rest/plant/add-year/' + year).then(function (response) {
                addRawPlantData(response.data);
                gardenSelf.selectedYear = year;
            }).catch(function () {
                $log.error('Could not add year:' + year);
            });
        };

        this.setPlants = function (pda) {
            gardenSelf.yearSquareMap = {};
            if (!pda || !pda.length) {
                var year = new Date().getFullYear();
                gardenSelf.yearSquareMap[year] = [new Square(new Location(year, 14, 2)), new Square(new Location(year, 0, 0))];
            }
            addRawPlantData(pda);
        };

        gardenSelf.setPlants(plantDataArray);
    }

    return {
        getGarden: function (username, mutable) {
            var garden = new Garden([], mutable);
            $http.get('/rest/plant/' + username).then(function (response) {
                garden.setPlants(response.data);
            });
            return garden;
        }
    };
}
angular.module('smigoModule').factory('GardenService', GardenService);