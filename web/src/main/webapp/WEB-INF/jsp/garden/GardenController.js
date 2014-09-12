function GardenController($scope, PlantService, GardenService) {
    console.log('GardenController', $scope);

    $scope.$watch('model.selectedYear', function (newYear) {
        $scope.squares = PlantService.getGarden().squares[newYear];
        $scope.visibleRemainderSquares = PlantService.getGarden().squares[newYear - 1];
    });

    $scope.species = PlantService.getGarden().species;

    $scope.$watch('model.availableYears', function (newYears) {
        $scope.forwardYear = newYears.slice(-1).pop() + 1;
        $scope.backwardYear = newYears[0] - 1;
    });

    $scope.addYear = PlantService.addYear;
    $scope.model = GardenService.model;
    $scope.onSquareClick = GardenService.onSquareClick;
    $scope.onVisibleRemainderClick = GardenService.onVisibleRemainderClick;
    $scope.onGridClick = GardenService.onGridClick;
    $scope.getGridSizeCss = GardenService.getGridSizeCss;
    $scope.getSquarePositionCss = GardenService.getSquarePositionCss;
}

angular.module('smigoModule').controller('GardenController', GardenController);