function GardenController($http, $log, $uibModal, $scope, $filter, $location, $anchorScroll, $timeout, GardenService, SpeciesService, UserService, VernacularService) {

    $scope.search = {query: '', proccessing: false};
    $scope.pressEnterToSelectTooltipEnable = true;
    $scope.clickAgainToOpenTooltipEnable = true;

    $scope.garden = GardenService.getGarden('', true);

    $scope.speciesState = SpeciesService.getState();
    $scope.userState = UserService.getState();

    $scope.addSpecies = SpeciesService.addSpecies;
    $scope.selectSpecies = SpeciesService.selectSpecies;
    $scope.searchSpecies = SpeciesService.searchSpecies;
    $scope.getVernacular = VernacularService.getVernacular;

    $scope.selectedSpeciesFromTopResult = function (search, event) {
        $log.log('Setting species from', [search, event]);
        $scope.pressEnterToSelectTooltipEnable = false;
        var topResult = $filter('speciesFilter')(SpeciesService.getAllSpecies(), search.query)[0];
        if (topResult) {
            SpeciesService.selectSpecies(topResult);
        }
        search.query = '';

        //make sure selected species is visible in species scroll list
        $timeout(function () {
            var speciesArrayAlphabetically = $filter('speciesFilter')(SpeciesService.getAllSpecies(), '');
            var indexOfSelectedSpecies = speciesArrayAlphabetically.indexOf(topResult);
            var speciesAboveSelected = speciesArrayAlphabetically[(indexOfSelectedSpecies - 4)];
            $log.log('Scrolling to(or not if undefined):', speciesAboveSelected);
            speciesAboveSelected && $anchorScroll('select-species-' + speciesAboveSelected.id);
            event.currentTarget[0].blur();
        });
    };
    $scope.getTopResult = function (query) {
        var topResult = $filter('speciesFilter')(SpeciesService.getAllSpecies(), query)[0];
        return topResult ? VernacularService.getVernacular(topResult.id).vernacularName : '-';
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
            }).result.then(function (x) {
                    $log.log('Modal close', x);
                    $anchorScroll('select-species-' + SpeciesService.getState().selectedSpecies.id);
                });
        }
    };
}

angular.module('smigoModule').controller('GardenController', GardenController);