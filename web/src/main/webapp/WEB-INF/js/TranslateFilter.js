function translateFilter($rootScope, $log, $http) {
    var msg = <c:out escapeXml="false" value="${f:toJson(messages)}" />;


    $rootScope.$on('newMessagesAvailable', function (event, messageKey, value) {
//        $log.debug("Adding translation", [messageKey,value,msg]);
        msg[messageKey] = value;
    });

    $rootScope.$on('current-user-changed', function (event, user) {
        $http.get('/rest/translation')
            .then(function (response) {
                msg = response.data;
            });
    });

    $log.log('TranslateFilter', [msg]);
    return function (messageObject, param) {
        if (!messageObject) {
            $log.error('Can not translate', messageObject, param);
            return 'n/a';
        }
        var translatedMessage = messageObject.messageKey ? msg[messageObject.messageKey] : msg[messageObject];

        if (!translatedMessage) {
            $log.error('Could not translate:' + messageObject, param);
            return '-';
        }

        if (param && param.messageKey) {
            return translatedMessage.replace('{0}', msg[param.messageKey]);
        }
        return translatedMessage.replace('{0}', param);
    };
}
angular.module('smigoModule').filter('translate', translateFilter);