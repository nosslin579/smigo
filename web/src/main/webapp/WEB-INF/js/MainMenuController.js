function MainMenuController($scope, UserService, StateService) {

    $scope.logout = UserService.logout;
    $scope.userState = StateService.getUser();
    $scope.menuIsCollapsed = true;
}

angular.module('smigoModule').controller('MainMenuController', MainMenuController);