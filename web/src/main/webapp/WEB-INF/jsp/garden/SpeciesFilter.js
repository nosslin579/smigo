function SpeciesFilter(orderByFilter) {
    return function (speciesArray, searchString) {
//        console.time('SpeciesFilter');
//        console.log('SpeciesFilter', [input, searchString]);
        if (!searchString) {
            return orderByFilter(speciesArray, 'vernacularName');
        }

        var ret = [];
        var searchStringLowerCase = searchString.toLowerCase();
        angular.forEach(speciesArray, function (s) {
            if (s.vernacularName &&
                (s.vernacularName.toLowerCase().indexOf(searchStringLowerCase) === 0 ||
                    s.vernacularName.toLowerCase().indexOf(' ' + searchStringLowerCase) !== -1)) {
                ret.unshift(s);
            } else if (s.scientificName &&
                (s.scientificName.toLowerCase().indexOf(searchStringLowerCase) === 0 ||
                    s.scientificName.toLowerCase().indexOf(' ' + searchStringLowerCase) !== -1)) {
                ret.push(s);
            }
        });
//        console.timeEnd('SpeciesFilter');
        return ret;
    };
}

angular.module('smigoModule').filter('speciesFilter', SpeciesFilter);