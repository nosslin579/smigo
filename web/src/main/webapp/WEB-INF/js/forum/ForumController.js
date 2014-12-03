function ForumController($scope, $http, $log, StateService, UserService) {

    $http.get('/rest/message')
        .then(function (response) {
            $scope.messages = response.data;
        });

    $scope.addMessage = function (form, message) {
        $http.post('/rest/message', message)
            .then(function (response) {
                message.id = response.data;
                message.submitter = StateService.getUser().currentUser.username;
                message.createdate = new Date();
                $scope.messages.push(message);
                $log.log('Messages is now:', $scope.messages)
            });
    }

    $scope.message = {}

}

angular.module('smigoModule').controller('ForumController', ForumController);