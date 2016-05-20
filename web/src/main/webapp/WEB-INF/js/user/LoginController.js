function LoginController($scope, UserService, TranslateService) {
    'use strict';
    $scope.viewModel = {
        login: true,
        usernameMin: 0,
        usernameMax: 999,
        usernamePattern: /.+/,
        passwordMin: 0,
        pageMessageKey: 'account.login',
        usernameTitle: TranslateService.translate('username') + ' / ' + TranslateService.translate('email')
    };
    $scope.formModel = {
        username: '',
        password: ''
    };
    $scope.formModel['remember-me'] = true;

    $scope.requestFeature = UserService.requestFeature;
    $scope.submitLoginOrRegisterForm = UserService.login;

}
angular.module('smigoModule').controller('LoginController', LoginController);