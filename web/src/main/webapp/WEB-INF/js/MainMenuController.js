function MainMenuController($log, $scope, UserService) {

    $scope.userState = UserService.getState();
    $scope.menuIsCollapsed = true;
}

angular.module('smigoModule').controller('MainMenuController', MainMenuController);