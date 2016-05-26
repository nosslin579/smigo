function AccountController($anchorScroll, $scope, $http, $log, $location, $routeParams, UserService) {
    'use strict';
    //todo these values can me merged
    $scope.userBean = angular.copy(UserService.getState().currentUser);
    $scope.state = UserService.getState();

    $scope.passwordBean = {};
    $scope.updateUser = UserService.updateUser;
    $scope.changePassword = UserService.changePassword;
    $scope.enableUsernameInput = $routeParams.enableUsernameInput==='true';

    $scope.goTo = function (id) {
        $log.log('Scrolling to ', id);
        $location.hash(id);
        $anchorScroll();
    };

    $http.get('/locales', {cache: true}).then(function (resopnse) {
        $log.log('Locales retrieved', resopnse);
        $scope.locales = resopnse.data;
    });
}

angular.module('smigoModule').controller('AccountController', AccountController);