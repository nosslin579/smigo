function SpeciesService($uibModal, $timeout, $http, $rootScope, translateFilter, $log, VernacularService) {
    var state = {},
        ruleMap = {};

    state.action = 'add';
    state.speciesArray = initData.species;
    state.selectedSpecies = initData.species.smigoFind(28, 'id');
    state.varieties = initData.varieties;
    state.rules = initData.rules;

    $log.log('SpeciesService', state);

    initData.rules.forEach(function (rule) {
        !ruleMap[rule.host] && (ruleMap[rule.host] = []);
        ruleMap[rule.host].push(createRule(rule));
    });

    augmentSpecies(state.speciesArray);

    $rootScope.$on('messages-reloaded', function (event, msg) {
        augmentSpecies(state.speciesArray);
    });

    function Variety(name, speciesId) {
        this.name = name;
        this.speciesId = speciesId;
    }

    function augmentSpecies(speciesArray) {
        $log.info('Augmenting species', [speciesArray]);
        speciesArray.forEach(function (species) {
            species.rules = ruleMap[species.id];
            !species.rules && (species.rules = []);
        });
    }

    function createRule(rule) {

        function Rule(rule, category, yearsBackMin, yearsBackMax, hasCauser, arg, fnGetArgsAsText) {
            this.id = rule.id;
            this.host = rule.host;
            this.type = rule.type;
            this.impacts = rule.impacts;
            this.arg = arg;
            this.category = category;
            this.hint = {
                messageKey: 'hint.' + category,
                messageParameter: arg,
                argument: arg,
                category: category,
                getArgsAsText: fnGetArgsAsText
            };
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
            familyArg = new Message("family" + rule.param),

            speciesGetArgsAsText = function (argument) {
                return VernacularService.getVernacularName(argument.id);
            },
            rotationGetArgsAsText = function (argument) {
                return [VernacularService.getVernacularName(argument[0].id), translateFilter(argument[1].messageKey)];
            },
            rawGetArgsAsText = function (argument) {
                return argument;
            };

        switch (rule.type) {
            case 0:
                return new Rule(rule, 'goodcompanion', 0, 0, hasCauser.companion, speciesArg, speciesGetArgsAsText);
            case 4:
                return new Rule(rule, 'badcompanion', 0, 0, hasCauser.companion, speciesArg, speciesGetArgsAsText);
            case 5:
                return new Rule(rule, 'goodcroprotation', 1, 1, hasCauser.rotation, [speciesHost, familyArg], rotationGetArgsAsText);
            case 6:
                return new Rule(rule, 'badcroprotation', 1, 1, hasCauser.rotation, [speciesHost, familyArg], rotationGetArgsAsText);
            case 7:
                return new Rule(rule, 'speciesrepetition', 1, rule.param, hasCauser.repetition, rule.param, rawGetArgsAsText);
            default :
                throw "No such rule type " + rule.type;
        }
    }

    function Message(messageKey) {
        this.messageKey = messageKey;
    }

    function Species(id) {
        this.id = id;
        this.iconFileName = 'defaulticon.png';
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

    function getSpecies(id) {
        if (!id) {
            throw "Can not get species with no id";
        }
        var species = state.speciesArray.smigoFind(id, 'id');
        if (species) {
            return species;
        }
        var ret = new Species(id);
        state.speciesArray.push(ret);
        $http.get('/rest/species/' + id)
            .then(function (response) {
                angular.extend(ret, response.data);
                augmentSpecies([ret]);
            });
        return ret;
    }

    return {
        getState: function () {
            return state;
        },
        selectSpecies: function (species, event) {
            $log.log('Species select:', [species, state, event]);
            event && event.preventDefault();
            if (state.speciesArray.indexOf(species) != -1) {
                state.selectedSpecies = species;
            } else if (angular.isNumber(species)) {
                state.selectedSpecies = getSpecies(species);
            } else {
                throw 'SpeciesService.selectSpecies got invalid parameter: ' + species;
            }
            state.action = 'add';
        },
        deleteSpecies: function (species) {
            return $http.delete('/rest/species/' + species.id).then(function (response) {
                $log.log('Response from delete species', [response, species]);
                var indexOfDeleted = state.speciesArray.indexOf(species);
                state.speciesArray.splice(indexOfDeleted, 1);
                species.deleted = true;
            }).catch(function (response) {
                alert('Delete failed');
                $log.warn('Response from delete species failed', [response, species]);
            });
        },
        addSpecies: function (search) {
            $log.log('AddSpecies', search);
            search.pendingAdd = true;
            search.objectErrors = [];
            return $http.post('/rest/species', {}).then(function (response) {
                $log.log('Response from post species', [response]);
                search.addedSpecies = new Species(response.data);
                search.name = search.query;
                return VernacularService.addVernacular(search.addedSpecies, search);
            }).then(function (response) {
                $log.log('Response from VernacularService.addVernacular', [response]);
                state.speciesArray.push(search.addedSpecies);
                state.selectedSpecies = search.addedSpecies;
                $uibModal.open({
                    templateUrl: 'views/species-modal.html',
                    controller: SpeciesModalController
                });
            }).catch(function (response) {
                $log.error('Add species failed', [response]);
                search.objectErrors = response.data;
            }).finally(function () {
                search.pendingAdd = false;
            });
        },
        updateSpecies: function (species, updateObj, overrideValue) {
            $log.info('updateSpecies', [species, updateObj, overrideValue]);
            updateObj.displayModReview = false;
            overrideValue && (updateObj.value = overrideValue);
            if (species[updateObj.field] === updateObj.value) {
                updateObj.visible = false;
                return;
            }
            var data = {
                scientificName: species.scientificName,
                iconFileName: species.iconFileName,
                family: species.family
            };
            data[updateObj.field] = updateObj.value;
            return $http.put('/rest/species/' + species.id, data).then(function (response) {
                $log.log('Response from put species', [response]);
                updateObj.visible = false;
                delete updateObj.objectErrors;
                if (response.status === 200) {
                    angular.extend(species, response.data);//merge new values into species
                } else if (response.status === 202) {
                    updateObj.displayModReview = true;
                }
            }).catch(function (response) {
                updateObj.objectErrors = response.data;
                updateObj.errorName = updateObj.value;
            });
        },
        searchSpecies: function (sq) {
            sq.objectErrors = [];
            var queryLowerCase = sq.query.toLocaleLowerCase();
            //Cancel search if new search within 2sec
            $timeout.cancel(sq.promise);
            sq.proccessing = false;
            if (!sq.query || sq.query.length < 2) {
                return;
            }
            sq.proccessing = true;
            sq.promise = $timeout(function () {
                $log.debug('Search with query', sq);
                $http.post('/rest/species/search', {query: sq.query}).then(function (response) {
                    response.data.forEach(function (species) {
                        if (!state.speciesArray.smigoFind(species.id, 'id')) {
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
        getSpecies: getSpecies,
        addVariety: function (editObj, speciesId) {
            var duplicate = state.varieties.some(function (v) {
                return v.speciesId === speciesId && v.name.toUpperCase() === editObj.value.toUpperCase();
            });
            $log.log('Add Variety:', [editObj, speciesId, duplicate]);
            if (!editObj.value || duplicate) {
                editObj.visible = false;
                return;
            }
            var variety = new Variety(editObj.value.capitalize(), speciesId);
            return $http.post('/rest/variety/', variety)
                .then(function (response) {
                    $log.log('Response from add variety', response);
                    variety.id = response.data;
                    state.varieties.push(variety);
                    editObj.value = '';
                    editObj.visible = false;
                    delete editObj.objectErrors;
                }).catch(function (response) {
                    $log.log('Add Variety failed:', response);
                    editObj.objectErrors = response.data;
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