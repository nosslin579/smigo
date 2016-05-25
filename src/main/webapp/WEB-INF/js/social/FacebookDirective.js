function FacebookDirective($log) {
    'use strict';
    $log.debug('Creating facebook directive');

    return {
        restrict: 'A',
        scope: {
            ngModel: '='
        },
        require: '^ngModel',
        template: '<a ng-if="ngModel" href="https://www.facebook.com/sharer/sharer.php?u=http://smigo.org/gardener/{{ngModel}}" target="_blank"><span class="social-sprite facebook"></span>&nbsp;Facebook</a>',
    }
}
angular.module('smigoModule').directive('facebook', FacebookDirective);