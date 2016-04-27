function SpeciesFilter($log, orderByFilter, translateFilter) {
    return function (speciesArray, query) {
//        console.time('SpeciesFilter');
//        console.log('SpeciesFilter', [input, query]);
        if (!query) {
            return orderByFilter(speciesArray, 'vernacularName');
        }

        var ret = [];
        var queryLowerCase = query.toLowerCase();
        angular.forEach(speciesArray, function (s) {
            var vernacularIndex = s.vernacularName.toLowerCase().indexOf(queryLowerCase);
            if (vernacularIndex !== -1 && query.length > 2 || vernacularIndex === 0) {
                ret.push(s);
            } else if (query.length > 2) {
                var searchResultVernacularArray = s.vernaculars.filter(function (v) {
                    return v.vernacularName.toLowerCase().indexOf(queryLowerCase) !== -1
                });
                (searchResultVernacularArray.length > 0 ||
                s.scientificName && s.scientificName.toLowerCase().indexOf(queryLowerCase) !== -1 ||
                s.family && translateFilter(s.family).toLowerCase().indexOf(queryLowerCase) === 0 ||
                s.family && s.family.name.toLocaleLowerCase().indexOf(queryLowerCase) === 0)
                && ret.push(s);
            }
        });
//        console.timeEnd('SpeciesFilter');
        return orderByFilter(ret, 'vernacularName');
    };
}

angular.module('smigoModule').filter('speciesFilter', SpeciesFilter);