function MainMenuController($log, $scope, $http, UserService) {
    'use strict';
    $scope.userState = UserService.getState();
    $scope.menuIsCollapsed = true;
    $scope.unreadComments = 0;

    $scope.$on('current-user-changed', function (event, newUser) {
        var username = newUser && newUser.username;
        $http.get('/rest/comment', {params: {receiver: username}}).then(function (response) {
            angular.forEach(response.data, function (comment) {
                comment.unread && $scope.unreadComments++;
            });
        });
    });

    $scope.$on('commentsRead', function (event, comments) {
        $log.log('commentsRead', event, comments);
        $scope.unreadComments = 0;
    });
}

angular.module('smigoModule').controller('MainMenuController', MainMenuController);