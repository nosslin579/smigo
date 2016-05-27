function TranslateService($log, $http, $rootScope) {
    'use strict';
    var state = {
        allMessages: {}
    };

    $log.log('TranslateService', [state, $rootScope]);

    reloadMessages();

    $rootScope.$on('current-user-changed', function (event, newUser, oldUser, initialChange) {
        var newLocale = newUser && newUser.locale,
            oldLocale = oldUser && oldUser.locale;
        if (!initialChange && oldLocale !== newLocale) {
            reloadMessages();
        }
    });

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
            var ret = state.allMessages[messageKey];

            if (!ret) {
                if (Object.keys(state.allMessages).length > 0) {
                    $log.error('Could not translate:', [messageKey, messageParameter1, messageParameter2, state]);
                    state.allMessages[messageKey] = '-';
                    throw new ReferenceError('Could not translate: ' + messageKey);
                }
                return '';
            }

            if (!messageParameter1 && !messageParameter2) {
                return ret;
            }

            var paramArray = getParameter(messageParameter1, messageParameter2);

            //String interpolate params
            for (var i = 0; i < paramArray.length; i++) {
                var param = paramArray[i];
                ret = ret.replace(new RegExp('\\{' + i + '\\}', 'g'), param);
            }
            //$log.debug('Translate:' + translatedMessage, [msg, message, paramArray]);
            return ret;
        },
        getState: function () {
            return state;
        }
    };
}
angular.module('smigoModule').factory('TranslateService', TranslateService);