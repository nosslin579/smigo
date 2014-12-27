angular.module('smigoModule').directive('soGrid', function ($log, $timeout, SpeciesService, GardenService, isTouchDevice) {
    return {
        link: function link(scope, element, attrs) {
            $log.log('grid', [scope, element, attrs]);

            scope.getSquarePositionCss = function getSquarePositionCss(square) {
                return {
                    top: square.location.y * 48 + 100000 + 'px',
                    left: square.location.x * 48 + 100000 + 'px'
                };
            }
            scope.getGridSizeCss = function getGridSizeCss(garden) {
                function getBounds(squareArrays) {
                    var axisLength = 9999;
                    var ret = {
                        xmax: -axisLength,
                        ymax: -axisLength,
                        xmin: axisLength,
                        ymin: axisLength
                    };
                    squareArrays.forEach(function (squareArray) {
                        if (squareArray) {
                            for (var i = 0; i < squareArray.length; i++) {
                                var square = squareArray[i];
                                ret.xmax = Math.max(square.location.x, ret.xmax);
                                ret.ymax = Math.max(square.location.y, ret.ymax);
                                ret.xmin = Math.min(square.location.x, ret.xmin);
                                ret.ymin = Math.min(square.location.y, ret.ymin);
                            }
                        }
                    });
                    return ret;
                }

//            console.time('grid size');
                var bounds = getBounds([garden.yearSquareMap[garden.selectedYear], garden.getTrailingSquares()]);
                var margin = 48 * 2;
//            console.timeEnd('grid size');
//                $log.log('Grid CSS ', bounds, garden);
                return {
                    'margin-top': (-100001 + -bounds.ymin * 48 + margin) + 'px',
                    'width': (100000 + 47 + bounds.xmax * 48 + margin) + 'px',
                    'height': (100000 + 47 + bounds.ymax * 48 + margin) + 'px',
                    'margin-left': (-100001 + -bounds.xmin * 48 + margin) + 'px'
                };
            }
            scope.onSquareClick = function onSquareClick(clickEvent, square) {
                $log.log('Square clicked', [clickEvent, square, SpeciesService.getState().selectedSpecies, SpeciesService.getState().action]);
                if (clickEvent.shiftKey || SpeciesService.getState().action == 'delete') {
                    square.removePlant();
                } else if (SpeciesService.getState().action == 'info') {
                    square.showTooltip = !square.showTooltip;
                    if (square.showTooltip) {
                        $timeout(function () {
                            square.showTooltip = false;
                        }, 3000);
                    }
                } else if (clickEvent.ctrlKey) {
                    $log.log('Copy species');
                } else {
                    square.togglePlant(SpeciesService.getState().selectedSpecies);
                }
                clickEvent.stopPropagation();
            };

            element.find('.square-container').on('click', function (clickEvent) {
                $log.log('Grid clicked', [clickEvent]);
                if (SpeciesService.getState().action == 'add') {
                    //http://stackoverflow.com/a/14872192/859514
                    var offsetX = clickEvent.clientX - $(clickEvent.target).offset().left;
                    var offsetY = clickEvent.clientY - $(clickEvent.target).offset().top;
                    var x = Math.floor((offsetX - 100000) / 48);
                    var y = Math.floor((offsetY - 100000) / 48);
                    scope.garden.getSquare(scope.garden.selectedYear, x, y).addPlant(SpeciesService.getState().selectedSpecies);
                }
                clickEvent.stopPropagation();

            });
        },
        templateUrl: 'grid.html',
        scope: {garden: '=soGrid'}
    }
});