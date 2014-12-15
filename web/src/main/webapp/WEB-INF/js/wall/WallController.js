function WallController($scope, $http, $log, $routeParams, GardenService, WallService) {

    $http.get('/rest/user/' + $routeParams.username)
        .then(function (response) {
            $scope.hostUser = response.data;
        });

    $http.get('/rest/plant/' + $routeParams.username)
        .then(function (response) {
            $scope.garden = GardenService.createGarden(response.data, false);
        });

}

angular.module('smigoModule').controller('WallController', WallController);