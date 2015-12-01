function RuleController($routeParams, $http, $log, $scope, $filter, $timeout, SpeciesService, UserService) {
    $scope.rule = SpeciesService.getRule(+$routeParams.id);
    $scope.host = SpeciesService.getSpecies($scope.rule.host);
    $scope.requestFeature = UserService.requestFeature;
}

angular.module('smigoModule').controller('RuleController', RuleController);