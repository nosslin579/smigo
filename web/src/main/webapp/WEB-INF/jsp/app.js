"use strict";
angular.module('smigoModule', ['ngRoute'])
    .config(function ($routeProvider) {
        $routeProvider.
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
            otherwise({redirectTo: '/garden'});

    })
    .run(function ($rootScope, $timeout) {
        console.log("App run");

        /*
         var $oldDigest = $rootScope.$digest;

         var $newDigest = function () {
         console.time("$digest");
         $oldDigest.apply($rootScope);
         console.timeEnd("$digest");
         };
         $rootScope.$digest = $newDigest;
         */

        $rootScope.getObjectLength = function (obj) {
            return Object.keys(obj).length;
        };

        $rootScope.$on('current-user-changed', function (event, user) {
            console.log('Broadcast: current-user-changed', [event, user]);
        });

        $rootScope.$on('$locationChangeSuccess', function (param1, param2) {
            console.log('Broadcast: $locationChangeSuccess', [param1, param2]);
        });

        $rootScope.$on('newMessagesAvailable', function (param1, param2) {
            console.info('Broadcast: newMessagesAvailable', [param1, param2]);
        });

    });