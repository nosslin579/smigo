angular.module('smigoModule', ['ngRoute', 'ui.bootstrap', 'ngSanitize'])
    .config(function ($routeProvider, $logProvider, $provide, $locationProvider, $uibTooltipProvider, $httpProvider) {
        'use strict';

        $httpProvider.interceptors.push('UserInterceptor');

        $logProvider.debugEnabled(false);

        $locationProvider.html5Mode(true);

        $routeProvider.
            when('/', {
                templateUrl: '?hide-nav=true'
            }).
            when('/help', {
                templateUrl: 'help?hide-nav=true'
            }).
            when('/welcome-back', {
                templateUrl: 'views/welcome-back.html'
            }).
            when('/garden-planner-comparison', {
                templateUrl: 'garden-planner-comparison?hide-nav=true'
            }).
            when('/request-password-link', {
                templateUrl: 'views/request-password-link.html'
            }).
            when('/account', {
                templateUrl: 'views/account.html'
            }).
            when('/accept-terms-of-service', {
                templateUrl: 'views/accept-terms-of-service.html'
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
    .run(function ($http) {
        'use strict';

        //notify if any 3rd party library failed to load
        window.missingLibraries && $http.post('/rest/log/error', {
            message: 'Missing 3rd party libraries:' + JSON.stringify(window.missingLibraries)
        });

    })
    .value('isTouchDevice', !!('ontouchstart' in window));