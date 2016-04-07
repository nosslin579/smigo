angular.module('smigoModule').directive('soFocusOnKeyPressed', function FocusOnKeyPressedDirective($log, $timeout, SpeciesService, GardenService, isTouchDevice) {
    return function (scope, element, attrs) {
        //$log.log('FocusOnKeyPressedDirective', [scope, element, attrs]);

        var handler = function (e) {
            if (!element.is(':focus')) {
                $log.log('FocusOnKeyPressedDirective keypressed', [e, document.activeElement]);
                element.focus();
                scope.$apply(function () {
                    scope.search.query = '';
                });
            }
        };

        scope.$on('species-modal-open', function () {
            document.removeEventListener('keypress', handler, false);
        });
        scope.$on('species-modal-close', function () {
            document.addEventListener('keypress', handler, false);
        });

        document.addEventListener('keypress', handler, false);
        scope.$on('$destroy', function () {
            document.removeEventListener('keypress', handler, false);
        });
    };
})
;