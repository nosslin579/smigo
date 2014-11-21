function SpeciesTooltip($log, $timeout, $window, SpeciesService, isTouchDevice) {
    return {
        restrict: 'A',
        scope: {},
        templateUrl: 'species-tooltip.html',
        link: function link(scope, tooltipElement, attributes) {
            var showPromise,
                speciesFrameElement = tooltipElement.parent();

            //using visibility because need height property
            tooltipElement.css('visibility', 'hidden');
            //avoid scope.species being undefied which may cause errors
            scope.species = SpeciesService.getAllSpecies()[0];

            function open(event) {
                event.preventDefault();
                if (!event.currentTarget.dataset.speciesid) {
                    return;
                }
                var id = +event.currentTarget.dataset.speciesid;
                scope.species = SpeciesService.getSpecies(id);
                $timeout(function () {
                    var ret, marginbottom = 35, marginTop = 50,
                        height = tooltipElement.children().height(),
                        bottom = event.pageY + height / 2 + marginbottom,
                        top = event.pageY - height / 2 + marginTop;

                    if (top < 0) {//off screen at the top
                        ret = marginTop;
                    } else if (bottom > $window.innerHeight) {//off screen at the bottom
                        ret = $window.innerHeight - height - marginbottom;
                    } else {
                        ret = event.pageY - height / 2;
                    }
                    tooltipElement.css('top', ret + 'px');
                    tooltipElement.css('visibility', 'visible');
                }, 0);
            }

            function cancel() {
                $timeout.cancel(showPromise);
            }

            function close(event) {
                event.preventDefault();
                event.stopPropagation();
                cancel();
                scope.$apply(function () {
                    tooltipElement.css('visibility', 'hidden');
                });
            };

            function delayedOpen(event) {
                $log.log('Showing species tooltip: ' + event.type, [tooltipElement, this, event]);
                showPromise = $timeout(function () {
                    open(event);
                }, 500);
            }

            function toggle(event) {
                var selected = $(event.currentTarget).parent().hasClass('active');
                if (selected && tooltipElement.css('visibility') === 'hidden') {
                    open(event);
                } else {
                    scope.$apply(function () {
                        tooltipElement.css('visibility', 'hidden');
                    });
                }
            }


            if (isTouchDevice) {
                /*Touch events*/
                speciesFrameElement.on('touchstart', 'a', toggle);
                tooltipElement.on('touchstart', '.close, .popover-content', close);
            } else {
                /*Mouse events*/
                speciesFrameElement.on('mouseenter', 'a', delayedOpen);
                tooltipElement.on('mouseleave', close);
                speciesFrameElement.on('mouseleave', 'a', cancel);
                tooltipElement.on('click', '.close, .popover-content', close);
            }
        }
    };
}
angular.module('smigoModule').directive('speciesTooltip', SpeciesTooltip);