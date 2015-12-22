function ForumController($scope, $http, $log, UserService) {

    $scope.message = {};
    $scope.pagination = {
        page: 0,
        size: 20
    };
    $scope.pendingGetMore = true;
    $scope.user = UserService.getState();

    $http.get('/rest/message', {params: $scope.pagination})
        .then(function (response) {
            $scope.messages = response.data;
            $scope.pendingGetMore = false;
            $scope.hasMoreMessages = $scope.messages.length === $scope.pagination.size;
        });

    $scope.getMoreMessages = function getMoreMessages(messages, pagination) {
        $log.log(this);
        $scope.pendingGetMore = true;
        pagination.page++;
        $http.get('/rest/message', {params: pagination})
            .then(function (response) {
                messages.push.apply(messages, response.data);
                $scope.pendingGetMore = false;
                $scope.hasMoreMessages = response.data === pagination.size;
            });
    };

    $scope.addMessage = function (form, message) {
        form.pendingAddMessage = true;
        $http.post('/rest/message', message)
            .then(function (response) {
                message.id = response.data;
                message.submitter = UserService.getState().currentUser.username;
                message.createdate = "now";
                $scope.messages.unshift(message);
                $scope.message = {};
                $log.log('Messages is now:', $scope.messages);
                form.pendingAddMessage = false;
            });
    }
}

angular.module('smigoModule').controller('ForumController', ForumController);