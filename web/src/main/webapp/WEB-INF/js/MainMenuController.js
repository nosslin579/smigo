function MainMenuController($log, $scope, $timeout, UserService) {

    $scope.$on('messages-reloaded', function () {
        $scope.uglyHackToReloadMenu = true;
        $timeout(function () {
            $scope.uglyHackToReloadMenu = false;
        }, 0);
    });

    $scope.userState = UserService.getState();
    $scope.menuIsCollapsed = true;
}

angular.module('smigoModule').controller('MainMenuController', MainMenuController);