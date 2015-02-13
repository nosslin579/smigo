"use strict";
angular.module('smigoModule', ['ngRoute', 'ui.bootstrap', 'ngTouch'])
    .config(function ($routeProvider, $logProvider, $provide, $locationProvider, $tooltipProvider) {
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
            when('/garden', {
                templateUrl: 'garden.html',
                controller: 'GardenController'
            }).
            when('/', {
                templateUrl: 'home.html',
            }).
            when('/wall/:username', {
                templateUrl: 'wall.html',
                controller: 'WallController'
            }).
            when('/forum', {
                templateUrl: 'forum.html',
                controller: 'ForumController'
            }).
            when('/species/:id', {
                templateUrl: 'species.html',
                controller: 'SpeciesController'
            }).
            when('/rule/:id', {
                templateUrl: 'rule.html',
                controller: 'RuleController'
            }).
            when('/beta', {redirectTo: '/'}).
            when('/_=_', {redirectTo: '/garden'}).
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

        $tooltipProvider.setTriggers({'click': 'blur'});
    })
    .run(function ($rootScope, $log, isTouchDevice) {
        $log.log("App run. isTouchDevice:" + isTouchDevice, initData);

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

    })
    .value('isTouchDevice', !!('ontouchstart' in window));