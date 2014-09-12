function GardenService(PlantService) {
    var model = {
        selectedSpecies: PlantService.getGarden().species["28"],
        selectedYear: +Object.keys(PlantService.getGarden().squares).sort().slice(-1).pop(),
        availableYears: PlantService.getAvailableYears()
    };
    return {
        model: model,
        onSquareClick: function (clickEvent, square) {
            console.log('Square clicked', [clickEvent, square, model.selectedSpecies]);
            if (clickEvent.shiftKey) {
                PlantService.removePlant(square);
            } else if (clickEvent.ctrlKey) {
                console.log('Copy species');
            } else {
                PlantService.addPlant(model.selectedSpecies, square);
            }
            clickEvent.stopPropagation();
        },
        onVisibleRemainderClick: function (clickEvent, square) {
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
        }
    }
}
angular.module('smigoModule').factory('GardenService', GardenService);