function SpeciesService(InitService, $rootScope, translateFilter) {
    var speciesArray = InitService.garden.species;
    updateVernacularName();

    console.log('SpeciesService', [speciesArray]);

    $rootScope.$on('newGardenAvailable', function (event, garden) {
        speciesArray = garden.species;
        updateVernacularName();
    });

    function updateVernacularName() {
        angular.forEach(speciesArray, function (s) {
            var name = translateFilter(s);
            s.vernacularName = name ? name : s.scientificName;
        });
    }

    return {
        getSpecies: function () {
            return speciesArray;
        }
    }
}
angular.module('smigoModule').factory('SpeciesService', SpeciesService);