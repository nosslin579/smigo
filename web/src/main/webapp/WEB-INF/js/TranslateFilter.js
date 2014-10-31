function translateFilter($rootScope, $log, $http) {
    var msg = <c:out escapeXml="false" value="${f:toJson(messages)}" />;
    $log.log('TranslateFilter', [msg]);


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
     * Param can be resolved from either message or messageParameter.
     */
    function getParameter(message, messageParameter) {
        if (message.messageParameter) {
            return message.messageParameter;
        } else if (messageParameter) {
            return messageParameter;
        }
        return '';
    }

    return function (message, messageParameter) {
        if (!message) {
            $log.error('Can not translate', message, messageParameter);
            return 'n/a';
        }

        //resolve parameter
        var param = getParameter(message, messageParameter),
            translatedParam = param.messageKey ? msg[param.messageKey] : param,
            translatedMessage = message.messageKey ? msg[message.messageKey] : msg[message];

        if (!translatedMessage) {
            $log.error('Could not translate:' + message, messageParameter);
            return '-';
        }
        return translatedMessage.replace('{0}', translatedParam);
    };
}
angular.module('smigoModule').filter('translate', translateFilter);