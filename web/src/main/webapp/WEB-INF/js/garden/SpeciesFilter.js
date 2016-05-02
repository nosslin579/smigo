function SpeciesFilter($log, orderByFilter, translateFilter, VernacularService) {

    var vernaculars = VernacularService.getState().vernaculars;

    function searchVernacular(query) {
        var ret = [];
        for (var i = 0; i < vernaculars.length; i++) {
            var vernacularIndex = vernaculars[i].vernacularName.toLowerCase().indexOf(query);
            if (vernacularIndex !== -1 && query.length > 2 || vernacularIndex === 0) {
                ret.push(vernaculars[i].speciesId);
            }
        }
        return ret;
    }

    function getVernacularLowerCase(s) {
        return VernacularService.getVernacular(s.id).vernacularName.toLowerCase();
    }

    return function (speciesArray, query) {
//        console.time('SpeciesFilter');
//        console.log('SpeciesFilter', [speciesArray, query]);
        if (!query) {
            return orderByFilter(speciesArray, getVernacularLowerCase);
        }

        var ret = [];
        var queryLowerCase = query.toLowerCase();
        var vernacularMatchesArray = searchVernacular(queryLowerCase);
        angular.forEach(speciesArray, function (s) {
            if (vernacularMatchesArray.indexOf(s.id) !== -1) {
                ret.push(s);
            } else if (query.length > 2) {
                (s.scientificName && s.scientificName.toLowerCase().indexOf(queryLowerCase) !== -1 ||
                s.family && translateFilter(s.family).toLowerCase().indexOf(queryLowerCase) === 0 ||
                s.family && s.family.name.toLocaleLowerCase().indexOf(queryLowerCase) === 0)
                && ret.push(s);
            }
        });
//        console.timeEnd('SpeciesFilter');
        return orderByFilter(ret, getVernacularLowerCase);
    };
}

angular.module('smigoModule').filter('speciesFilter', SpeciesFilter);