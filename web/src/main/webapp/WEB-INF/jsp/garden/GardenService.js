function GardenService(PlantService, SpeciesService, $rootScope, $http) {
    var model = {};

    function initModel() {
        var availableYears = PlantService.getAvailableYears();
        model.selectedSpecies = SpeciesService.getSpecies()[0];
        model.selectedYear = availableYears.slice(-1).pop();
        model.availableYears = availableYears;
        model.action = 'add';
        console.log('Garden model initialized', model);
    }

    initModel();

    $rootScope.$on('newGardenAvailable', initModel);

    return {
        model: model,
        fn: {
            addSpecies: function (vernacularName) {
                SpeciesService.addSpecies(vernacularName);
            },
            isSpeciesAddable: function (vernacularName) {
                if (!vernacularName) {
                    return false;
                }
//                console.log('isSpeciesAddable', vernacularName);
                var name = vernacularName.toLowerCase();
                for (var i = 0; i < SpeciesService.getSpecies().length; i++) {
                    var species = SpeciesService.getSpecies()[i];
                    if (species.vernacularName && species.vernacularName.toLowerCase() == name) {
                        return false;
                    }
                }
                return true;
            },
            setSelectedSpecies: function (species) {
                model.selectedSpecies = species;
                model.action = 'add';
                console.log('Species selected:', species);
            }
        },
        onSquareClick: function (clickEvent, square) {
            console.log('Square clicked', [clickEvent, square, model.selectedSpecies]);
            if (clickEvent.shiftKey || model.action == 'delete') {
                PlantService.removePlant(square);
            } else if (clickEvent.ctrlKey) {
                console.log('Copy species');
            } else {
                PlantService.addPlant(model.selectedSpecies, square);
            }
            clickEvent.stopPropagation();
        },
        onVisibleRemainderClick: function (clickEvent, square) {
            console.log('VisibleRemainder clicked', [clickEvent, square, model.selectedSpecies]);
            PlantService.addSquare(model.selectedYear, square.location.x, square.location.y, model.selectedSpecies);
            PlantService.save();
            clickEvent.stopPropagation();
        },
        onGridClick: function (clickEvent) {
            //http://stackoverflow.com/a/14872192/859514
            var offsetX = clickEvent.clientX - $(clickEvent.target).offset().left;
            var offsetY = clickEvent.clientY - $(clickEvent.target).offset().top;
            var x = Math.floor((offsetX - 100000) / 48);
            var y = Math.floor((offsetY - 100000) / 48);
            PlantService.addSquare(model.selectedYear, x, y, model.selectedSpecies);
            clickEvent.stopPropagation();
        },
        getGridSizeCss: function (year) {
            var bounds = PlantService.getBounds(year);
//        console.log('Grid size', bounds);
            var margin = 48 * 2;
            return {
                'margin-top': (-100000 + -bounds.ymin * 48 + margin) + 'px',
                'width': (100000 + 47 + bounds.xmax * 48 + margin) + 'px',
                'height': (100000 + 47 + bounds.ymax * 48 + margin) + 'px',
                'margin-left': (-100000 + -bounds.xmin * 48 + margin) + 'px'
            };
        },
        getSquarePositionCss: function (square) {
            return {
                top: square.location.y * 48 + 100000 + 'px',
                left: square.location.x * 48 + 100000 + 'px'
            };
        },
        reloadGarden: function () {
            return $http.get('rest/garden')
                .then(function (response) {
                    console.log('Garden reloaded', response);
                    $rootScope.$broadcast('newGardenAvailable', response.data);
                });
        }

    }
}
angular.module('smigoModule').factory('GardenService', GardenService);