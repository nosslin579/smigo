"use strict";
var app = angular.module('speciesModule', ['ngRoute']);

app.config(function ($routeProvider) {
    $routeProvider.
        when('/test', {
            templateUrl: 'test.html',
            controller: 'TestController'
        }).
        when('/help', {
            templateUrl: 'help.html',
            controller: 'AboutController'
        }).
        when('/login', {
            templateUrl: 'login.html',
            controller: 'LoginController'
        }).
        when('/register', {
            templateUrl: 'login.html',
            controller: 'RegisterController'
        }).
        when('/garden', {
            templateUrl: 'garden.html',
            controller: 'GardenController'
        }).
        otherwise({redirectTo: '/garden'});

});
app.controller('TestController', function ($scope) {
    $scope.doSomething = function () {
        alert('Submitted!');
    }
});
app.run(function ($rootScope) {
    console.log("App run");
    $rootScope.currentUser = '${pageContext.request.remoteUser}';
});
app.filter('translate', function () {
    var msg = <c:out escapeXml="false" value="${f:toJson(messages)}" />;
    return function (messageObject, param) {
        if (!messageObject) {
            console.error('Can not translate', messageObject);
            return 'n/a';
        }
        if (messageObject.messageKey) {
            return msg[messageObject.messageKey];
        }
        if (!msg[messageObject]) {
            console.error('Could not translate:' + messageObject);
            return messageObject;
        }
        return msg[messageObject].replace('{0}', param);
    };
});
app.directive('equals', function () {
    return {
        restrict: 'A', // only activate on element attribute
        require: '?ngModel', // get a hold of NgModelController
        link: function (scope, elem, attrs, ngModel) {
            console.log('Equals', [scope, elem, attrs, ngModel]);
            if (!ngModel) return; // do nothing if no ng-model

            // watch own value and re-validate on change
            scope.$watch(attrs.ngModel, function () {
                validate();
            });

            // observe the other value and re-validate on change
            attrs.$observe('equals', function (val) {
                validate();
            });

            var validate = function () {
                // values
                var val1 = ngModel.$viewValue;
                var val2 = attrs.equals;

                // set validity
                ngModel.$setValidity('equals', val1 === val2);
            };
        }
    }
});
app.directive('rememberScroll', function ($timeout) {
    return {
        restrict: 'A',
        link: function (scope, $elm, attr) {
            scope.$watch('selectedYear', function (newYear, oldYear, wtf) {
                $timeout(function () {
                    $elm[0].scrollLeft = 99999;
                    $elm[0].scrollLeft = $elm[0].scrollLeft / 2;
                    $elm[0].scrollTop = 99999;
                    $elm[0].scrollTop = $elm[0].scrollTop / 2;
                }, 0, false);
            }, true);
        }
    }
});
app.factory('plantService', function ($http, $rootScope) {
    console.log('plantService');
    function PlantData(plant) {
        this.year = plant.location.year;
        this.y = plant.location.y;
        this.x = plant.location.x;
        this.speciesId = plant.species.id;
    }

    return {
        reloadGarden: function (onSuccess, onError) {
            console.log('Reloading garden');
            $http.get('rest/garden').success(onSuccess).error(onError);
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
            var lastYear = +sortedYearsArray.slice(-1)[0];
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
});
app.factory('userService', function ($rootScope, $http, $location) {
    return {
        loginOrRegister: function (form, scope, action) {
            scope.objectErrors = [];
            if (form.$invalid) {
                console.log('Form is invalid:' + action);
                //set all values to trigger dirty, so validation messages become visible
                angular.forEach(form, function (value) {
                    if (value.$setViewValue) {
                        value.$setViewValue(value.$viewValue);
                    }
                });
                return;
            }

            scope.formModel['remember-me'] = true;

            $http({
                method: 'POST',
                url: action,
                data: $.param(scope.formModel),
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).success(function (data, status, headers, config) {
                $rootScope.currentUser = scope.formModel.username;
                $location.path('/garden');
                console.log('Performed successfully action:' + action, [data, status, headers, config]);
            }).error(function (data, status, headers, config) {
                scope.objectErrors = data;
                delete $rootScope.currentUser;
                console.log('Performed unsuccessfully action:' + action, [form, data, status, headers, config]);
            });
        }};
});
app.controller('GardenController', function ($scope, $rootScope, $http, plantService) {
    console.log('GardenController', $scope);

    $scope.garden = <c:out escapeXml="false" value="${f:toJson(garden)}"/>;

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
        console.log('Year set to', [availableYear, $scope.garden]);
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
        var axisLength = squares.length === 0 ? -1 : 9999;
        var xmax = -axisLength, ymax = -axisLength, xmin = axisLength, ymin = axisLength;
        angular.forEach(squares, function (square, index) {
            xmax = Math.max(square.location.x, xmax);
            ymax = Math.max(square.location.y, ymax);
            xmin = Math.min(square.location.x, xmin);
            ymin = Math.min(square.location.y, ymin);
        });
//        console.log('Grid size', ymin, xmax, ymax, xmin);
        var margin = 48 * 5;
        return {
            'margin-top': (-100000 + -ymin * 48 + margin) + 'px',
            'width': (100000 + 48 + xmax * 48 + margin) + 'px',
            'height': (100000 + 48 + ymax * 48 + margin) + 'px',
            'margin-left': (-100000 + -xmin * 48 + margin) + 'px'
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

    $rootScope.$watch('currentUser', function (newValue, oldValue, scope) {
        console.log('currentuser value changed', [newValue, oldValue, scope]);
        if (newValue !== oldValue) {
            plantService.reloadGarden(
                function (data) {
                    $scope.garden = data;
                    $scope.availableYears = plantService.getAvailableYears(data.squares);
                    $scope.selectedYear = Object.keys(data.squares).sort().slice(-1).pop();
                },
                function (data, status, headers, config) {
                    console.error('Could not reload garden', [data, status, headers, config]);
                });
        }
    });
});
app.controller('LoginController', function ($scope, userService) {
    $scope.viewModel = {
        login: true,
        usernameMin: 0,
        usernameMax: 999,
        usernamePattern: /.+/,
        passwordMin: 0,
        pageMessageKey: 'account.login'
    };
    $scope.formModel = {
        username: 'user7389327855123',
        password: 'testreg17'
    };
    $scope.submitLoginOrRegisterForm = function (form) {
        console.log('loginOrRegister', [form, $scope]);
        userService.loginOrRegister(form, $scope, 'login');
    }
});
app.controller('RegisterController', function ($scope, userService) {
    $scope.viewModel = {
        register: true,
        usernameMin: 5,
        usernameMax: 40,
        usernamePattern: /^[\w]+$/,
        passwordMin: 6,
        pageMessageKey: 'msg.account.register'
    };

    /*
     var newName = 'testreg' + Math.floor(Math.random() * 999999999);
     $scope.formModel = {
     username: newName,
     password: newName,
     passwordAgain: newName
     };
     */

    $scope.submitLoginOrRegisterForm = function (form) {
        userService.loginOrRegister(form, $scope, 'register');
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
