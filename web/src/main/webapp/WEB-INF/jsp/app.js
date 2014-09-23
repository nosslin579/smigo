"use strict";
angular.module('smigoModule', ['ngRoute'])
    .config(function ($routeProvider, $provide) {
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
            when('/accept-terms-of-service', {
                templateUrl: 'accept-terms-of-service.html',
                controller: 'AcceptTermsOfServiceController'
            }).
            otherwise({redirectTo: '/garden'});

        $provide.decorator("$exceptionHandler", ['$delegate', '$injector', function ($delegate, $injector) {
            return function (exception, cause) {
                $delegate(exception, cause);
                var referenceError = {
                    message: exception.message,
                    stack: exception.stack,
                    cause: cause
                };
                var $http = $injector.get("$http");
                $http.post('rest/log', referenceError);
            };
        }]);
    })
    .run(function ($rootScope) {
        console.log("App run");

        $rootScope.getObjectLength = function (obj) {
            return Object.keys(obj).length;
        };

        $rootScope.$on('current-user-changed', function (event, user) {
            console.log('Broadcast: current-user-changed', [event, user]);
        });

        $rootScope.$on('$locationChangeSuccess', function (param1, param2, param3) {
            console.log('Broadcast: $locationChangeSuccess', [param1, param2, param3]);
        });

        $rootScope.$on('$locationChangeStart', function (param1, param2, param3) {
            console.info('Broadcast: locationChangeStart', [param1, param2, param3]);
        });

        $rootScope.$on('$routeChangeStart', function (param1, param2, param3) {
            console.info('Broadcast: routeChangeStart', [param1, param2, param3]);
        });

        $rootScope.$on('newMessagesAvailable', function (param1, param2, param3) {
            console.info('Broadcast: newMessagesAvailable', [param1, param2, param3]);
        });

    });