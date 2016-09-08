function WallController($scope, $http, $log, $routeParams, GardenService) {
    'use strict';
    $http.get('/rest/user/' + $routeParams.username).then(function (response) {
        $scope.hostUser = response.data;
    });

    $http.get('/rest/comment', {params: {receiver: $routeParams.username}}).then(function (response) {
        $scope.comments = response.data;
    });

    $scope.garden = GardenService.getGarden($routeParams.username, true);
}

angular.module('smigoModule').controller('WallController', WallController);