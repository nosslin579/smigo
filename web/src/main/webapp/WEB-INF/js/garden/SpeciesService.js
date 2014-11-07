function SpeciesService($timeout, $http, $rootScope, translateFilter, $log) {
    var state = {},
        search = {previous: [], promise: ""},
        ruleMap = {};

    state.action = 'add';
    state.speciesArray = initData.species;
    state.selectedSpecies = initData.species.find(28, 'id');
    state.pendingAdd = false;
    $log.log('SpeciesService', state);

    initData.rules.forEach(function (rule) {
        !ruleMap[rule.host] && (ruleMap[rule.host] = []);
        ruleMap[rule.host].push(createRule(rule));
    });

    augmentSpecies(state.speciesArray);

    $rootScope.$on('messages-reloaded', function (event, msg) {
        augmentSpecies(state.speciesArray);
    });


    function augmentSpecies(speciesArray) {
        $log.info('Augmenting species', speciesArray);
        speciesArray.forEach(function (species) {
            species.vernacularName = translateFilter(species);
            species.rules = ruleMap[species.id];
            !species.rules && (species.rules = []);
        });
    }

    function createRule(rule) {

        function Rule(rule, category, hintMessageKey, yearsBackMin, yearsBackMax, hasCauser, arg) {
            this.id = rule.id;
            this.host = rule.host;
            this.type = rule.type;
            this.category = category;
            this.hint = {messageKey: hintMessageKey, messageParameter: arg}
            this.yearsBack = {min: yearsBackMin, max: yearsBackMax};
            this.hasCauser = hasCauser;
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
            },
            speciesArg = new Message("msg.species" + rule.param),
            speciesHost = new Message("msg.species" + rule.host),
            familyArg = new Message("family" + rule.param);

        switch (rule.type) {
            case 0:
                return new Rule(rule, 'goodcompanion', 'hint.goodcompanion', 0, 0, hasCauser.companion, speciesArg);
            case 1:
                return new Rule(rule, 'goodcompanion', 'hint.fightdisease', 0, 0, hasCauser.companion, speciesArg);
            case 2:
                return new Rule(rule, 'goodcompanion', "hint.repelpest", 0, 0, hasCauser.companion, speciesArg);
            case 3:
                return new Rule(rule, 'goodcompanion', "hint.improvesflavor", 0, 0, hasCauser.companion, speciesArg);
            case 4:
                return new Rule(rule, 'badcompanion', "hint.badcompanion", 0, 0, hasCauser.companion, speciesArg);
            case 5:
                return new Rule(rule, 'goodcroprotation', "hint.goodcroprotation", 1, 1, hasCauser.rotation, [speciesHost, familyArg]);
            case 6:
                return new Rule(rule, 'badcroprotation', "hint.badcroprotation", 1, 1, hasCauser.rotation, [speciesHost, familyArg]);
            case 7:
                return new Rule(rule, 'speciesrepetition', "hint.speciesrepetition", 1, rule.param, hasCauser.repetition, rule.param);
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
                state.selectedSpecies = state.speciesArray.find(28, 'id');
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
                    state.selectedSpecies = state.speciesArray.find(28, 'id');
                    state.pendingAdd = false;
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
                        augmentSpecies(response.data);
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
            return state.speciesArray.find(vernacularName, 'vernacularName', {ignoreCase: true}) === null;
        },
        loadSpeciesFromUser: function (userId) {
            $http.get('/rest/species/' + userId)
                .then(function (response) {

                });
        }
    }
}
angular.module('smigoModule').factory('SpeciesService', SpeciesService);