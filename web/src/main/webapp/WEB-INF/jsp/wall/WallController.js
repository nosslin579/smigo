function WallController($scope, $http, $log, $routeParams, PlantService, GridService, WallService) {

    WallService.setUser($routeParams.username);

    var state = {};
    $scope.plantsState = {};
    $scope.userState = WallService.getState();


    $scope.getGridSizeCss = GridService.getGridSizeCss;
    $scope.getSquarePositionCss = GridService.getSquarePositionCss;

    PlantService.getGarden($routeParams.username)
        .then(function (garden) {
            state.garden = garden;
            $scope.plantsState.selectedYear = garden.getAvailableYears().last();
            $scope.plantsState.squares = garden.getSquares($scope.plantsState.selectedYear);
        });
}

angular.module('smigoModule').controller('WallController', WallController);