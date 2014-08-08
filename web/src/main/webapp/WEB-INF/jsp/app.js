"use strict";
var app = angular.module('speciesModule', ['ngRoute']);

app.config(function ($routeProvider) {
    $routeProvider.
        when('/help', {
            templateUrl: 'help.html',
            controller: 'AboutController'
        }).
        when('/login', {
            templateUrl: 'login.html',
            controller: 'LoginController'
        }).
        when('/garden', {
            templateUrl: 'garden.html',
            controller: 'GardenController'
        }).
        otherwise({redirectTo: '/garden'});

});

app.run(function ($rootScope) {
    $rootScope.currentUser = '${pageContext.request.remoteUser}';
});

app.filter('translate', function () {
    var msg = <c:out escapeXml="false" value="${f:toJson(messages)}" />;
    return function (messageObject) {
        if (!messageObject) {
            console.error('Can not translate', messageObject);
            return 'n/a';
        }
        if (messageObject.messageKey) {
            return msg[messageObject.messageKey];
        }
        return msg[messageObject];
    };
});

app.factory('plantService', function ($http, $rootScope) {
    console.log('plantService');
    var garden = <c:out escapeXml="false" value="${f:toJson(garden)}"/>;

    function PlantData(plant) {
        this.year = plant.location.year;
        this.y = plant.location.y;
        this.x = plant.location.x;
        this.speciesId = plant.species.id;
    }

    var ret = {
        reloadGarden: function () {
            $http.get('rest/garden').success(function (data) {
                console.log('Garden reloaded', data);
                garden = data;
            }).error(function (data, status, headers, config) {
                console.error('Could not reload garden', [data, status, headers, config]);
            });
        },
        getGarden: function () {
            return garden;
        },
        addYear: function (garden, year) {
            var mostRecentYear = Object.keys(garden.squares).sort().slice(-1).pop();
            var newYearSquareArray = [];
            var copyFromSquareArray = garden.squares[mostRecentYear];

            angular.forEach(copyFromSquareArray, function (square) {
                angular.forEach(square.plants, function (plant) {
                    if (!plant.species.annual) {
                        var newLocation = {year: year, x: plant.location.x, y: plant.location.y};
                        var newSquare = {location: newLocation, plants: {}};
                        newSquare.plants[plant.species.id] = {species: plant.species, location: newLocation, add: true};
                        newYearSquareArray.push(newSquare);
                    }
                });
            });

            garden.squares[year] = newYearSquareArray;
            console.log('Year added:' + year, garden.squares);
        },
        getAvailableYears: function (squares) {
            var sortedYearsArray = Object.keys(squares).sort();
            var firstYear = +sortedYearsArray[0];
            var lastYear = +sortedYearsArray[sortedYearsArray.length - 1];
            var ret = [];
            ret.push({year: firstYear - 1, exists: false});
            for (var i = firstYear; i <= lastYear; i++) {
                ret.push({year: i, exists: sortedYearsArray.indexOf(i.toString()) !== -1});
            }
            ret.push({year: lastYear + 1, exists: false});
            console.log('AvailableYears', ret);
            return ret;
        },
        addSquare: function (garden, year, x, y) {
            var newSquare = {
                location: {
                    x: x,
                    y: y,
                    year: +year
                },
                plants: {}
            };
            garden.squares[year].push(newSquare);
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
        },
        addPlant: function (species, square) {
            if (!square.plants[species.id]) {
                square.plants[species.id] = { species: species, location: square.location};
            }
            square.plants[species.id].add = true;
            console.log('Species added', square.plants);
        },
        save: function (yearMap) {
            var update = { addList: [], removeList: [] };
            angular.forEach(yearMap, function (squareList) {
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
            $http.post('rest/garden', update);
        }
    };

    $rootScope.$watch('currentUser', function (newValue, oldValue, scope) {
        if (newValue !== oldValue) {
            console.log('currentuser', newValue, oldValue, scope);
            ret.reloadGarden();
        }
    });

    return ret;
});

app.controller('GardenController', function ($scope, $http, plantService) {
    console.log('GardenController');
    $scope.garden = plantService.getGarden();

    $scope.selectSpecies = function (species) {
        console.log('Selected species set to', species);
        $scope.selectedSpecies = species;
    };

    $scope.availableYears = plantService.getAvailableYears($scope.garden.squares);

    $scope.selectYear = function (availableYear) {
        if (!availableYear.exists) {
            plantService.addYear($scope.garden, availableYear.year);
            $scope.availableYears = plantService.getAvailableYears($scope.garden.squares);
        }
        $scope.selectedYear = availableYear.year;
        console.log('Year set to', availableYear, $scope.garden);
    };

    $scope.onSquareClick = function (clickEvent, square) {
        console.log('Square clicked', [clickEvent, square, $scope.selectedSpecies]);
        if (clickEvent.shiftKey) {
            plantService.removePlant(square);
        } else if (clickEvent.ctrlKey) {
            console.log('Copy species');
        } else {
            plantService.addPlant($scope.selectedSpecies, square);
        }
        plantService.save($scope.garden.squares);
        clickEvent.stopPropagation();
    };

    $scope.onGridClick = function (clickEvent) {
        var x = Math.floor((clickEvent.offsetX - 100000) / 48);
        var y = Math.floor((clickEvent.offsetY - 100000) / 48);
        var addedSquare = plantService.addSquare($scope.garden, $scope.selectedYear, x, y);
        plantService.addPlant($scope.selectedSpecies, addedSquare);
        plantService.save($scope.garden.squares);
        clickEvent.stopPropagation();
    };

    $scope.getGridSizeCss = function (squares) {
        var xmax = -9999, ymax = -9999, xmin = 9999, ymin = 9999;
        angular.forEach(squares, function (square, index) {
            xmax = Math.max(square.location.x, xmax);
            ymax = Math.max(square.location.y, ymax);
            xmin = Math.min(square.location.x, xmin);
            ymin = Math.min(square.location.y, ymin);
        });
        console.log('Grid size', ymin, xmax, ymax, xmin);
        return {
            'margin-top': (-100000 + 48 + -ymin * 48) + 'px',
            'width': (100000 + 96 + xmax * 48) + 'px',
            'height': (100000 + 96 + ymax * 48) + 'px',
            'margin-left': (-100000 + 48 + -xmin * 48) + 'px'
        };
    };

    $scope.getSquarePositionCss = function (square) {
        return {
            top: square.location.y * 48 + 100000 + 'px',
            left: square.location.x * 48 + 100000 + 'px'
        };
    };

    $scope.selectSpecies($scope.garden.species["1"]);
    $scope.selectedYear = Object.keys($scope.garden.squares).sort().slice(-1).pop();
});

app.controller('LoginController', function ($route, $scope, $http, $location, plantService, $rootScope) {
    $scope.loginFormModel = {
        username: 'testreg17',
        password: 'testreg17'
    };
    $scope.login = function (form) {
        // If form is invalid, return and let AngularJS show validation errors.
        if (form.$invalid) {
            return;
        }

        $http({
            method: 'POST',
            url: 'login',
            data: $.param($scope.loginFormModel),
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function (data, status, headers, config) {
            $rootScope.currentUser = $scope.loginFormModel.username;
            delete $scope.loginFormModel;
            $location.path('/garden');
        }).error(function (data, status, headers, config) {
            delete $rootScope.currentUser;
        });
        console.log('Login', $scope);

    }
});

app.controller('AboutController', function () {
});

app.controller('MainMenuController', function ($http, $scope, $rootScope) {
    $scope.logout = function () {
        $http({
            method: 'GET',
            url: 'logout'
        }).success(function (data, status, headers, config) {
            delete $rootScope.currentUser;
        }).error(function (data, status, headers, config) {
            delete $rootScope.currentUser;
        });

    };
});
