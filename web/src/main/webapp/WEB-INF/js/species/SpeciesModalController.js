function SpeciesModalController($log, $scope, UserService, SpeciesService, VernacularService, VarietyService) {
    'use strict';
    $scope.species = SpeciesService.getState().selectedSpecies;
    $scope.currentUser = UserService.getState().currentUser;
    $scope.varieties = VarietyService.getAllVarieties();
    $scope.updateVernacularName = VernacularService.updateVernacular;
    $scope.addVernacularName = VernacularService.addVernacular;
    $scope.deleteVernacular = VernacularService.deleteVernacular;
    $scope.getVernacular = VernacularService.getVernacular;
    $scope.getVernaculars = VernacularService.getVernaculars;
    $scope.updateSpecies = SpeciesService.updateSpecies;
    $scope.addVariety = VarietyService.addVariety;
    $scope.selectSpecies = SpeciesService.selectSpecies;
    $scope.deleteSpecies = SpeciesService.deleteSpecies;


    $scope.toggleVariety = function (variety, species, event) {
        $log.log('Toggle variety:', [variety, species, event]);
        species.variety = species.variety == variety ? null : variety;
        event.currentTarget.blur();
    };
}

angular.module('smigoModule').controller('SpeciesModalController', SpeciesModalController);