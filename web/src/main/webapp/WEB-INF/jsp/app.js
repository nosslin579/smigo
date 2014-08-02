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
        smigolog('currentSpecies set to', species);
        $scope.currentSpecies = species;
    };

    $scope.selectYear = function (year) {
        smigolog('year set to', year);
        $scope.currentYear = year;
        $scope.squares = $scope.garden.squares[year];
    };

    $scope.addPlant = function (clickEvent) {
        smigolog("addplant", [clickEvent, this]);
        var plantCoord = {
            x: Math.floor((clickEvent.offsetX - 100000) / 48),
            y: Math.floor((clickEvent.offsetY - 100000) / 48)
        };
        console.log(plantCoord);
        $scope.squares.push({
            x: plantCoord.x,
            y: plantCoord.y,
            year: this.currentYear,
            plants: [
                {
                    speciesId: this.currentSpecies.id,
                    species: this.currentSpecies,
                    x: plantCoord.x,
                    y: plantCoord.y,
                    year: this.currentYear
                }
            ]
        });
    };

    $scope.gridSize = function (squares) {
        var xmax = -4, ymax = -2, xmin = 4, ymin = 2;
        angular.forEach(squares, function (square, index) {
            xmax = Math.max(square.x, xmax);
            ymax = Math.max(square.y, ymax);
            xmin = Math.min(square.x, xmin);
            ymin = Math.min(square.y, ymin);
        });
        return {
            'margin-top': (-100000 + 48 + Math.abs(ymin) * 48) + 'px',
            'width': (100000 + 96 + Math.abs(xmax) * 48) + 'px',
            'height': (100000 + 96 + Math.abs(ymax) * 48) + 'px',
            'margin-left': (-100000 + 48 + Math.abs(xmin) * 48) + 'px'
        };
    };

    $scope.squarePostion = function (square) {
        smigolog('squarePostion', square);
        return {
            top: square.y * 48 + 100000 + 'px',
            left: square.x * 48 + 100000 + 'px'
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
