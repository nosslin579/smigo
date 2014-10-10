function SpeciesService($timeout, $http, $rootScope, translateFilter, $log) {
    var state = {},
        search = {previous: [], promise: ""};
    state.selectedSpecies = new Species('not set');
    state.action = 'add';
    state.speciesArray = initData.species;
    state.selectedSpecies = initData.species[0];
    $log.log('SpeciesService', state);

    $rootScope.$on('current-user-changed', function (event, user) {
        if (user) {
            reloadSpecies();
        }
    });

    $rootScope.$on('locale-changed', function (event, locale) {
        reloadSpecies();
    });


    function Species(vernacularName) {
        this.vernacularName = vernacularName;
    }

    function reloadSpecies() {
        return $http.get('rest/species')
            .then(function (response) {
                $log.log('Species retrieve successful. Response:', response);
                state.speciesArray = response.data;
                state.selectedSpecies = state.speciesArray[0];
            });
    }

    function getSpecies(id) {
        for (var i = 0; i < state.speciesArray.length; i++) {
            if (state.speciesArray[i].id === id) {
                return state.speciesArray[i];
            }
        }
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
                }).catch(function (error) {
                    $log.warn('Could not add species', species, error);
                    state.speciesArray.pop(species);
                    state.selectedSpecies = state.speciesArray[0];
                });
        },
        searchSpecies: function (query) {
            //Cancel search if new search within 2sec
            $timeout.cancel(search.promise);
            if (!query || query.length <= 2 || search.previous.indexOf(query) != -1) {
                return;
            }
            search.promise = $timeout(function () {
                $log.debug('Search with query', query);
                search.previous.push(query);
                $http.post('rest/species/search', {query: query})
                    .then(function (response) {
                        response.data.forEach(function (species) {
                            if (!getSpecies(species.id)) {
                                state.speciesArray.push(species);
                            }
                        });
                        $log.debug('Response from search ', response);
                    });
            }, 2000);
        },
        getSpecies: function () {
            return state.speciesArray;
        }
    }
}
angular.module('smigoModule').factory('SpeciesService', SpeciesService);