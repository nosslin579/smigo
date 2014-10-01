angular.module('smigoModule').directive('squareInfo', function ($timeout) {
    return {
        link: function link(scope, tooltipElement, attrs) {
            var showPromise,
                squareElement = tooltipElement.parent();

            function showSquareTooltip(a) {
                tooltipElement.show();
            }

            //open popup after 1sec on mouseenter if not already open
            squareElement.bind('mouseenter', function (event) {
                if (!scope.square.showTooltip) {
                    showPromise = $timeout(showSquareTooltip, 1000);
                }
            });

            //dont show tooltip if pointer leaves before 1sec and hide if leave after 1sec
            squareElement.bind('mouseleave', function (event) {
                $timeout.cancel(showPromise);
                tooltipElement.hide();
            });
        },
        templateUrl: 'squareinfo.html',
        scope: {square: '=squareInfo'}
    }
});