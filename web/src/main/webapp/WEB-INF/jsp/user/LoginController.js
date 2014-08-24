function LoginController($scope, UserService) {
    $scope.viewModel = {
        login: true,
        usernameMin: 0,
        usernameMax: 999,
        usernamePattern: /.+/,
        passwordMin: 0,
        pageMessageKey: 'account.login'
    };
//    $scope.formModel = {
//        username: 'user7389327855123',
//        password: 'testreg17'
//    };
    $scope.submitLoginOrRegisterForm = function (form) {
        console.log('loginOrRegister', [form, $scope]);
        UserService.loginOrRegister(form, $scope, 'login');
    }
}
angular.module('smigoModule').controller('LoginController', LoginController);