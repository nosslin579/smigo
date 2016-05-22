function AccountController($anchorScroll, $scope, $http, $log, $location, UserService) {
    'use strict';
    $scope.userBean = angular.copy(UserService.getState().currentUser);
    $scope.updateUser = UserService.updateUser;

    $scope.goTo = function (id) {
        $log.log('Scrolling to ', id);
        $location.hash(id);
        $anchorScroll();
    };

    $http.get('locales', {cache: true}).then(function (resopnse) {
        $log.log('Locales retrieved', resopnse);
        $scope.locales = resopnse.data;
    });
}

angular.module('smigoModule').controller('AccountController', AccountController);