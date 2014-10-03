function RegisterController($scope, UserService) {
    $scope.viewModel = {
        register: true,
        usernameMin: 5,
        usernameMax: 40,
        usernamePattern: /^[\w]+$/,
        passwordMin: 6,
        pageMessageKey: 'msg.account.register'
    };

//     var newName = 'testreg' + Math.floor(Math.random() * 999999999);
//     $scope.passwordAgain = newName;
//     $scope.formModel = {
//     username: newName,
//     password: newName
//     };

    $scope.submitLoginOrRegisterForm = function (form, formModel) {
        UserService.register(form, formModel);
    }

    $scope.requestFeature = UserService.requestFeature;

}

angular.module('smigoModule').controller('RegisterController', RegisterController);