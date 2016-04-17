function GardenController($http, $log, $uibModal, $scope, $filter, $location, $anchorScroll, $timeout, GardenService, SpeciesService, UserService) {

    $scope.search = {query: '', proccessing: false};
    $scope.pressEnterToSelectTooltipEnable = true;
    $scope.clickAgainToOpenTooltipEnable = true;

    $scope.garden = GardenService.getGarden('', true);

    $scope.speciesState = SpeciesService.getState();
    $scope.userState = UserService.getState();

    $scope.addSpecies = SpeciesService.addSpecies;
    $scope.selectSpecies = SpeciesService.selectSpecies;
    $scope.searchSpecies = SpeciesService.searchSpecies;

    $scope.selectedSpeciesFromTopResult = function (query, event) {
        $log.log('Setting species from', [query, event]);
        $scope.pressEnterToSelectTooltipEnable = false;
        var topResult = $filter('speciesFilter')(SpeciesService.getAllSpecies(), query)[0];
        if (topResult) {
            SpeciesService.selectSpecies(topResult);
        }
        $scope.search.query = '';

        //make sure selected species is visible in species scroll list
        $timeout(function () {
            var speciesArrayAlphabetically = $filter('orderBy')(SpeciesService.getAllSpecies(), 'vernacularName');
            var indexOfSelectedSpecies = speciesArrayAlphabetically.indexOf(topResult);
            var speciesAboveSelected = speciesArrayAlphabetically[(indexOfSelectedSpecies - 4)];
            $log.log('Scrolling to(or not if undefined):', speciesAboveSelected);
            speciesAboveSelected && $anchorScroll('select-species-' + speciesAboveSelected.id);
            event.currentTarget[0].blur();
        });
    };
    $scope.getTopResult = function (query) {
        var topResult = $filter('speciesFilter')(SpeciesService.getAllSpecies(), query)[0];
        return topResult ? topResult : '-';
    };
    $scope.openAddYearModal = function () {
        $uibModal.open({
            templateUrl: 'views/add-year-modal.html',
            controller: AddYearModalController,
            size: 'sm',
            resolve: {
                garden: function () {
                    return $scope.garden;
                }
            }
        }).result.then(function (year) {
                $log.log('Add year modal close ', year);
                $scope.garden.addYear(year);
            });
    };
    $scope.openSpeciesModal = function (species) {
        if (SpeciesService.getState().selectedSpecies == species) {
            $scope.clickAgainToOpenTooltipEnable = false;
            $uibModal.open({
                templateUrl: 'views/species-modal.html',
                controller: SpeciesModalController
            });
        }
    };
}

angular.module('smigoModule').controller('GardenController', GardenController);