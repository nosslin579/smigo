function SpeciesService($timeout, $http, $rootScope, translateFilter, $log) {
    var state = {},
        search = {previous: [], promise: ""},
        ruleMap = {};

    state.action = 'add';
    state.speciesArray = initData.species;
    state.selectedSpecies = initData.species.find(28, 'id');
    state.pendingAdd = false;
    state.varieties = initData.varieties;
    $log.log('SpeciesService', state);

    initData.rules.forEach(function (rule) {
        !ruleMap[rule.host] && (ruleMap[rule.host] = []);
        ruleMap[rule.host].push(createRule(rule));
    });

    augmentSpecies(state.speciesArray);

    $rootScope.$on('messages-reloaded', function (event, msg) {
        augmentSpecies(state.speciesArray);
    });

    function Variety(name, speciesId, userId) {
        this.name = name;
        this.speciesId = speciesId;
        this.userId = userId;
    }

    function augmentSpecies(speciesArray) {
        $log.info('Augmenting species', [speciesArray]);
        speciesArray.forEach(function (species) {
            species.vernacularName = translateFilter(species);
            species.rules = ruleMap[species.id];
            !species.rules && (species.rules = []);
        });
    }

    function createRule(rule) {

        function Rule(rule, category, yearsBackMin, yearsBackMax, hasCauser, arg) {
            this.id = rule.id;
            this.host = rule.host;
            this.type = rule.type;
            this.impacts = rule.impacts;
            this.arg = arg;
            this.category = category;
            this.hint = {messageKey: 'hint.' + category, messageParameter: arg, category: category}
            this.yearsBack = {min: yearsBackMin, max: yearsBackMax};
            this.hasCauser = hasCauser;
            this.messageKey = 'rule.' + category;
        }

        var hasCauser = {
                companion: function (square) {
                    return square.containsSpecies(rule.param);
                },
                rotation: function (square) {
                    return square.containsFamily(rule.param);
                },
                repetition: function (square) {
                    return square.containsSpecies(rule.host);
                }
            },
            speciesArg = new Species(rule.param),
            speciesHost = new Species(rule.host),
            familyArg = new Message("family" + rule.param);

        switch (rule.type) {
            case 0:
                return new Rule(rule, 'goodcompanion', 0, 0, hasCauser.companion, speciesArg);
            case 4:
                return new Rule(rule, 'badcompanion', 0, 0, hasCauser.companion, speciesArg);
            case 5:
                return new Rule(rule, 'goodcroprotation', 1, 1, hasCauser.rotation, [speciesHost, familyArg]);
            case 6:
                return new Rule(rule, 'badcroprotation', 1, 1, hasCauser.rotation, [speciesHost, familyArg]);
            case 7:
                return new Rule(rule, 'speciesrepetition', 1, rule.param, hasCauser.repetition, rule.param);
            default :
                throw "No such rule type " + rule.type;
        }
    }

    function Message(messageKey) {
        this.messageKey = messageKey;
    }

    function Species(id, vernacularName) {
        this.id = id;
        this.vernacularName = vernacularName;
        this.iconFileName = 'defaulticon.png';
        this.messageKey = 'msg.species' + id;
        this.rules = [];
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
        selectSpecies: function (species, event) {
            event && event.preventDefault();
            state.selectedSpecies = species;
            state.action = 'add';
            $log.log('Species selected:', [state, event]);
        },
        addSpecies: function (vernacularName, user) {
            state.pendingAdd = true;
            var name = vernacularName.capitalize();
            var species = new Species(null, name);
            $log.log('Adding species:' + vernacularName, state);
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
                    $rootScope.$broadcast('new-messages-available', species.messageKey, species.vernacularName);
                }).catch(function (error) {
                    $log.warn('Could not add species', species, error);
                    state.speciesArray.pop(species);
                    state.selectedSpecies = state.speciesArray.find(28, 'id');
                    state.pendingAdd = false;
                });
        },
        searchSpecies: function (sq) {
            var queryLowerCase = sq.query.toLocaleLowerCase();
            //Cancel search if new search within 2sec
            $timeout.cancel(search.promise);
            sq.proccessing = false;
            if (!sq.query || sq.query.length <= 2 || search.previous.indexOf(queryLowerCase) != -1) {
                return;
            }
            sq.proccessing = true;
            search.promise = $timeout(function () {
                $log.debug('Search with query', sq);
                $http.post('/rest/species/search', {query: sq.query})
                    .then(function (response) {
                        search.previous.push(queryLowerCase);
                        response.data.forEach(function (species) {
                            if (!state.speciesArray.find(species.id, 'id')) {
                                state.speciesArray.push(species);
                                augmentSpecies([species]);
                            }
                        });
                        $log.debug('Response from search ' + queryLowerCase, response);
                        sq.proccessing = false;
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
            var ret = new Species(id, 'id' + id);
            state.speciesArray.push(ret);
            $http.get('/rest/species/' + id)
                .then(function (response) {
                    angular.extend(ret, response.data);
                    augmentSpecies([ret]);
                });
            return ret;
        },
        addVariety: function (name, speciesId, userId) {
            var variety = new Variety(name, speciesId, userId);
            $http.post('/rest/variety/', variety)
                .then(function (response) {
                    variety.id = response.data;
                    state.varieties.push(variety);
                });
        },
        getAllVarieties: function () {
            return state.varieties;
        },
        getRule: function (id) {
            for (var i = 0; i < state.speciesArray.length; i++) {
                var species = state.speciesArray[i];
                for (var j = 0; j < species.rules.length; j++) {
                    if (species.rules[j].id === id) {
                        return species.rules[j];
                    }
                }
            }
        }
    }
}
angular.module('smigoModule').factory('SpeciesService', SpeciesService);