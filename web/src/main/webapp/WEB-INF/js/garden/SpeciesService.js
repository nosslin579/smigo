function SpeciesService($timeout, $http, $rootScope, translateFilter, $log) {
    var state = {},
        search = {previous: [], promise: ""},
        ruleArray = initData.rules;

    state.action = 'add';
    state.speciesArray = initData.species;
    state.selectedSpecies = initData.species[0];
    state.pendingAdd = false;
    $log.log('SpeciesService', state);

    augmentSpecies(initData.species);

    $rootScope.$on('current-user-changed', function (event, user) {
        if (user) {
            reloadSpecies();
        }
    });


    function augmentSpecies(speciesArray) {
        speciesArray.forEach(function (species) {
            species.vernacularName = translateFilter(species);
        });

        ruleArray.forEach(function (rule) {
            var species = speciesArray.find(rule.host, 'id');
            if (species) {
                species.rules.push(createRule(rule));
            }
        });
    }

    function createRule(rule) {

        function Rule(rule, messageKey, hintMessageKey, yearsBackMin, yearsBackMax, hasCauser, messageParameter) {
            this.id = rule.id;
            this.host = rule.host;
            this.type = rule.type;
            this.messageKey = messageKey;
            this.messageParameter = messageParameter;
            this.yearsBack = {min: yearsBackMin, max: yearsBackMax};
            this.hasCauser = hasCauser;
            this.hint = {messageKey: hintMessageKey, messageParameter: messageParameter}
        }

        var hasCauser = {
            companion: function (square) {
                return square.plants.hasOwnProperty(rule.param);
            },
            rotation: function (square) {
                return square.containsFamily(rule.param);
            },
            repetition: function (square) {
                return square.plants.hasOwnProperty(rule.host);
            }
        }

        switch (rule.type) {
            case 0:
                return new Rule(rule, 'rule.goodcompanion', 'hint.goodcompanion', 0, 0, hasCauser.companion, new Message("msg.species" + rule.param));
            case 1:
                return new Rule(rule, 'rule.fightdisease', 'hint.fightdisease', 0, 0, hasCauser.companion, new Message("msg.species" + rule.param));
            case 2:
                return new Rule(rule, "rule.repelpest", "hint.repelpest", 0, 0, hasCauser.companion, new Message("msg.species" + rule.param));
            case 3:
                return new Rule(rule, "rule.improvesflavor", "hint.improvesflavor", 0, 0, hasCauser.companion, new Message("msg.species" + rule.param));
            case 4:
                return new Rule(rule, "rule.badcompanion", "hint.badcompanion", 0, 0, hasCauser.companion, new Message("msg.species" + rule.param));
            case 5:
                return new Rule(rule, "rule.goodcroprotation", "hint.goodcroprotation", 1, 1, hasCauser.rotation, new Message("family" + rule.param));
            case 6:
                return new Rule(rule, "rule.badcroprotation", "hint.badcroprotation", 1, 1, hasCauser.rotation, new Message("family" + rule.param));
            case 7:
                return new Rule(rule, "rule.speciesrepetition", "hint.speciesrepetition", 1, rule.param, hasCauser.repetition, rule.param);
            default :
                throw "No such rule type " + rule.type;
        }
    }

    function Message(messageKey) {
        this.messageKey = messageKey;
    }

    function Species(vernacularName) {
        this.vernacularName = vernacularName;
    }

    function reloadSpecies() {
        return $http.get('/rest/species')
            .then(function (response) {
                $log.log('Species retrieve successful. Response:', response);
                state.speciesArray = response.data;
                state.selectedSpecies = response.data[0];
                augmentSpecies(response.data);
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
                    augmentSpecies([ret]);
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