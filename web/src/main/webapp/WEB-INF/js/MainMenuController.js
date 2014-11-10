function MainMenuController($log, $scope, $timeout, UserService, StateService) {

    $scope.$on('messages-reloaded', function () {
        $scope.uglyHackToReloadMenu = true;
        $timeout(function () {
            $scope.uglyHackToReloadMenu = false;
        }, 0);
    });

    $scope.logout = UserService.logout;
    $scope.userState = StateService.getUser();
    $scope.menuIsCollapsed = true;
}

angular.module('smigoModule').controller('MainMenuController', MainMenuController);