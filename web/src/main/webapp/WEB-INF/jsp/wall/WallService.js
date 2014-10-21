function WallService($http, $log, PlantService) {

    var state = {user: {}, plants: {}},
        garden;

    function updatePlantsState(newGarden) {
        garden = newGarden;
        state.plants.availableYears = newGarden.getAvailableYears().reverse();
        state.plants.selectedYear = newGarden.getAvailableYears().last();
        state.plants.squares = newGarden.getSquares(state.plants.selectedYear);
    }

    return {
        setUser: function (username) {
            $http.get('/rest/user/' + username)
                .then(function (response) {
                    angular.extend(state.user, response.data);
                });
            PlantService.getGarden(username)
                .then(function (garden) {
                    updatePlantsState(garden);
                });
        },
        selectYear: function (year) {
            state.plants.selectedYear = year;
            state.plants.squares = garden.getSquares(year);
            $log.debug('Year selected:' + year, state);
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