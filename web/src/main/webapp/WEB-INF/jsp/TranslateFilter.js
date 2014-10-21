function translateFilter($rootScope, $log, $http) {
    var msg = <c:out escapeXml="false" value="${f:toJson(messages)}" />;

    $rootScope.$on('newMessagesAvailable', function (event, messageKey, value) {
        msg[messageKey] = value;
    });

    $rootScope.$on('locale-changed', function (event, locale) {
        return $http.get('/rest/translation/' + locale)
            .then(function (response) {
                msg = response.data;
            });
    });

    $log.log('TranslateFilter', [msg]);
    return function (messageObject, param) {
        if (!messageObject) {
            $log.error('Can not translate', messageObject);
            return 'n/a';
        }
        if (messageObject.messageKey) {
            return msg[messageObject.messageKey];
        }
        if (!msg[messageObject]) {
            $log.error('Could not translate:' + messageObject);
            return messageObject;
        }
        return msg[messageObject].replace('{0}', param);
    };
}
angular.module('smigoModule').filter('translate', translateFilter);