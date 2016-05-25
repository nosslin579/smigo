function GardenController($log, $uibModal, $scope, speciesQueryFilter, GardenService, SpeciesService, UserService, VernacularService) {
    'use strict';
    $scope.search = {query: '', proccessing: false, pressEnterToSelectTooltipEnable: true};

    $scope.clickAgainToOpenTooltipEnable = true;

    $scope.garden = GardenService.getState().garden;

    $scope.speciesState = SpeciesService.getState();
    $scope.userState = UserService.getState();

    $scope.addSpecies = SpeciesService.addSpecies;
    $scope.selectSpecies = SpeciesService.selectSpecies;
    $scope.searchSpecies = SpeciesService.searchSpecies;
    $scope.getVernacular = VernacularService.getVernacular;

    $scope.getTopResult = function (query) {
        var topResult = speciesQueryFilter(SpeciesService.getAllSpecies(), query)[0];
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
            });
        }
    };
}

angular.module('smigoModule').controller('GardenController', GardenController);