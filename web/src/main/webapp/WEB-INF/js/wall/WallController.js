function WallController($scope, $http, $log, $routeParams, PlantService, GridService, WallService) {

    WallService.setUser($routeParams.username);

    $scope.plantsState = WallService.getPlantsState();
    $scope.hostUser = WallService.getUserState();

    window.s = $scope;


    $scope.selectYear = WallService.selectYear;
    $scope.getGridSizeCss = GridService.getGridSizeCss;
    $scope.getSquarePositionCss = GridService.getSquarePositionCss;
}

angular.module('smigoModule').controller('WallController', WallController);