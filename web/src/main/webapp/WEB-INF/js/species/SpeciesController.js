function SpeciesController($routeParams, $http, $log, $scope, $filter, $timeout, SpeciesService, UserService) {
    $scope.species = SpeciesService.getSpecies(+$routeParams.id);
    $scope.requestFeature = UserService.requestFeature;
}

angular.module('smigoModule').controller('SpeciesController', SpeciesController);