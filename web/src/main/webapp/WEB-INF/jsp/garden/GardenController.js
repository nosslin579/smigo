function GardenController($scope, PlantService) {
    console.log('GardenController', $scope);

    //Garden
    $scope.$watch(PlantService.getGarden, function () {
        $scope.garden = PlantService.getGarden();
        $scope.forwardYear = +Object.keys(PlantService.getGarden().squares).sort().slice(-1).pop() + 1;
        $scope.backwardYear = +Object.keys(PlantService.getGarden().squares).sort()[0] - 1;
    }, true);

    //Year
    $scope.$watch(PlantService.getSelectedYear, function (newVal, oldVal, scope) {
        $scope.selectedYear = PlantService.getSelectedYear();
        $scope.availableYears = PlantService.getAvailableYears();
    });
    $scope.addYear = PlantService.addYear;
    $scope.setSelectedYear = PlantService.setSelectedYear;

    //Species
    $scope.$watch(PlantService.getSelectedSpecies, function (newVal, oldVal, scope) {
        $scope.selectedSpecies = PlantService.getSelectedSpecies();
    });
    $scope.setSelectedSpecies = PlantService.setSelectedSpecies;

    //Action
    //todo

    $scope.onSquareClick = function (clickEvent, square) {
        console.log('Square clicked', [clickEvent, square, $scope.selectedSpecies]);
        if (clickEvent.shiftKey) {
            PlantService.removePlant(square);
        } else if (clickEvent.ctrlKey) {
            console.log('Copy species');
        } else {
            PlantService.addPlant($scope.selectedSpecies, square);
        }
        clickEvent.stopPropagation();
    };

    $scope.onVisibleRemainderClick = function (clickEvent, s) {
        PlantService.addSquare($scope.selectedYear, s.location.x, s.location.y, $scope.selectedSpecies);
        PlantService.save();
        clickEvent.stopPropagation();
    };

    $scope.onGridClick = function (clickEvent) {
        //http://stackoverflow.com/a/14872192/859514
        var offsetX = clickEvent.clientX - $(clickEvent.target).offset().left;
        var offsetY = clickEvent.clientY - $(clickEvent.target).offset().top;
        var x = Math.floor((offsetX - 100000) / 48);
        var y = Math.floor((offsetY - 100000) / 48);
        PlantService.addSquare($scope.selectedYear, x, y, $scope.selectedSpecies);
        clickEvent.stopPropagation();
    };

    $scope.getGridSizeCss = function (year) {
        var bounds = PlantService.getBounds(year);
//        console.log('Grid size', bounds);
        var margin = 48 * 2;
        return {
            'margin-top': (-100000 + -bounds.ymin * 48 + margin) + 'px',
            'width': (100000 + 47 + bounds.xmax * 48 + margin) + 'px',
            'height': (100000 + 47 + bounds.ymax * 48 + margin) + 'px',
            'margin-left': (-100000 + -bounds.xmin * 48 + margin) + 'px'
        };
    };

    $scope.getSquarePositionCss = function (square) {
        return {
            top: square.location.y * 48 + 100000 + 'px',
            left: square.location.x * 48 + 100000 + 'px'
        };
    };
}

angular.module('smigoModule').controller('GardenController', GardenController);