angular.module('smigoModule').directive('soMsg', function TranslateDirective($log, TranslateService) {
    'use strict';
    return {
        link: function (scope, element, attrs) {
            //$log.log('TranslateDirective link', [scope, element, attrs]);
            var original = element.text();
            element.text(original + TranslateService.translate(attrs.soMsg));
            scope.$on('get-translation-success', function (event, allMessages) {
                //$log.info('TranslateDirective get-translation-success', [scope, element, attrs, allMessages]);
                element.text(original + TranslateService.translate(attrs.soMsg));
            });
        }
    }
});

angular.module('smigoModule').directive('soMsgAttr', function AttributeTranslateDirective($log, TranslateService) {
    'use strict';
    return {
        link: function (scope, element, attrs) {
            var split = attrs.soMsgAttr.split('=');
            attrs.$set(split[0], TranslateService.translate(split[1]));
            scope.$on('get-translation-success', function (event, allMessages) {
                //$log.info('AttributeTranslateDirective get-translation-success ' + attrs.soMsg, [scope, element, attrs, allMessages]);
                var split = attrs.soMsgAttr.split('=');
                attrs.$set(split[0], TranslateService.translate(split[1]));
            });
        }
    };
});

angular.module('smigoModule').directive('soMsgScope', function ScopeTranslateDirective($log, TranslateService) {
    'use strict';
    function addTranslationsToScope(scope, attrs) {
        scope.msg = scope.msg || {};
        attrs.soMsgScope.split(',').forEach(function (key) {
            scope.msg[key] = TranslateService.translate(key);
        });
    }

    return {
        link: function (scope, element, attrs) {
            //$log.info('ScopeTranslateDirective ' + attrs.soMsgScope, [scope, element, attrs]);
            addTranslationsToScope(scope, attrs);
            scope.$on('get-translation-success', function (event, allMessages) {
                addTranslationsToScope(scope, attrs);
            });
        }
    };
});