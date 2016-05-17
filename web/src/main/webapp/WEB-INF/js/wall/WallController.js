function WallController($scope, $http, $log, $routeParams, GardenService) {
    'use strict';
    $http.get('/rest/user/' + $routeParams.username)
        .then(function (response) {
            $scope.hostUser = response.data;
        });

    $scope.garden = GardenService.getGarden($routeParams.username, true);
}

angular.module('smigoModule').controller('WallController', WallController);