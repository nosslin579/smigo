function translateFilter($rootScope, $log, $http) {
    var msg = <c:out escapeXml="false" value="${f:toJson(messages)}" />;
    $log.log('TranslateFilter', [msg]);


    $rootScope.$on('newMessagesAvailable', function (event, messageKey, value) {
        $log.debug("Adding translation", [messageKey, value, msg]);
        msg[messageKey] = value;
    });

    $rootScope.$on('current-user-changed', function (event, user) {
        $http.get('/rest/translation')
            .then(function (response) {
                $log.debug('Messages reloaded', [msg, response.data]);
                msg = response.data;
                $rootScope.$broadcast('messages-reloaded', msg);
            });
    });

    /**
     * Param can be resolved from either message or messageParameter.
     */
    function getParameter(message, messageParameter) {
        if (message.messageParameter) {
            return message.messageParameter instanceof Array ? message.messageParameter : [message.messageParameter];
        } else if (messageParameter) {
            return [messageParameter];
        }
        return [];
    }

    return function (message, messageParameter) {
        if (!message) {
            $log.error('Can not translate', message, messageParameter);
            return 'n/a';
        }

        //resolve parameter
        var paramArray = getParameter(message, messageParameter),
            translatedMessage = message.messageKey ? msg[message.messageKey] : msg[message];

        if (!translatedMessage) {
            $log.error('Could not translate:', [msg, message, messageParameter]);
            return '-';
        }

        for (var i = 0; i < paramArray.length; i++) {
            var param = paramArray[i];
            translatedMessage = translatedMessage.replace('{' + i + '}', param.messageKey ? msg[param.messageKey] : param);
        }
//        $log.debug('Translate:'+translatedMessage, [msg, message, messageParameter]);
        return translatedMessage;
    };
}
angular.module('smigoModule').filter('translate', translateFilter);