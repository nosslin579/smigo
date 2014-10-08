function SpeciesService($http, $rootScope, translateFilter, $log) {
    var state = {
        speciesArray: [],
        selectedSpecies: new Species('not set'),
        action: 'add'
    };
    state.speciesArray = initData.species;
    state.selectedSpecies = initData.species[0];
    $log.log('SpeciesService', state);

    $rootScope.$on('current-user-changed', function (event, user) {
        if (user) {
            reloadSpecies();
        }
    });

    function Species(vernacularName) {
        this.vernacularName = vernacularName;
    }

    function reloadSpecies() {
        return $http.get('rest/species')
            .then(function (response) {
                $log.log('Species retrieve successful. Response:', response);
                state.speciesArray = response.data;
                state.selectedSpecies = state.species[0];
            });
    }

    return {
        getState: function () {
            return state;
        },
        selectSpecies: function (species) {
            state.selectedSpecies = species;
            state.action = 'add';
            $log.log('Species selected:', state);
        },
        addSpecies: function (vernacularName) {
            var name = vernacularName.capitalize();
            var species = new Species(name);
            state.selectedSpecies = species;
            state.speciesArray.push(species);
            $log.log('Species added:' + vernacularName, state);
            return $http.post('rest/species', species)
                .then(function (response) {
                    $log.log('Response from post species', response);
                    species.id = response.data;
                }).then(function () {
                    return $http.get('rest/species/' + species.id);
                }).then(function (response) {
                    angular.extend(species, response.data);
                    $rootScope.$broadcast('newMessagesAvailable', species.messageKey, vernacularName);
                });
        },
        getSpecies: function () {
            return state.speciesArray;
        }
    }
}
angular.module('smigoModule').factory('SpeciesService', SpeciesService);