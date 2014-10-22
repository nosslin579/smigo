function FacebookDirective($log, $window, $timeout) {
    $log.debug('Creating facebook directive');

    return {
        restrict: 'A',
        scope: {
            ngModel: '='
        },
        require: '^ngModel',
        template: '<a ng-if="ngModel" href="https://www.facebook.com/sharer/sharer.php?u=http://smigo.org/wall/{{ngModel}}" target="_blank"><span class="social-sprite facebook"></span>&NonBreakingSpace;</a>',
        link: function () {

        }
    }
}
angular.module('smigoModule').directive('facebook', FacebookDirective);