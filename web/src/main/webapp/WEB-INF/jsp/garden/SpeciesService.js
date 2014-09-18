function SpeciesService($http, $rootScope, translateFilter, Http) {
    var state = {
        speciesArray: [],
        selectedSpecies: new Species('not set'),
        action: 'add'
    };
    updateState(initData.species);
    console.log('SpeciesService', state);

    $rootScope.$on('current-user-changed', function (event, user) {
        if (user) {
            reloadSpecies();
        }
    });

    function Species(vernacularName) {
        this.vernacularName = vernacularName;
        this.annual = true;
        this.item = false;
        this.iconFileName = "defaulticon.png";
    }

    function reloadSpecies() {
        return $http.get('rest/species')
            .then(function (response) {
                console.log('Species retrieve successful. Response:', response);
                updateState(response.data);
            });
    }

    function updateState(species) {
        state.speciesArray = species;
        state.selectedSpecies = species[0];
        angular.forEach(state.speciesArray, function (s) {
            s.vernacularName = translateFilter(s);
        });
        console.log('Species state updated', state);
    }

    return {
        getState: function () {
            return state;
        },
        selectSpecies: function (species) {
            state.selectedSpecies = species;
            state.action = 'add';
            console.log('Species selected:', state);
        },
        addSpecies: function (vernacularName) {
            var name = vernacularName.capitalize();
            var species = new Species(name);
            state.selectedSpecies = species;
            state.speciesArray.push(species);
            console.log('Species added:' + vernacularName, state);
            return Http.post('rest/species', species)
                .then(function (response) {
                    console.log('Response from post species', response);
                    species.id = response.data;
                    var newMessage = {};
                    newMessage['species' + species.id] = vernacularName;
                    $rootScope.$broadcast('newMessagesAvailable', newMessage);
                });
        },
        getSpecies: function () {
            return state.speciesArray;
        }
    }
}
angular.module('smigoModule').factory('SpeciesService', SpeciesService);