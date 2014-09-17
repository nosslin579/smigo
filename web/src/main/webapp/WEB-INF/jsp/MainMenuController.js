function MainMenuController(UserService, $scope) {
    $scope.logout = UserService.logout;
}

angular.module('smigoModule').controller('MainMenuController', MainMenuController);