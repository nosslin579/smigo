function SpeciesTooltip($log, $timeout, SpeciesService) {
    return {
        restrict: 'A',
        scope: {},
        templateUrl: 'species-tooltip.html',
        link: function link(scope, element, attributes) {

            var showPromise;

            element.parent().on('mouseenter', 'a', function (elem) {
                $log.log('mouseenter', elem, this);
                if (this.dataset.speciesid) {
                    var id = +this.dataset.speciesid;
                    showPromise = $timeout(function () {
                        scope.species = SpeciesService.getSpecies(id);
                        scope.showSpeciesTooltip = true;
                    }, 500);
                }
            });

            element.parent().on('mouseleave', 'a', function () {
                $timeout.cancel(showPromise);
                scope.$apply(function () {
                    scope.showSpeciesTooltip = false;
                });
            });
        }

    }
}
angular.module('smigoModule').directive('speciesTooltip', SpeciesTooltip);