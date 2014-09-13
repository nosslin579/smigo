function SpeciesService(InitService, $rootScope) {
    var speciesMap = InitService.garden.species;
    console.log('SpeciesService', [speciesMap]);

    $rootScope.$on('newGardenAvailable', function (event, garden) {
        speciesMap = garden.species;
    });

    return {
        getSpecies: function () {
            return speciesMap;
        }
    }
}
angular.module('smigoModule').factory('SpeciesService', SpeciesService);