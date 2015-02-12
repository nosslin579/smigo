function StateService($http, $window, $timeout, $rootScope, $q, $log, GardenService) {

    var garden = GardenService.createGarden(initData.plantDataArray, true),
        user = {
            currentUser: initData.user,
        },
        pingCounter = 0;

    $timeout(pingServer, 10000, false);

    $rootScope.$on('current-user-changed', function (event, newUser) {
        if (newUser) {
            $http.get('/rest/plant/' + newUser.username)
                .then(function (response) {
                    garden.setPlants(response.data);
                });
            user.currentUser = newUser;
        } else {
            garden.setPlants([]);
            user.currentUser = null;
        }
    });

    function pingServer() {
        $http.get('ping')
            .then(function (response) {
                $log.debug("Ping success nr:" + pingCounter, response);
                var username = user.currentUser ? user.currentUser.username : undefined;
                if (username !== response.data.name) {
                    $log.error('Username mismatch detected', [user, response, username]);
                }
            })
            .catch(function (response) {
                $log.warn("Ping fail", response);
            });
        if (pingCounter++ < 100) {
            $timeout(pingServer, 960000);
        }
    }

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