function VernacularService($http, $rootScope, $log, $q) {
    var state = {vernaculars: []};

    $log.log('VernacularService', state);

    $http.get('/rest/vernacular').then(function (response) {
        $log.log('Response from /rest/vernacular', response);
        state.vernaculars.length = 0;
        Array.prototype.push.apply(state.vernaculars, response.data);
    });

    $rootScope.$on('messages-reloaded', function (event, msg) {
        //todo
    });

    function Vernacular(speciesId) {
        //this.id = id;
        this.speciesId = speciesId;
        this.vernacularName = 'id' + speciesId;
    }

    function getVernaculars(speciesId) {
        return state.vernaculars.filter(function (v) {
            return v.speciesId === speciesId;
        });
    }

    return {
        getState: function () {
            return state;
        },
        getVernacular: function (speciesId) {
            return state.vernaculars.smigoFind(speciesId, 'speciesId', new Vernacular(speciesId));
        },
        getVernacularName: function (speciesId) {
            return state.vernaculars.smigoFind(speciesId, 'speciesId', new Vernacular(speciesId)).vernacularName;
        },
        getVernaculars: getVernaculars,
        addVernacular: function (species, updateObj) {
            $log.info('addVernacular', [species, updateObj]);
            updateObj.displayModReview = false;
            if (!updateObj.name) {
                updateObj.visible = false;
                return;
            }
            updateObj.name = updateObj.name.capitalize();
            var data = {
                vernacularName: updateObj.name,
                speciesId: species.id
            };
            return $http.post('/rest/vernacular', data).then(function (response) {
                $log.log('Response from /rest/vernacular', [response, state.vernaculars]);
                updateObj.visible = false;
                delete updateObj.objectErrors;
                if (response.status === 200) {
                    data.id = response.data;
                    state.vernaculars.push(data);
                } else if (response.status === 202) {
                    updateObj.displayModReview = true;
                }
                updateObj.visible = false;
                return $q.resolve();
            }).catch(function (response) {
                $log.error('Add vernacular failed', [response, updateObj, species]);
                updateObj.objectErrors = response.data;
                updateObj.errorName = updateObj.name;
                return $q.reject(response);
            });
        },
        deleteVernacular: function (species, updateObj, vernacular) {
            $log.info('deleteVernacular', [species, updateObj, vernacular]);
            updateObj.displayModReview = false;
            if (getVernaculars(species.id).length === 1) {
                updateObj.objectErrors = [{defaultMessage: 'msg.unknownerror'}];
                return;
            }
            return $http.delete('/rest/vernacular/' + vernacular.id).then(function (response) {
                $log.log('Response from delete /rest/verncular/' + vernacular.id, [response, state]);
                if (response.status === 200) {
                    var indexOfDeleteElement = state.vernaculars.indexOf(vernacular);
                    state.vernaculars.splice(indexOfDeleteElement, 1);
                } else if (response.status === 202) {
                    updateObj.displayModReview = true;
                }
                updateObj.visible = false;
            }).catch(function (response) {
                $log.error('Fail: delete /rest/verncular/' + vernacular.id, [response, updateObj, vernacular]);
                updateObj.objectErrors = response.data;
            });
        }
    }
}
angular.module('smigoModule').factory('VernacularService', VernacularService);