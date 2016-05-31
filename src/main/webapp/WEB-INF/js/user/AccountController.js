function AccountController($anchorScroll, $scope, $http, $log, $location, $routeParams, UserService) {
    'use strict';

    $scope.$watch(function () {
        return UserService.getState().currentUser;
    }, function (newVal) {
        $scope.userBean = angular.copy(newVal);
    });

    $scope.passwordBean = {};
    $scope.updateUser = UserService.updateUser;
    $scope.changePassword = UserService.changePassword;
    $scope.logout= UserService.logout;
    $scope.enableUsernameInput = $routeParams.enableUsernameInput === 'true';


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