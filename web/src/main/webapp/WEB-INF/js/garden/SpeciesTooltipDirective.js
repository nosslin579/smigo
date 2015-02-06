function SpeciesTooltip($log, $timeout, $window, $location, SpeciesService, isTouchDevice) {
    return {
        restrict: 'A',
        scope: {},
        templateUrl: 'species-tooltip.html',
        link: function link(scope, tooltipElement, attributes) {
            var showPromise,
                gardenPageElement = tooltipElement.parent();

            //using visibility because need height property
            tooltipElement.css('visibility', 'hidden');
            //avoid scope.species being undefied which may cause errors
            scope.species = SpeciesService.getAllSpecies()[0];

            function open(event) {
                event.preventDefault();

                var id = +event.currentTarget.getAttribute('data-speciesid'); //dataset.speciesid doesn't work on IE10

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
                gardenPageElement.on('touchstart', 'a.select-species-link', toggle);
                tooltipElement.on('touchstart', '.close', close);
            } else {
                /*Mouse events*/
                gardenPageElement.on('mouseenter', 'a.select-species-link', delayedOpen);
                tooltipElement.on('mouseleave', close);
                gardenPageElement.on('mouseleave', 'a.select-species-link', cancel);
                tooltipElement.on('click', '.close', close);
            }
        }
    };
}
angular.module('smigoModule').directive('speciesTooltip', SpeciesTooltip);