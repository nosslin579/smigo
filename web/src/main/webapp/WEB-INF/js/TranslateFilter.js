function translateFilter($rootScope, $log, $http, $route) {
    var msg = initData.messages;
    $log.log('TranslateFilter', [msg]);


    $rootScope.$on('new-messages-available', function (event, messageKey, value) {
        $log.debug("Adding translation", [messageKey, value, msg]);
        msg[messageKey] = value;
    });

    $rootScope.$on('current-user-changed', function (event, user) {
        $http.get('/rest/translation')
            .then(function (response) {
                if (!angular.equals(msg, response.data)) {
                    $log.info('Messages reloaded and they differ, firing messages-reloaded', [msg, response.data]);
                    msg = response.data;
                    $rootScope.$broadcast('messages-reloaded', msg);
                    $route.reload();
                }
            });
    });

    /**
     * Param can be resolved from either message or messageParameter.
     */
    function getParameter(message, messageParameter) {
        if (!message.messageParameter && !messageParameter) {
            return [];
        }
        var param = message.messageParameter ? message.messageParameter : messageParameter;
        return param instanceof Array ? param : [param];
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
            translatedMessage = translatedMessage.replace(new RegExp('\\{' + i + '\\}', 'g'), param.messageKey ? msg[param.messageKey] : param);
        }
//        $log.debug('Translate:'+translatedMessage, [msg, message, messageParameter,paramArray]);
        return translatedMessage;
    };
}
angular.module('smigoModule').filter('translate', translateFilter);