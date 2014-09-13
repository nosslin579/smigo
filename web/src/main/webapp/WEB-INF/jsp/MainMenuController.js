function MainMenuController(InitService, UserService, $scope) {
    $scope.logout = UserService.logout;
    $scope.currentUser = InitService.user;

    $scope.$on('current-user-changed', function (event, user) {
        $scope.currentUser = user;
    });
}

angular.module('smigoModule').controller('MainMenuController', MainMenuController);