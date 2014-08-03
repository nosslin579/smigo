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
        if (messageObject.messageKey) {
            return msg[messageObject.messageKey];
        }
        return msg[messageObject];
    };
});

app.controller('GardenController', function ($scope, $http) {
    $scope.selectAction = function (species) {
        console.log('CurrentSquareAction set to add species', species);
        $scope.currentSquareAction = function (square) {
            console.log('Adding species', [square, species]);
            square.plants.push({
                    species: species,
                    location: square.location
                }
            );
        };
    };

    $scope.selectYear = function (year) {
        smigolog('year set to', year);
        $scope.currentYear = year;
        $scope.squares = $scope.garden.squares[year];
    };

    $scope.onSquareClick = function (clickEvent, square) {
        console.log('onSquareClick', [clickEvent, square]);
        $scope.currentSquareAction(square);
        clickEvent.stopPropagation();
    };

    $scope.addSquare = function (clickEvent) {
        var newSquare = {
            location: {
                x: Math.floor((clickEvent.offsetX - 100000) / 48),
                y: Math.floor((clickEvent.offsetY - 100000) / 48),
                year: this.currentYear
            },
            plants: []
        };
        $scope.squares.push(newSquare);
        console.log('square added', [newSquare, clickEvent, this]);
        this.currentSquareAction(newSquare);
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

    $scope.selectAction($scope.species["1"]);
});
