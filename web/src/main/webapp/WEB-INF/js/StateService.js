function StateService($http, $window, $timeout, $rootScope, $q, $log, GardenService) {

    var garden = GardenService.createGarden(initData.plantDataArray, true);

    $rootScope.$on('current-user-changed', function (event, newUser) {
        if (newUser) {
            $http.get('/rest/plant/' + newUser.username)
                .then(function (response) {
                    garden.setPlants(response.data);
                });
        } else {
            garden.setPlants([]);
        }
    });

    return {
        getGarden: function () {
            return garden;
        },
        getUser: function () {
            throw 'Not used anymore';
        }
    }

}
angular.module('smigoModule').factory('StateService', StateService);