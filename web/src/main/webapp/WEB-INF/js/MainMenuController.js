function MainMenuController(UserService, $scope) {
    $scope.logout = UserService.logout;
    $scope.userState = UserService.getState();
    $scope.menuIsCollapsed = true;
}

angular.module('smigoModule').controller('MainMenuController', MainMenuController);