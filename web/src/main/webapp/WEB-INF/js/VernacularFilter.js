function vernacularFilter($log, VernacularService) {
    return function (speciesId) {
        return VernacularService.getVernacular(speciesId).vernacularName;
    };
}
angular.module('smigoModule').filter('vernacular', vernacularFilter);