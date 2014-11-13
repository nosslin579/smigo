function GridService($http, $window, $timeout, $rootScope, $q, $log) {

    function getBounds(squareArrays) {
        var axisLength = 9999;
        var ret = {
            xmax: -axisLength,
            ymax: -axisLength,
            xmin: axisLength,
            ymin: axisLength
        };
        squareArrays.forEach(function (squareArray) {
            angular.forEach(squareArray, function (square, index) {
                ret.xmax = Math.max(square.location.x, ret.xmax);
                ret.ymax = Math.max(square.location.y, ret.ymax);
                ret.xmin = Math.min(square.location.x, ret.xmin);
                ret.ymin = Math.min(square.location.y, ret.ymin);
            });
        });
        return ret;
    }


    return {
        getGridSizeCss: function (garden) {
            console.time('grid size');
            var squareArrays = [garden.yearSquareMap[garden.selectedYear], garden.yearSquareMap[garden.selectedYear - 1]]
            var bounds = getBounds(squareArrays);
            var margin = 48 * 2;
            console.timeEnd('grid size');
            $log.debug('Grid CSS ', bounds, garden);
            return {
                'margin-top': (-100001 + -bounds.ymin * 48 + margin) + 'px',
                'width': (100000 + 47 + bounds.xmax * 48 + margin) + 'px',
                'height': (100000 + 47 + bounds.ymax * 48 + margin) + 'px',
                'margin-left': (-100001 + -bounds.xmin * 48 + margin) + 'px'
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
angular.module('smigoModule').factory('GridService', GridService);