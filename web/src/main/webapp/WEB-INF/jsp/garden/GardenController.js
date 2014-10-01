function GardenController($modal, $scope, $filter, PlantService, SpeciesService, UserService, $log) {

    $scope.plantsState = PlantService.getState();
    $scope.speciesState = SpeciesService.getState();
    $scope.userState = UserService.getState();

    $scope.selectYear = PlantService.selectYear;
    $scope.addSpecies = SpeciesService.addSpecies;
    $scope.selectSpecies = SpeciesService.selectSpecies;

    $scope.selectedSpeciesFromTopResult = function (searchString) {
        $log.log('Setting species from', searchString);
        var topResult = $filter('speciesFilter')(SpeciesService.getSpecies(), searchString)[0];
        if (topResult) {
            SpeciesService.selectSpecies(topResult);
        }
        $scope.speciesSearch = '';
    };
    $scope.onSquareClick = function (clickEvent, square) {
        $log.log('Square clicked', [clickEvent, square, SpeciesService.getState().selectedSpecies, SpeciesService.getState().action]);
        if (clickEvent.shiftKey || SpeciesService.getState().action == 'delete') {
            PlantService.removePlant(square);
        } else if (SpeciesService.getState().action == 'info') {
            square.showTooltip = !square.showTooltip;
        } else if (clickEvent.ctrlKey) {
            $log.log('Copy species');
        } else {
            PlantService.addPlant(SpeciesService.getState().selectedSpecies, square);
        }
        clickEvent.stopPropagation();
    };
    $scope.onGridClick = function (clickEvent) {
        //http://stackoverflow.com/a/14872192/859514
        var offsetX = clickEvent.clientX - $(clickEvent.target).offset().left;
        var offsetY = clickEvent.clientY - $(clickEvent.target).offset().top;
        var x = Math.floor((offsetX - 100000) / 48);
        var y = Math.floor((offsetY - 100000) / 48);
        PlantService.addSquare(PlantService.getState().selectedYear, x, y, SpeciesService.getState().selectedSpecies);
        clickEvent.stopPropagation();
    };
    $scope.getGridSizeCss = function (year) {
        var bounds = PlantService.getBounds(year);
//        $log.log('Grid size', bounds);
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
    $scope.isSpeciesAddable = function (vernacularName) {
        if (!vernacularName) {
            return false;
        }
//                $log.log('isSpeciesAddable', vernacularName);
        var name = vernacularName.toLowerCase();
        for (var i = 0; i < SpeciesService.getSpecies().length; i++) {
            var species = SpeciesService.getSpecies()[i];
            if (species.vernacularName && species.vernacularName.toLowerCase() == name) {
                return false;
            }
        }
        return true;
    };


    $scope.openAddYearModal = function () {
        $modal.open({
            templateUrl: 'add-year-modal.html',
            controller: AddYearModalController,
            size: 'sm'
        });
    };

    $scope.scroll = function (left, top) {
        var peepholeElement = $('#peephole')[0];
        $log.log('Scroll', peepholeElement);
        peepholeElement.scrollLeft = peepholeElement.scrollLeft + left;
        peepholeElement.scrollTop = peepholeElement.scrollTop + top;
    };
}

angular.module('smigoModule').controller('GardenController', GardenController);