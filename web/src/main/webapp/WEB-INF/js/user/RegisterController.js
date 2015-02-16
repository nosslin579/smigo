function RegisterController($timeout, $scope, $location, $http, UserService) {
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

    $scope.submitLoginOrRegisterForm = UserService.register;
    $scope.requestFeature = UserService.requestFeature;


    //todo this should be a directive
    $timeout(angular.noop, 1000)
        .then(function () {
            return $http.get('/rpc/showcaptcha/');
        })
        .then(function renderCaptch(response) {
            if (response.data && $location.path() === '/register') {
                $scope.widgetId = grecaptcha.render('recaptcha', {
                    sitekey: '6LeO6_4SAAAAACgz20mK-j47nP8wJULuMci06Cej'
                });
            }
        });
}

angular.module('smigoModule').controller('RegisterController', RegisterController);