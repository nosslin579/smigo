function WallController($scope, $http, $log, $routeParams, GardenService, UserService) {
    'use strict';
    $http.get('/rest/user/' + $routeParams.username).then(function (response) {
        $scope.hostUser = response.data;
    });


    reloadComments().then(function (comments) {
        if (UserService.isUser($routeParams.username)) {
            angular.forEach(comments, function (comment) {
                if (comment.unread) {
                    comment.highlightNew = true;
                    comment.unread = false;
                    $http.put('/rest/comment', comment);
                }
            });
        }
    });


    $scope.garden = GardenService.getGarden($routeParams.username, true);
    $scope.comment = {};

    $scope.addComment = function (comment) {
        $scope.objectErrors = [];
        $http.post('/rest/comment', comment).then(function (response) {
            $log.info('A ok', response, $scope);
            $scope.comment.text = '';
            reloadComments();
        }).catch(function (error) {
            $log.error('Not ok', error);
            $scope.objectErrors = error.data;
        });
    };

    $scope.deleteComment = function (comment) {
        $http.delete('/rest/comment/' + comment.id).then(function (response) {
            reloadComments();
        }).catch(function (error) {
            $scope.objectErrors = error.data;
        });
    };

    function reloadComments() {
        return $http.get('/rest/comment', {params: {receiver: $routeParams.username}}).then(function (response) {
            $scope.comments = response.data;
            return response.data;
        });

    }
}

angular.module('smigoModule').controller('WallController', WallController);