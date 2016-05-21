function VarietyService($log, $http) {
    'use strict';
    var state = {varieties: []};

    $log.log('VarietyService', state);

    reloadVarieties();

    function reloadVarieties() {
        return $http.get('/rest/variety').then(function (response) {
            $log.log('Response from /rest/varieties', response);
            state.varieties.length = 0;
            Array.prototype.push.apply(state.varieties, response.data);
        });

    }

    function Variety(name, speciesId) {
        this.name = name;
        this.speciesId = speciesId;
    }

    return {
        addVariety: function (editObj, speciesId) {
            $log.log('Add Variety:', [editObj, speciesId]);
            var duplicate = function (v) {
                return v.speciesId === speciesId && v.name.toUpperCase() === editObj.value.toUpperCase();
            };
            if (!editObj.value || state.varieties.some(duplicate)) {
                editObj.visible = false;
                return;
            }
            var variety = new Variety(editObj.value.capitalize(), speciesId);
            return $http.post('/rest/variety/', variety).then(function (response) {
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
        getVariety: function (varietyId) {
            return state.varieties.smigoFind(varietyId, 'id');
        },
        getAllVarieties: function () {
            return state.varieties;
        }
    }
}
angular.module('smigoModule').factory('VarietyService', VarietyService);