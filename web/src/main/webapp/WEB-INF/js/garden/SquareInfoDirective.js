angular.module('smigoModule').directive('squareInfo', function ($log, $timeout, GardenService) {
    return {
        link: function link(scope, tooltipElement, attrs) {
            var showPromise,
                squareElement = tooltipElement.parent();

//            $log.log('square info link', [scope, tooltipElement, attrs]);

            //open popup after 1sec on mouseenter if not already open
            squareElement.bind('mouseenter', function (event) {
                if (!scope.square.showTooltip) {
                    showPromise = $timeout(function showSquareTooltip() {
                            angular.forEach(scope.square.plantArray, function (plant) {
                                plant.hints = plant.getHints();
                            });
                            $timeout(function () {
                                var mousePositionInLeftDocumentHalf = (event.pageX > ($(document).width() + $('#species-frame').width()) / 2);
                                tooltipElement.css('left', mousePositionInLeftDocumentHalf ? -tooltipElement.width() - 24 : 72)

                                var mousePositionInLowerDocumentHalf = (event.pageY > ($(document).height() + 100) / 2);
                                tooltipElement.css('top', mousePositionInLowerDocumentHalf ? -tooltipElement.height() - 24 : 72)
                                tooltipElement.show();
                                $log.log('Showing square info', [scope.square]);
                            }, 0);
                        }
                        , 1000);
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