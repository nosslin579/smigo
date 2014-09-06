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
        UserService.login(form, $scope.formModel);
    }
}
angular.module('smigoModule').controller('LoginController', LoginController);