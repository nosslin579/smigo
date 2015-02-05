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
            if (s.vernacularName && vernacularIndex !== -1) {
                vernacularIndex === 0 ? ret.unshift(s) : ret.push(s);
            } else if (s.scientificName &&
                (s.scientificName.toLowerCase().indexOf(queryLowerCase) === 0 ||
                    s.scientificName.toLowerCase().indexOf(' ' + queryLowerCase) !== -1)) {
                ret.push(s);
            } else if (s.family && (translateFilter(s.family).toLowerCase().indexOf(queryLowerCase) === 0 ||
                s.family.name.toLocaleLowerCase().indexOf(queryLowerCase) === 0)) {
                ret.unshift(s);
            }
        });
//        console.timeEnd('SpeciesFilter');
        return ret;
    };
}

angular.module('smigoModule').filter('speciesFilter', SpeciesFilter);