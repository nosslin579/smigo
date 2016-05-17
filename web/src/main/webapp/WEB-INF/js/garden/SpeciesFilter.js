function SpeciesFilter($log, orderByFilter, translateFilter, VernacularService) {
    'use strict';
    var vernaculars = VernacularService.getState().vernaculars;

    /**
     * @returns {Array} array of speciesId that matches query
     */
    function searchVernacular(queryUpperCase) {
        var ret = [];
        for (var i = 0; i < vernaculars.length; i++) {
            var vernacularIndex = vernaculars[i].vernacularName.toUpperCase().indexOf(queryUpperCase);
            if (vernacularIndex !== -1 && queryUpperCase.length > 2 || vernacularIndex === 0) {
                ret.push(vernaculars[i].speciesId);
            }
        }
        return ret;
    }

    function getVernacularUpperCase(s) {
        return VernacularService.getVernacular(s.id).vernacularName.toUpperCase();
    }

    return function (speciesArray, query) {
//        console.time('SpeciesFilter');
//        console.log('SpeciesFilter', [speciesArray, query]);
        if (!query) {
            return orderByFilter(speciesArray, getVernacularUpperCase);
        }

        var ret = [];
        var queryUpperCase = query.toUpperCase();
        var vernacularMatchesArray = searchVernacular(queryUpperCase);
        angular.forEach(speciesArray, function (s) {
            if (s.id == query || vernacularMatchesArray.indexOf(s.id) !== -1) {
                ret.push(s);
            } else if (query.length > 2) {
                (s.scientificName && s.scientificName.toUpperCase().indexOf(queryUpperCase) !== -1 ||
                s.family && translateFilter(s.family.messageKey).toUpperCase().indexOf(queryUpperCase) === 0 ||
                s.family && s.family.name.toUpperCase().indexOf(queryUpperCase) === 0)
                && ret.push(s);
            }
        });
//        console.timeEnd('SpeciesFilter');
        return orderByFilter(ret, getVernacularUpperCase);
    };
}

angular.module('smigoModule').filter('speciesFilter', SpeciesFilter);