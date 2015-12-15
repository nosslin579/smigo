function TwitterDirective($log, $timeout) {
    return {
        restrict: 'A',
        scope: {
            ngModel: '='
        },
        require: '^ngModel',
        template: '<a ng-if="ngModel" href="http://twitter.com/share?text=' + "{{'msg.twittersharetext' | translate}}" + '&url=http://smigo.org/gardener/{{ngModel}}&via=smigogarden" target="_blank"><span class="social-sprite twitter"></span>&nbsp;Twitter</a>'
    }
}
angular.module('smigoModule').directive('twitter', TwitterDirective);