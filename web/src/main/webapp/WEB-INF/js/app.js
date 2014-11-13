"use strict";
angular.module('smigoModule', ['ngRoute', 'ui.bootstrap'])
    .config(function ($routeProvider, $logProvider, $provide, $locationProvider) {
        $logProvider.debugEnabled(false);

        $locationProvider.html5Mode(true);

        $routeProvider.
            when('/request-password-link', {
                templateUrl: 'request-password-link.html'
            }).
            when('/account', {
                templateUrl: 'account.html'
            }).
            when('/hasta-luego', {
                templateUrl: 'hasta-luego.html'
            }).
            when('/help', {
                templateUrl: 'help.html'
            }).
            when('/login', {
                templateUrl: 'login.html',
                controller: 'LoginController'
            }).
            when('/register', {
                templateUrl: 'login.html',
                controller: 'RegisterController'
            }).
            when('/', {
                templateUrl: 'garden.html',
                controller: 'GardenController'
            }).
            when('/wall/:username', {
                templateUrl: 'wall.html',
                controller: 'WallController'
            }).
            when('/accept-terms-of-service', {
                templateUrl: 'accept-terms-of-service.html',
                controller: 'AcceptTermsOfServiceController'
            }).
            when('/garden', {redirectTo: '/'}).
            when('/beta', {redirectTo: '/'}).
            otherwise({templateUrl: '404.html'});

        $provide.decorator("$exceptionHandler", ['$delegate', '$injector', function ($delegate, $injector) {
            var x = 0, previousStacks = [];

            return function (exception, cause) {
                if (x++ > 30) {
                    throw "To many errors";
                }

                $delegate(exception, cause);

                if (previousStacks.indexOf(exception.stack) != -1) {
                    //Already sent this error to server.
                    return;
                }
                previousStacks.push(exception.stack);
                var referenceError = {
                    message: exception.message,
                    stack: exception.stack,
                    cause: cause
                };
                var $http = $injector.get("$http");
                $http.post('/rest/log/error', referenceError);
            };
        }]);
    })
    .run(function ($rootScope, $log) {
        $log.log("App run", initData);

        /*
         $rootScope.$on('current-user-changed', function (event, user) {
         $log.log('Broadcast: current-user-changed', [event, user]);
         });

         $rootScope.$on('$locationChangeSuccess', function (param1, param2, param3) {
         $log.log('Broadcast: $locationChangeSuccess', [param1, param2, param3]);
         });

         $rootScope.$on('$locationChangeStart', function (param1, param2, param3) {
         $log.info('Broadcast: locationChangeStart', [param1, param2, param3]);
         });

         $rootScope.$on('$routeChangeStart', function (param1, param2, param3) {
         $log.info('Broadcast: routeChangeStart', [param1, param2, param3]);
         });

         $rootScope.$on('newMessagesAvailable', function (param1, param2, param3) {
         $log.info('Broadcast: newMessagesAvailable', [param1, param2, param3]);
         });
         */

    });