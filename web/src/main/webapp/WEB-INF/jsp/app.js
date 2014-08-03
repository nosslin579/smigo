var app = angular.module('speciesModule', ['ngRoute']);

app.config(function ($routeProvider) {
    $routeProvider.
        otherwise({
            templateUrl: 'garden',
            controller: 'GardenController'
        });
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

app.factory('plantService', function ($http, $scope) {
    console.log('plantservice')
    return {
        save: function () {
            //todo
        }
    };
});

app.controller('GardenController', function ($scope, $http) {
    $scope.selectSpecies = function (species) {
        console.log('Selected species set to', species);
        $scope.selectedSpecies = species;
    };

    $scope.selectYear = function (year) {
        $scope.currentYear = year;
        $scope.squares = $scope.garden.squares[year];
        console.log('year set to', year, [$scope.squares]);
    };

    $scope.onSquareClick = function (clickEvent, square) {
        console.log('onSquareClick', [clickEvent, square, $scope.selectedSpecies]);
        var plant = square.plants[this.selectedSpecies.id];
        if (clickEvent.shiftKey) {
            angular.forEach(square.plants, function (plant, key) {
                plant.remove = true;
                if (plant.add) {//undo add
                    delete square.plants[key];
                }
            });
            console.log('Species removed', square);
        } else if (clickEvent.ctrlKey) {
            console.log('Copy species');
        } else {
            if (!plant) {
                square.plants[this.selectedSpecies.id] = { species: this.selectedSpecies, location: square.location};
            }
            square.plants[this.selectedSpecies.id].add = true;
            console.log('Species added', square.plants);
        }
//        plantService.save();
        clickEvent.stopPropagation();
    };

    $scope.addSquare = function (clickEvent) {
        var newSquare = {
            location: {
                x: Math.floor((clickEvent.offsetX - 100000) / 48),
                y: Math.floor((clickEvent.offsetY - 100000) / 48),
                year: this.currentYear
            },
            plants: {}
        };
        $scope.squares.push(newSquare);
        console.log('Square added', [newSquare, clickEvent, this]);
        this.onSquareClick(clickEvent, newSquare);
    };

    $scope.gridSize = function (squares) {
        var xmax = -4, ymax = -2, xmin = 4, ymin = 2;
        angular.forEach(squares, function (square, index) {
            xmax = Math.max(square.location.x, xmax);
            ymax = Math.max(square.location.y, ymax);
            xmin = Math.min(square.location.x, xmin);
            ymin = Math.min(square.location.y, ymin);
        });
        return {
            'margin-top': (-100000 + 48 + Math.abs(ymin) * 48) + 'px',
            'width': (100000 + 96 + Math.abs(xmax) * 48) + 'px',
            'height': (100000 + 96 + Math.abs(ymax) * 48) + 'px',
            'margin-left': (-100000 + 48 + Math.abs(xmin) * 48) + 'px'
        };
    };

    $scope.squarePostion = function (square) {
//        smigolog('squarePostion', square);
        return {
            top: square.location.y * 48 + 100000 + 'px',
            left: square.location.x * 48 + 100000 + 'px'
        };
    };

    $scope.species = <c:out escapeXml="false" value="${f:toJson(species)}"/>;
    $scope.garden = <c:out escapeXml="false" value="${f:toJson(garden)}"/>;
    smigolog("garden", $scope.garden);

    $scope.refresh = function () {
        smigolog("refresh");
        $http.get('species').success(function (response) {
            $scope.species = response;
            smigolog($scope.species);
        });
    };

    $scope.selectSpecies($scope.species["1"]);
});
