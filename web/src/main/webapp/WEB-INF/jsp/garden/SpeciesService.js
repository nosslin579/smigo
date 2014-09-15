function SpeciesService(InitService, $rootScope, translateFilter, Http) {
    var speciesArray = InitService.garden.species;
    updateVernacularName();

    console.log('SpeciesService', [speciesArray]);

    $rootScope.$on('newGardenAvailable', function (event, garden) {
        speciesArray = garden.species;
        updateVernacularName();
    });

    function updateVernacularName() {
        angular.forEach(speciesArray, function (s) {
            s.vernacularName = translateFilter(s);
        });
    }

    function Species(vernacularName) {
        this.vernacularName = vernacularName;
        this.annual = true;
        this.item = false;
        this.iconFileName = 'defaulticon.png';
    }

    return {
        addSpecies: function (vernacularName) {
            var name = vernacularName.capitalize();
            var species = new Species(name);
            speciesArray.push(species);
            return Http.post('rest/species', species)
                .then(function (response) {
                    species.id = response.data;
                })
                .then(function () {
                    var translation = {messageKey: 'species' + species.id, value: vernacularName};
                    InitService.messages[translation.messageKey] = translation.value;
                });
        },
        getSpecies: function () {
            return speciesArray;
        }
    }
}
angular.module('smigoModule').factory('SpeciesService', SpeciesService);