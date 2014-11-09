function WallController($scope, $http, $log, $routeParams, GardenService, GridService, WallService) {

    $http.get('/rest/user/' + $routeParams.username)
        .then(function (response) {
            $scope.hostUser = response.data;
        });

    $http.get('/rest/plant/' + $routeParams.username)
        .then(function (response) {
            $scope.garden = GardenService.createGarden(response.data);
        });

    $scope.getGridSizeCss = GridService.getGridSizeCss;
    $scope.getSquarePositionCss = GridService.getSquarePositionCss;
}

angular.module('smigoModule').controller('WallController', WallController);