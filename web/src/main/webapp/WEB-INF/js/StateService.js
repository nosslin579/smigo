function StateService($http, $window, $timeout, $rootScope, $q, $log, PlantService) {

    var garden = PlantService.createGarden(initData.plantDataArray);

    $window.addEventListener("beforeunload", function (event) {
        garden.save();
    });

    $rootScope.$on('user-logout', function () {
        garden.save();
    });

    $rootScope.$on('current-user-changed', function (event, user) {
        if (user) {
            $http.get('/rest/plant/' + user.username)
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
        }
    }

}
angular.module('smigoModule').factory('StateService', StateService);