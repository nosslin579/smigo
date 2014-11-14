function SpeciesTooltip($log, $timeout, $window, SpeciesService) {
    return {
        restrict: 'A',
        scope: {},
        templateUrl: 'species-tooltip.html',
        link: function link(scope, element, attributes) {
            var showPromise;

            //using visibility because need height property
            element.css('visibility', 'hidden');
            scope.species = SpeciesService.getAllSpecies()[0];

            function close() {
                $timeout.cancel(showPromise);
                scope.$apply(function () {
                    element.css('visibility', 'hidden');
                });
            };

            element.parent().on('mouseenter', 'a', function (event) {
//                $log.log('Showing species tooltip: ' + event.originalEvent.type, [element, this, event]);
                if (this.dataset.speciesid) {
                    var id = +this.dataset.speciesid;
                    showPromise = $timeout(function () {
                        scope.species = SpeciesService.getSpecies(id);
                        $timeout(function () {
                            var ret, marginbottom = 35, marginTop = 50,
                                height = element.children().height(),
                                bottom = event.pageY + height / 2 + marginbottom,
                                top = event.pageY - height / 2 + marginTop;

                            if (top < 0) {//off screen at the top
                                ret = marginTop;
                            } else if (bottom > $window.innerHeight) {//off screen at the bottom
                                ret = $window.innerHeight - height - marginbottom;
                            } else {
                                ret = event.pageY - height / 2;
                            }
                            element.css('top', ret + 'px');
                            element.css('visibility', 'visible');
                        }, 0);
                    }, 500);
                }
            });

            element.on('mouseleave', close);
            element.on('click', '.close, .popover-content', close);
        }

    }
}
angular.module('smigoModule').directive('speciesTooltip', SpeciesTooltip);