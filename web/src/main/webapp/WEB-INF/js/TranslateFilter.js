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

    /**
     * Param can be resolved from either messageObject or parameterMessageObject.
     */
    function getParameter(messageObject, parameterMessageObject) {
        if (messageObject.messageKeyParameter) {
            return messageObject.messageKeyParameter;
        } else if (parameterMessageObject) {
            return parameterMessageObject;
        }
        return '';
    }

    $log.log('TranslateFilter', [msg]);

    return function (messageObject, parameterMessageObject) {
        if (!messageObject) {
            $log.error('Can not translate', messageObject, parameterMessageObject);
            return 'n/a';
        }

        //resolve parameter
        var param = getParameter(messageObject, parameterMessageObject);
        var translatedParam = param.messageKey ? msg[param.messageKey] : param
        var translatedMessage = messageObject.messageKey ? msg[messageObject.messageKey] : msg[messageObject];

        if (!translatedMessage) {
            $log.error('Could not translate:' + messageObject, parameterMessageObject);
            return '-';
        }
        return translatedMessage.replace('{0}', translatedParam);
    };
}
angular.module('smigoModule').filter('translate', translateFilter);