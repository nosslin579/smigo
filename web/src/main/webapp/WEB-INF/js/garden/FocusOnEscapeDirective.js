angular.module('smigoModule').directive('soFocusOnEscape', function FocusOnEscapeDirective($log, $timeout, SpeciesService, GardenService, isTouchDevice) {
    return function (scope, element, attrs) {
        //$log.log('FocusOnEscapeDirective', [scope, element, attrs]);
        var handler = function (e) {
            $log.log('FocusOnEscapeDirective keypressed', e);
            e.keyCode === 27 && element.focus();
        };
        document.addEventListener('keydown', handler, false);
        scope.$on('$destroy', function () {
            document.removeEventListener('keydown', handler, false);
        });
    };
})
;