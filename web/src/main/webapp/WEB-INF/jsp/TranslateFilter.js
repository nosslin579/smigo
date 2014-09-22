function translateFilter($rootScope) {
    var msg = <c:out escapeXml="false" value="${f:toJson(messages)}" />;

    $rootScope.$on('newMessagesAvailable', function (event, messageKey, value) {
        msg[messageKey] = value;
    });

    $rootScope.$on('newLanguageAvailable', function (event, messages) {
        msg = messages;
    });

    console.log('TranslateFilter', [msg]);
    return function (messageObject, param) {
        if (!messageObject) {
            console.error('Can not translate', messageObject);
            return 'n/a';
        }
        if (messageObject.messageKey) {
            return msg[messageObject.messageKey];
        }
        if (!msg[messageObject]) {
            console.error('Could not translate:' + messageObject);
            return messageObject;
        }
        return msg[messageObject].replace('{0}', param);
    };
}
angular.module('smigoModule').filter('translate', translateFilter);