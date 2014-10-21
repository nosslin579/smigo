function TwitterDirective($log, $timeout) {
    return {
        restrict: 'A',
        scope: {
            ngModel: '='
        },
        require: '^ngModel',
        template: '<a href="http://twitter.com/share?text=' + "{{'msg.twittersharetext' | translate}}" + '&url=http://smigo.org/wall/{{ngModel}}&via=smigogarden" target="_blank"><span class="social-sprite twitter"></span>&NonBreakingSpace;</a>'
    }
}
angular.module('smigoModule').directive('twitter', TwitterDirective);