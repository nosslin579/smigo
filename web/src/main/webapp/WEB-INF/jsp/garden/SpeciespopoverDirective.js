angular.module('ui.bootstrap.speciespopover', [ 'ui.bootstrap.tooltip' ])
    .directive('speciespopoverPopup', function () {
        return {
            restrict: 'EA',
            replace: true,
            scope: { title: '@', content: '@', placement: '@', animation: '&', isOpen: '&', species: '='},
            templateUrl: 'speciespopover.html'
        };
    })
    .directive('speciespopover', ['$tooltip', function ($tooltip) {
        return  $tooltip('speciespopover', 'speciespopover', 'mouseenter');
    }]);