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
            var queryLowerCase = query.toLocaleLowerCase();
            //Cancel search if new search within 2sec
            $timeout.cancel(search.promise);
            if (!query || query.length <= 2 || search.previous.indexOf(queryLowerCase) != -1) {
                return;
            }
            search.promise = $timeout(function () {
                $log.debug('Search with query', query);
                $http.post('rest/species/search', {query: query})
                    .then(function (response) {
                        search.previous.push(queryLowerCase);
                        response.data.forEach(function (species) {
                            if (!state.speciesArray.find(species.id, 'id')) {
                                state.speciesArray.push(species);
                            }
                        });
                        $log.debug('Response from search ' + queryLowerCase, response);
                    });
            }, 2000);
        },
        getSpecies: function () {
            return state.speciesArray;
        },
        isSpeciesAddable: function (vernacularName) {
            if (!vernacularName || search.previous.indexOf(vernacularName.toLocaleLowerCase()) == -1) {
                $log.debug('Species:' + vernacularName + ' not addable', search);
                return false;
            }
            var species = state.speciesArray.find(vernacularName, 'vernacularName', {ignoreCase: true});
            return species ? false : true;
        }
    }
}
angular.module('smigoModule').factory('SpeciesService', SpeciesService);