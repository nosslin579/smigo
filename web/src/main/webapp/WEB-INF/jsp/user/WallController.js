function WallController($scope, $http, $log, $routeParams, PlantService, GridService) {

    var wallState = {};
    $scope.plantsState = {};

    $scope.getGridSizeCss = GridService.getGridSizeCss;
    $scope.getSquarePositionCss = GridService.getSquarePositionCss;

    $http.get('rest/plant/' + $routeParams.userId)
        .then(function (response) {
            $log.log('Garden retrieve successful. Response:', response);
            wallState.squares = response.data;
            $scope.plantsState.availableYears = Object.keys(wallState.squares).sort();
            $scope.plantsState.selectedYear = Object.keys(wallState.squares).sort().last();
            $scope.plantsState.squares = wallState.squares[$scope.plantsState.selectedYear];
        });
}

angular.module('smigoModule').controller('WallController', WallController);