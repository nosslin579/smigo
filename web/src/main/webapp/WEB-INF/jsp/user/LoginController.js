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
//        password: 'user7389327855123'
//    };
    $scope.requestFeature = UserService.requestFeature;

    $scope.submitLoginOrRegisterForm = function (form, formModel) {
        UserService.login(form, formModel);
    }
}
angular.module('smigoModule').controller('LoginController', LoginController);