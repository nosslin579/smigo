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
    .run(function ($rootScope) {
        console.log("App run");
        $rootScope.getObjectLength = function (obj) {
            return Object.keys(obj).length;
        };
    });