function MainMenuController($http, $scope, $rootScope, PlantService, $route) {
    $scope.logout = function () {
        $http({
            method: 'GET',
            url: 'logout'
        }).success(function (data, status, headers, config) {
            delete $rootScope.currentUser.authenticated;
            PlantService.reloadGarden().then(function () {
                $route.reload();
            });
        }).error(function (data, status, headers, config) {
            delete $rootScope.currentUser.authenticated;
        });

    };
}

angular.module('smigoModule').controller('MainMenuController', MainMenuController);