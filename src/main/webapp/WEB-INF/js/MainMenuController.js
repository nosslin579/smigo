function MainMenuController($log, $scope, UserService) {
    'use strict';
    $scope.userState = UserService.getState();
    $scope.menuIsCollapsed = true;
}

angular.module('smigoModule').controller('MainMenuController', MainMenuController);