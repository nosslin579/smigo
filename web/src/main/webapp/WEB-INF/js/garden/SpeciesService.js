function SpeciesService($timeout, $http, $rootScope, translateFilter, $log) {
    var state = {},
        search = {previous: [], promise: ""};
    state.selectedSpecies = new Species('not set');
    state.action = 'add';
    state.speciesArray = initData.species;
    state.selectedSpecies = initData.species[0];
    state.pendingAdd = false;
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
        return $http.get('/rest/species')
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
            state.pendingAdd = true;
            var name = vernacularName.capitalize();
            var species = new Species(name);
            $log.log('Species added:' + vernacularName, state);
            return $http.post('/rest/species', species)
                .then(function (response) {
                    $log.log('Response from post species', response);
                    species.id = response.data;
                    state.speciesArray.push(species);
                    state.selectedSpecies = species;
                    state.pendingAdd = false;
                }).then(function () {
                    return $http.get('/rest/species/' + species.id);
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
                $http.post('/rest/species/search', {query: query})
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
        getAllSpecies: function () {
            return state.speciesArray;
        },
        getSpecies: function (id) {
            if (!id) {
                throw "Can not get species with no id";
            }
            var species = state.speciesArray.find(id, 'id');
            if (species) {
                return species;
            }
            var ret = {id: id, iconFileName: 'defaulticon.png'};
            state.speciesArray.push(ret);
            $http.get('/rest/species/' + id)
                .then(function (response) {
                    angular.extend(ret, response.data);
                });
            return ret;
        },
        isSpeciesAddable: function (vernacularName) {
            if (!vernacularName || search.previous.indexOf(vernacularName.toLocaleLowerCase()) == -1 || state.pendingAdd) {
//                $log.debug('Species:' + vernacularName + ' not addable', search);
                return false;
            }
            var species = state.speciesArray.find(vernacularName, 'vernacularName', {ignoreCase: true});
            return species ? false : true;
        },
        loadSpeciesFromUser: function (userId) {
            $http.get('/rest/species/' + userId)
                .then(function (response) {

                });
        }
    }
}
angular.module('smigoModule').factory('SpeciesService', SpeciesService);