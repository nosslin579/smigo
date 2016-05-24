angular.module('smigoModule').directive('soFocusOnKeyPressed', function FocusOnKeyPressedDirective($log) {
    'use strict';
    return function (scope, element, attrs) {
        //$log.log('FocusOnKeyPressedDirective', [scope, element, attrs]);

        var handler = function (e) {
            //http://stackoverflow.com/questions/302122/jquery-event-keypress-which-key-was-pressed
            var code = e.keyCode || e.which;
            var arrowKey = code > 36 && code < 41;
            if (!element.is(':focus') && !arrowKey) {
                $log.log('FocusOnKeyPressedDirective keypressed', [e.which, e.code, e.key, e.keyCode, e.char, e.charCode, e, document.activeElement]);
                element.focus();
                scope.$apply(function () {
                    scope.search.query = '';
                });
            }
        };

        var sourceElement = document.getElementById(attrs.soFocusOnKeyPressed);
        sourceElement.tabIndex = 0;//must be focusable for event listener to catch event
        sourceElement.style['outline-style'] = 'none';
        sourceElement.addEventListener('keypress', handler, false);
        scope.$on('$destroy', function () {
            document.getElementById(attrs.soFocusOnKeyPressed).removeEventListener('keypress', handler, false);
        });
    };
})
;