function translateFilter($rootScope, $log, $http, $route) {
    var allMessages = initData.messages;
    $log.log('TranslateFilter', [allMessages]);


    $rootScope.$on('new-messages-available', function (event, messageKey, value) {
        $log.debug("Adding translation", [messageKey, value, allMessages]);
        allMessages[messageKey] = value;
    });

    $rootScope.$on('current-user-changed', function (event, user) {
        $http.get('/rest/translation')
            .then(function (response) {
                if (!angular.equals(allMessages, response.data)) {
                    $log.info('Messages reloaded and they differ, firing messages-reloaded', [allMessages, response.data]);
                    allMessages = response.data;
                    $rootScope.$broadcast('messages-reloaded', allMessages);
                    $route.reload();
                }
            });
    });

    /**
     * Param can be resolved from either message or messageParameter.
     */
    function getParameter(message, messageParameter1, messageParameter2) {
        //$log.log('getParameter', [message, messageParameter1, messageParameter2]);
        var ret = [].concat(message.messageParameter, messageParameter1, messageParameter2);
        return ret.filter(function (n) {
            return n != undefined; //removes undefined and null elements
            //}).map(function (n) {
            //    return typeof n === 'function' ? n() : n;//if any parameter is a function, execute it and use return value
        });
    }

    return function (message, messageParameter1, messageParameter2) {
        if (!message) {
            $log.error('Can not translate', message, messageParameter1, messageParameter2);
            return 'n/a';
        } else if (typeof message === 'function') {
            return message();
        }

        var paramArray = getParameter(message, messageParameter1, messageParameter2),
            translatedMessage = message.messageKey ? allMessages[message.messageKey] : allMessages[message];

        if (!translatedMessage) {
            $log.error('Could not translate:', [allMessages, message, messageParameter1, messageParameter2]);
            return '-';
        }

        //String interpolate params
        for (var i = 0; i < paramArray.length; i++) {
            var param = paramArray[i];
            translatedMessage = translatedMessage.replace(new RegExp('\\{' + i + '\\}', 'g'), param.messageKey ? allMessages[param.messageKey] : param);
        }
        //$log.debug('Translate:' + translatedMessage, [msg, message, paramArray]);
        return translatedMessage;
    };
}
angular.module('smigoModule').filter('translate', translateFilter);