function WallController($scope, $http, $log, $routeParams, GardenService) {
    'use strict';
    $http.get('/rest/user/' + $routeParams.username).then(function (response) {
        $scope.hostUser = response.data;
    });

    $http.get('/rest/comment', {params: {receiver: $routeParams.username}}).then(function (response) {
        $scope.comments = response.data;
    });

    $scope.garden = GardenService.getGarden($routeParams.username, true);
    $scope.comment = {};

    $scope.addComment = function (comment) {
        $scope.objectErrors = [];
        $http.post('/rest/comment', comment).then(function (response) {
            $log.info('A ok', response, $scope);
            $scope.comment.text = '';
            $http.get('/rest/comment', {params: {receiver: $routeParams.username}}).then(function (response) {
                $scope.comments = response.data;
            });
        }).catch(function (error) {
            $log.error('Not ok', error);
            $scope.objectErrors = error.data;
        });
    };
}

angular.module('smigoModule').controller('WallController', WallController);