function SpeciesService($rootScope, translateFilter, Http) {
    var speciesArray = [];
    console.log('SpeciesService');

    $rootScope.$on('newGardenAvailable', function (event, garden) {
        speciesArray = garden.species;
        angular.forEach(speciesArray, function (s) {
            s.vernacularName = translateFilter(s);
        });
    });

    function Species(vernacularName) {
        this.vernacularName = vernacularName;
        this.annual = true;
        this.item = false;
    }

    return {
        addSpecies: function (vernacularName) {
            var name = vernacularName.capitalize();
            var species = new Species(name);
            speciesArray.push(species);
            return Http.post('rest/species', species)
                .then(function (response) {
                    species.id = response.data;
                    var newMessage = {};
                    newMessage['species' + species.id] = vernacularName;
                    $rootScope.$broadcast('newMessagesAvailable', newMessage);
                });
        },
        getSpecies: function () {
            return speciesArray;
        }
    }
}
angular.module('smigoModule').factory('SpeciesService', SpeciesService);