function GardenController($scope, PlantService, SpeciesService, GardenService, $filter) {

    $scope.$watch('model.selectedYear', function (toYear, fromYear) {
        console.log('Selected year changed', toYear, fromYear);
        $scope.squares = PlantService.getSquares(toYear);
        $scope.visibleRemainderSquares = PlantService.getSquares(toYear - 1);
    });

    $scope.$watch('model.availableYears', function (newYears) {
        if (newYears) {
            $scope.forwardYear = newYears.slice(-1).pop() + 1;
            $scope.backwardYear = newYears[0] - 1;
        }
    });

    $scope.$on('newGardenAvailable', function (event, garden) {
        $scope.species = garden.species;
    });

    $scope.species = SpeciesService.getSpecies();
    $scope.addYear = PlantService.addYear;
    $scope.model = GardenService.getModel();
    $scope.fn = GardenService.fn;
    $scope.onSquareClick = GardenService.onSquareClick;
    $scope.onVisibleRemainderClick = GardenService.onVisibleRemainderClick;
    $scope.onGridClick = GardenService.onGridClick;
    $scope.getGridSizeCss = GardenService.getGridSizeCss;
    $scope.getSquarePositionCss = GardenService.getSquarePositionCss;
    $scope.setSelectedSpecies = GardenService.setSelectedSpecies;

    $scope.setSelectedSpeciesFromTopResult = function (searchString) {
        console.log('Setting species from', searchString);
        var topResult = $filter('speciesFilter')(SpeciesService.getSpecies(), searchString)[0];
        if (topResult) {
            GardenService.fn.setSelectedSpecies(topResult);
        }
        $scope.speciesSearch = '';
    };
}

angular.module('smigoModule').controller('GardenController', GardenController);