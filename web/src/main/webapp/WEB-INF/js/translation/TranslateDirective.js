angular.module('smigoModule').directive('soMsg', function TranslateDirective($log, TranslateService) {
    return {
        link: function (scope, element, attrs) {
            $log.log('TranslateDirective link', [scope, element, attrs]);
            element.text(TranslateService.translate(attrs.soMsg));
            scope.$on('get-translation-success', function (event, allMessages) {
                //$log.info('TranslateDirective get-translation-success', [scope, element, attrs, allMessages]);
                element.text(TranslateService.translate(attrs.soMsg));
            });
        }
    }
});