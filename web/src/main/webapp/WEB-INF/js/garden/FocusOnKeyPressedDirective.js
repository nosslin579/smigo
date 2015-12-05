angular.module('smigoModule').directive('soFocusOnKeyPressed', function FocusOnKeyPressedDirective($log, $timeout, SpeciesService, GardenService, isTouchDevice) {
    return function (scope, element, attrs) {
        //$log.log('FocusOnKeyPressedDirective', [scope, element, attrs]);
        var handler = function (e) {
            $log.log('FocusOnKeyPressedDirective keypressed', [e, document.activeElement]);
            element.focus();
        };
        document.addEventListener('keypress', handler, false);
        scope.$on('$destroy', function () {
            document.removeEventListener('keypress', handler, false);
        });
    };
})
;