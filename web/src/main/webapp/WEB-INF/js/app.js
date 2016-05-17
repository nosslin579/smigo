angular.module('smigoModule', ['ngRoute', 'ui.bootstrap', 'ngSanitize'])
    .config(function ($routeProvider, $logProvider, $provide, $locationProvider, $uibTooltipProvider) {
        'use strict';
        $logProvider.debugEnabled(false);

        $locationProvider.html5Mode(true);

        $routeProvider.
            when('/request-password-link', {
                templateUrl: 'views/request-password-link.html'
            }).
            when('/account', {
                templateUrl: 'views/account.html'
            }).
            when('/login', {
                templateUrl: 'views/login.html',
                controller: 'LoginController'
            }).
            when('/register', {
                templateUrl: 'views/login.html',
                controller: 'RegisterController'
            }).
            when('/garden-planner', {
                templateUrl: 'views/garden-planner.html',
                controller: 'GardenController'
            }).
            when('/gardener/:username', {
                templateUrl: 'views/wall.html',
                controller: 'WallController'
            }).
            when('/forum', {
                templateUrl: 'views/forum.html',
                controller: 'ForumController'
            }).
            when('/wall/:username', {redirectTo: '/gardener/:username'}).
            when('/beta', {redirectTo: '/'}).
            when('/garden', {redirectTo: 'garden-planner'}).
            //facebook adds #_=_ to URL after login
            when('/_=_', {redirectTo: '/garden-planner'}).
            otherwise({templateUrl: 'views/404.html'});

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

        $uibTooltipProvider.setTriggers({'click': 'blur'});
    })
    .run(function ($rootScope, $log, isTouchDevice) {
        'use strict';
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