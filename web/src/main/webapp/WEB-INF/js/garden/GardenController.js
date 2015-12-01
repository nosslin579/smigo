function GardenController($http, $log, $modal, $scope, $filter, $timeout, StateService, SpeciesService, UserService) {

    $scope.search = {query: '', proccessing: false};
    $scope.clickAgainToOpenTooltipEnable = true;

    $scope.garden = StateService.getGarden();
    $scope.speciesState = SpeciesService.getState();
    $scope.userState = StateService.getUser();

    $scope.addSpecies = SpeciesService.addSpecies;
    $scope.selectSpecies = SpeciesService.selectSpecies;
    $scope.searchSpecies = SpeciesService.searchSpecies;
    $scope.isSpeciesAddable = SpeciesService.isSpeciesAddable;

    $scope.selectedSpeciesFromTopResult = function (query) {
        $log.log('Setting species from', query);
        var topResult = $filter('speciesFilter')(SpeciesService.getAllSpecies(), query)[0];
        if (topResult) {
            SpeciesService.selectSpecies(topResult);
        }
        $scope.search.query = '';
    };
    $scope.openAddYearModal = function () {
        $modal.open({
            templateUrl: 'add-year-modal.html',
            controller: AddYearModalController,
            size: 'sm'
        });
    };
    $scope.openSpeciesModal = function (species) {
        if (SpeciesService.getState().selectedSpecies == species) {
            $scope.clickAgainToOpenTooltipEnable = false;
            $modal.open({
                templateUrl: 'species-modal.html',
                controller: SpeciesModalController
            });
        }
    };
}

angular.module('smigoModule').controller('GardenController', GardenController);