function SpeciesController($routeParams, $http, $log, $modal, $scope, $filter, $timeout, StateService, SpeciesService, UserService, GridService) {
    $scope.species = SpeciesService.getSpecies(+$routeParams.id);
}

angular.module('smigoModule').controller('SpeciesController', SpeciesController);