function SpeciesModalController($routeParams, $http, $log, $scope, $filter, $timeout, $uibModalInstance, SpeciesService, UserService) {
    $scope.species = SpeciesService.getState().selectedSpecies;
    $scope.selectSpecies = function (speciesId) {
        $log.info('Species from modal selected:' + speciesId);
        SpeciesService.selectSpecies(SpeciesService.getSpecies(speciesId));
        $uibModalInstance.dismiss('selected species');
    };

    $scope.close = function () {
        $uibModalInstance.dismiss('cancel');
    };
}

angular.module('smigoModule').controller('SpeciesModalController', SpeciesModalController);