function MainMenuController(UserService, $scope) {
    $scope.logout = UserService.logout;
    $scope.userState = UserService.getState();
}

angular.module('smigoModule').controller('MainMenuController', MainMenuController);