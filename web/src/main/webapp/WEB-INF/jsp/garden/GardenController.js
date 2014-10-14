function GardenController($http, $log, $modal, $scope, $filter, PlantService, SpeciesService, UserService, GridService) {

    $scope.plantsState = PlantService.getState();
    $scope.speciesState = SpeciesService.getState();
    $scope.userState = UserService.getState();

    $scope.selectYear = PlantService.selectYear;
    $scope.addSpecies = SpeciesService.addSpecies;
    $scope.selectSpecies = SpeciesService.selectSpecies;
    $scope.searchSpecies = SpeciesService.searchSpecies;
    $scope.isSpeciesAddable = SpeciesService.isSpeciesAddable;
    $scope.getGridSizeCss = GridService.getGridSizeCss;
    $scope.getSquarePositionCss = GridService.getSquarePositionCss;

    $scope.selectedSpeciesFromTopResult = function (searchString) {
        $log.log('Setting species from', searchString);
        var topResult = $filter('speciesFilter')(SpeciesService.getSpecies(), searchString)[0];
        if (topResult) {
            SpeciesService.selectSpecies(topResult);
        }
        $scope.speciesQuery = '';
    };
    $scope.onSquareClick = function (clickEvent, square) {
        $log.log('Square clicked', [clickEvent, square, SpeciesService.getState().selectedSpecies, SpeciesService.getState().action]);
        if (clickEvent.shiftKey || SpeciesService.getState().action == 'delete') {
            PlantService.removePlant(square);
        } else if (SpeciesService.getState().action == 'info') {
            square.showTooltip = !square.showTooltip;
        } else if (clickEvent.ctrlKey) {
            $log.log('Copy species');
        } else {
            PlantService.addPlant(SpeciesService.getState().selectedSpecies, square);
        }
        clickEvent.stopPropagation();
    };
    $scope.onGridClick = function (clickEvent) {
        $log.log('Grid clicked', [clickEvent, SpeciesService.getState().selectedSpecies, SpeciesService.getState().action]);
        if (SpeciesService.getState().action == 'add') {
            //http://stackoverflow.com/a/14872192/859514
            var offsetX = clickEvent.clientX - $(clickEvent.target).offset().left;
            var offsetY = clickEvent.clientY - $(clickEvent.target).offset().top;
            var x = Math.floor((offsetX - 100000) / 48);
            var y = Math.floor((offsetY - 100000) / 48);
            PlantService.addSquare(PlantService.getState().selectedYear, x, y, SpeciesService.getState().selectedSpecies);
        }
        clickEvent.stopPropagation();
    };
    $scope.openAddYearModal = function () {
        $modal.open({
            templateUrl: 'add-year-modal.html',
            controller: AddYearModalController,
            size: 'sm'
        });
    };
}

angular.module('smigoModule').controller('GardenController', GardenController);