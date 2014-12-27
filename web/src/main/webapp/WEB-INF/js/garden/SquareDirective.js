angular.module('smigoModule').directive('soSquare', function SquareDirective($log, $timeout, SpeciesService, GardenService, isTouchDevice) {
    return {
        link: function link(scope, squareElement, attrs) {
            var showTooltipPromise,
                tooltipElement = squareElement.find('.square-tooltip'),
                squareContentElement = squareElement.find('.square-content');

//            $log.log('square info link', [scope, squareElement, attrs, isTouchDevice]);

            squareElement.css({
                top: scope.square.location.y * 48 + 100000 + 'px',
                left: scope.square.location.x * 48 + 100000 + 'px'
            });

            function onSquareClick(clickEvent) {
                $log.log('Square clicked', [clickEvent, scope.square, SpeciesService.getState().selectedSpecies, SpeciesService.getState().action]);
                if (clickEvent.shiftKey || SpeciesService.getState().action === 'delete') {
                    scope.square.removePlant();
                } else if (SpeciesService.getState().action === 'info') {
                    openSquareTooltip(clickEvent, 1);
                } else if (clickEvent.ctrlKey) {
                    $log.log('Copy species');
                } else if (SpeciesService.getState().action === 'add') {
                    scope.square.togglePlant(SpeciesService.getState().selectedSpecies);
                }
                clickEvent.stopPropagation();
                clickEvent.preventDefault();
            };

            //open popup after 1sec on mouseenter if not already open
            function openSquareTooltip(event, _delay) {
                var delay = _delay ? _delay : 1000;
                showTooltipPromise = $timeout(function showSquareTooltip() {
                    angular.forEach(scope.square.plantArray, function (plant) {
                        plant.hints = plant.getHints();
                    });
                    $timeout(function () {
                        var mousePositionInLeftDocumentHalf = (event.pageX > ($(document).width() + $('#species-frame').width()) / 2);
                        tooltipElement.css('left', mousePositionInLeftDocumentHalf ? -tooltipElement.width() - 24 : 72)

                        var mousePositionInLowerDocumentHalf = (event.pageY > ($(document).height() + 100) / 2);
                        tooltipElement.css('top', mousePositionInLowerDocumentHalf ? -tooltipElement.height() - 24 : 72)
                        tooltipElement.show();
                        $log.log('Showing square info', [scope.square, event, tooltipElement]);
                    }, 0);
                }, delay);
            }

            function closeSquareTooltip(event) {
                $timeout.cancel(showTooltipPromise);
                tooltipElement.hide();
                event.stopPropagation();
                event.preventDefault();
            }

            if (!isTouchDevice) {
                squareElement.on('mouseenter', openSquareTooltip);
                squareElement.on('mouseleave', closeSquareTooltip);
            }

            squareContentElement.on('click', onSquareClick);
            tooltipElement.on('click', closeSquareTooltip);

        },
        templateUrl: 'square.html',
        scope: {square: '=soSquare'}
    }
});