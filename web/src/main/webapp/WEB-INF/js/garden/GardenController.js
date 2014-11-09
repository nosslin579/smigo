function GardenController($http, $log, $modal, $scope, $filter, StateService, SpeciesService, UserService, GridService) {

    $scope.garden = StateService.getGarden();
    $scope.speciesState = SpeciesService.getState();
    $scope.userState = StateService.getUser();

    $scope.addSpecies = SpeciesService.addSpecies;
    $scope.selectSpecies = SpeciesService.selectSpecies;
    $scope.searchSpecies = SpeciesService.searchSpecies;
    $scope.isSpeciesAddable = SpeciesService.isSpeciesAddable;
    $scope.getGridSizeCss = GridService.getGridSizeCss;
    $scope.getSquarePositionCss = GridService.getSquarePositionCss;

    $scope.selectedSpeciesFromTopResult = function (query) {
        $log.log('Setting species from', query);
        var topResult = $filter('speciesFilter')(SpeciesService.getAllSpecies(), query)[0];
        if (topResult) {
            SpeciesService.selectSpecies(topResult);
        }
        $scope.speciesQuery = '';
    };
    $scope.onSquareClick = function (clickEvent, square) {
        $log.log('Square clicked', [clickEvent, square, SpeciesService.getState().selectedSpecies, SpeciesService.getState().action]);
        if (clickEvent.shiftKey || SpeciesService.getState().action == 'delete') {
            square.removePlant();
        } else if (SpeciesService.getState().action == 'info') {
            square.showTooltip = !square.showTooltip;
        } else if (clickEvent.ctrlKey) {
            $log.log('Copy species');
        } else {
            square.addPlant(SpeciesService.getState().selectedSpecies);
        }
        clickEvent.stopPropagation();
    };
    $scope.onGridClick = function (clickEvent, garden) {
        $log.log('Grid clicked', [clickEvent, $scope]);
        if (SpeciesService.getState().action == 'add') {
            //http://stackoverflow.com/a/14872192/859514
            var offsetX = clickEvent.clientX - $(clickEvent.target).offset().left;
            var offsetY = clickEvent.clientY - $(clickEvent.target).offset().top;
            var x = Math.floor((offsetX - 100000) / 48);
            var y = Math.floor((offsetY - 100000) / 48);
            garden.getSquare(garden.selectedYear, x, y).addPlant(SpeciesService.getState().selectedSpecies);
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