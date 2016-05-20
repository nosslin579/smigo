function RegisterController($scope, UserService, TranslateService) {
    'use strict';
    $scope.viewModel = {
        register: true,
        usernameMin: 5,
        usernameMax: 40,
        usernamePattern: /^[\w]+$/,
        passwordMin: 6,
        pageMessageKey: 'msg.account.register',
        usernameTitle: TranslateService.translate('username')
    };

//     var newName = 'testreg' + Math.floor(Math.random() * 999999999);
//     $scope.passwordAgain = newName;
//     $scope.formModel = {
//     username: newName,
//     password: newName
//     };

    $scope.submitLoginOrRegisterForm = UserService.register;
    $scope.requestFeature = UserService.requestFeature;
}

angular.module('smigoModule').controller('RegisterController', RegisterController);