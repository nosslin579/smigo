angular.module('smigoModule').directive('soVernacular', function vernacularDirective($log, VernacularService) {
    'use strict';
    return {
        link: function (scope, element, attrs) {
            var speciesIdAsInt = +attrs.soVernacular;
            //$log.log('VernacularDirective link ' + speciesIdAsInt, [scope, element, attrs]);
            element.text(VernacularService.getVernacular(speciesIdAsInt).vernacularName);
            scope.$on('vernaculars-changed', function (event, vernaculars) {
                //$log.info('VernacularDirective update', [event, vernaculars]);
                element.text(VernacularService.getVernacular(speciesIdAsInt).vernacularName);
            });
        }
    }
});