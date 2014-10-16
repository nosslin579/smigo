function WallService($http, $log) {

    var state = {}, users = {};


    return {
        setUser: function (username) {
            return $http.get('rest/user/' + username)
                .then(function (response) {
                    state.user = response.data;
                });
        },
        getState: function () {
            return state;
        }
    }
}
angular.module('smigoModule').service('WallService', WallService);