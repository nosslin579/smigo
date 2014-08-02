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
        console.log("addplant", [clickEvent, this]);
        /*
         var parentOffset = $(this).parent().offset();
         $scope.squares.push({
         x: 5,
         y: 5,
         year: $scope.currentYear,
         plants: [
         {
         speciesId: $scope.currentSpecies.id,
         species: $scope.currentSpecies,
         x: 5,
         y: 5,
         year: $scope.currentYear
         }
         ]
         });
         */
    };

    $scope.gridSize = function (squares) {
        var xmax = -9999, ymax = -99999, xmin = 9999, ymin = 9999;
        angular.forEach(squares, function (square, index) {
            xmax = Math.max(square.x, xmax);
            ymax = Math.max(square.y, ymax);
            xmin = Math.min(square.x, xmin);
            ymin = Math.min(square.y, ymin);
        });
        return {
            'margin-top': (-10000 + 3 + Math.abs(ymin) * 3) + 'em',
            'width': (10000 + 6 + Math.abs(xmax) * 3) + 'em',
            'height': (10000 + 6 + Math.abs(ymax) * 3) + 'em',
            'margin-left': (-10000 + 3 + Math.abs(xmin) * 3) + 'em'
        };
    };

    $scope.squarePostion = function (square) {
        smigolog('squarePostion', square);
        return {
            top: square.y * 3 + 10000 + 'em',
            left: square.x * 3 + 10000 + 'em'
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
