function vernacularFilter($log, VernacularService) {
    'use strict';
    return function (speciesId) {
        return VernacularService.getVernacular(speciesId).vernacularName;
    };
}
angular.module('smigoModule').filter('vernacular', vernacularFilter);