function WallController($rootScope, $scope, $http, $log, $routeParams, GardenService, UserService, CommentService) {
    'use strict';
    $http.get('/rest/user/' + $routeParams.username).then(function (response) {
        $scope.hostUser = response.data;
    });

    $http.get('/rest/comment', {params: {receiver: $routeParams.username}}).then(function (response) {
        $scope.comments = response.data;
        if (UserService.isUser($routeParams.username)) {
            angular.forEach($scope.comments, function (comment) {
                if (comment.unread) {
                    comment.highlightNew = true;
                    comment.unread = false;
                    $http.put('/rest/comment', comment);
                }
            });
            $rootScope.$broadcast('commentsRead');
        }
    });

    $scope.garden = GardenService.getGarden($routeParams.username, true);
    $scope.comment = {};
    $scope.isOwnGarden = function () {
        return UserService.isUser($routeParams.username);
    };

    $scope.addComment = function (comment) {
        $scope.objectErrors = [];
        comment.unread = !UserService.isUser($routeParams.username);
        $http.post('/rest/comment', comment).then(function (response) {
            $log.info('A ok', response, $scope);
            $scope.comments.push({
                text: comment.text,
                id: response.data,
                submitter: UserService.getState().currentUser.username,
                receiver: comment.receiver,
                year: comment.year,
                unread: false,
                createdate: new Date().toDateString()
            });
            $scope.comment.text = '';
        }).catch(function (error) {
            $scope.objectErrors = error.data;
        });
    };

    $scope.deleteComment = function (comment) {
        $log.log('Deleting', comment);
        $http.delete('/rest/comment/' + comment.id).then(function (response) {
            $scope.comments.splice($scope.comments.indexOf(comment), 1);
        }).catch(function (error) {
            $scope.objectErrors = error.data;
        });
    };

}

angular.module('smigoModule').controller('WallController', WallController);