function TranslateService($log, $http, $rootScope) {
    'use strict';
    var state = {
        allMessages: {}
    };

    $log.log('TranslateService', [state, $rootScope]);

    reloadMessages();

    $rootScope.$on('current-user-changed', reloadMessages);

    function reloadMessages() {
        return $http.get('/rest/translation').then(function (response) {
            $log.info('Messages reloaded', [state, response]);
            angular.extend(state.allMessages, response.data);
            $rootScope.$broadcast('get-translation-success', state.allMessages);
        });
    }

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

    return {
        translate: function (messageKey, messageParameter1, messageParameter2) {
            if (!messageParameter1) {
                return state.allMessages[messageKey];
            }

            if (!messageKey || typeof messageKey !== 'string') {
                $log.error('Can not translate(invalid message key)', [messageKey, messageParameter1, messageParameter2]);
                return 'n/a';
            }

            var paramArray = getParameter(messageParameter1, messageParameter2),
                translatedMessage = state.allMessages[messageKey];

            if (!translatedMessage) {
                if (Object.keys(state.allMessages).length > 0) {
                    $log.error('Could not translate(missing key):', [messageKey, messageParameter1, messageParameter2, state]);
                }
                return '-';
            }

            //String interpolate params
            for (var i = 0; i < paramArray.length; i++) {
                var param = paramArray[i];
                translatedMessage = translatedMessage.replace(new RegExp('\\{' + i + '\\}', 'g'), param);
            }
            //$log.debug('Translate:' + translatedMessage, [msg, message, paramArray]);
            return translatedMessage;
        },
        getState: function () {
            return state;
        }
    };
}
angular.module('smigoModule').factory('TranslateService', TranslateService);