function SpeciesFilter($log, orderByFilter) {
    return function (speciesArray, query) {
//        console.time('SpeciesFilter');
//        console.log('SpeciesFilter', [input, query]);
        if (!query) {
            return orderByFilter(speciesArray, 'vernacularName');
        }

        var ret = [];
        var queryLowerCase = query.toLowerCase();
        angular.forEach(speciesArray, function (s) {
            if (s.vernacularName &&
                (s.vernacularName.toLowerCase().indexOf(queryLowerCase) === 0 ||
                    s.vernacularName.toLowerCase().indexOf(' ' + queryLowerCase) !== -1)) {
                ret.unshift(s);
            } else if (s.scientificName &&
                (s.scientificName.toLowerCase().indexOf(queryLowerCase) === 0 ||
                    s.scientificName.toLowerCase().indexOf(' ' + queryLowerCase) !== -1)) {
                ret.push(s);
            }
        });
//        console.timeEnd('SpeciesFilter');
        return ret;
    };
}

angular.module('smigoModule').filter('speciesFilter', SpeciesFilter);