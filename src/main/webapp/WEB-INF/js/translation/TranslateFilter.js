function translateFilter(TranslateService) {
    'use strict';
    return function (message, messageParameter1, messageParameter2) {
        return TranslateService.translate(message, messageParameter1, messageParameter2);
    };
}
angular.module('smigoModule').filter('translate', translateFilter);