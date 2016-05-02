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
     * Merge parameters to an array since they can be either string or array of string.
     */
    function getParameter(messageParameter1, messageParameter2) {
        //$log.log('getParameter', [messageParameter1, messageParameter2]);

        //convert to array
        var ret = [].concat(messageParameter1, messageParameter2);
        //removes undefined and null elements
        return ret.filter(function (n) {
            return n != undefined;
        });
    }

    return function (message, messageParameter1, messageParameter2) {
        if (!message || typeof message !== 'string') {
            $log.error('Can not translate', [message, messageParameter1, messageParameter2]);
            return 'n/a';
        }

        var paramArray = getParameter(messageParameter1, messageParameter2),
            translatedMessage = allMessages[message];

        if (!translatedMessage) {
            $log.error('Could not translate(missing key):', [allMessages, message, messageParameter1, messageParameter2]);
            return '-';
        }

        //String interpolate params
        for (var i = 0; i < paramArray.length; i++) {
            var param = paramArray[i];
            translatedMessage = translatedMessage.replace(new RegExp('\\{' + i + '\\}', 'g'), param);
        }
        //$log.debug('Translate:' + translatedMessage, [msg, message, paramArray]);
        return translatedMessage;
    };
}
angular.module('smigoModule').filter('translate', translateFilter);