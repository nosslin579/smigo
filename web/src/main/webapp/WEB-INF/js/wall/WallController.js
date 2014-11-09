function WallController($scope, $http, $log, $routeParams, PlantService, GridService, WallService) {

    $http.get('/rest/user/' + $routeParams.username)
        .then(function (response) {
            $scope.hostUser = response.data;
        });

    $scope.garden = PlantService.getGarden($routeParams.username);

    $scope.getGridSizeCss = GridService.getGridSizeCss;
    $scope.getSquarePositionCss = GridService.getSquarePositionCss;
}

angular.module('smigoModule').controller('WallController', WallController);