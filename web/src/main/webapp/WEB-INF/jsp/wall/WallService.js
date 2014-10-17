function WallService($http, $log, PlantService) {

    var state = {user: {}, plants: {}};

    function updatePlantsState(garden) {
        state.garden = garden;
        state.plants.availableYears = garden.getAvailableYears().reverse();
        state.plants.selectedYear = garden.getAvailableYears().last();
        state.plants.squares = garden.getSquares(state.plants.selectedYear);
    }

    return {
        setUser: function (username) {
            $http.get('rest/user/' + username)
                .then(function (response) {
                    angular.extend(state.user, response.data);
                    $log.debug('state.user', state.user);
                });
            PlantService.getGarden(username)
                .then(function (garden) {
                    updatePlantsState(garden);
                });
        },
        selectYear: function (year) {
            state.plants.selectedYear = year;
            state.plants.squares = state.garden.getSquares(year);
            $log.log('Year selected:' + year, state);
        },
        getUserState: function () {
            return state.user;
        },
        getPlantsState: function () {
            return state.plants;
        }
    }
}
angular.module('smigoModule').service('WallService', WallService);