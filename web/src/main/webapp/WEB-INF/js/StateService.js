function StateService($http, $window, $timeout, $rootScope, $q, $log, GardenService) {

    var garden = GardenService.createGarden(initData.plantDataArray, true),
        user = {
            currentUser: initData.user
        };

    $rootScope.$on('current-user-changed', function (event, newUser) {
        if (newUser) {
            $http.get('/rest/plant/' + newUser.username)
                .then(function (response) {
                    garden.setPlants(response.data);
                });
            user.currentUser = newUser;
            $http.defaults.headers.common.SmigoUser = newUser.username;
        } else {
            garden.setPlants([]);
            user.currentUser = null;
            $http.defaults.headers.common.SmigoUser = null;
        }
    });

    return {
        getGarden: function () {
            return garden;
        },
        getUser: function () {
            return user;
        }
    }

}
angular.module('smigoModule').factory('StateService', StateService);