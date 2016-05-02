function vernacularFilter($log, VernacularService) {
    return function (speciesId) {
        return VernacularService.getVernacularName(speciesId);
    };
}
angular.module('smigoModule').filter('vernacular', vernacularFilter);