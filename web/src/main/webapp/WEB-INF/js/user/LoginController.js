function LoginController($scope, UserService) {
    $scope.viewModel = {
        login: true,
        usernameMin: 0,
        usernameMax: 999,
        usernamePattern: /.+/,
        passwordMin: 0,
        pageMessageKey: 'account.login'
    };
    $scope.formModel = {
        username: '',
        password: ''
    };

    $scope.requestFeature = UserService.requestFeature;
    $scope.submitLoginOrRegisterForm = UserService.login;

}
angular.module('smigoModule').controller('LoginController', LoginController);