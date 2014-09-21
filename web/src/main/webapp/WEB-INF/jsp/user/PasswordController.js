function PasswordController($scope, $http) {

    $scope.passwordBean = {};

    $scope.submitForm = function (form, passwordBean) {
//        console.log('Submit ', [form, passwordBean]);
        $scope.updateSuccessful = false;
        $scope.objectErrors = [];
        if (form.$invalid) {
//            console.log('Form is invalid', form);
            return;
        }

        $scope.passwordBean = {};
        $scope.verifyPassword = '';

        form.$setPristine();

        $http.post('change-password', passwordBean)
            .then(function (response) {
                console.log('Update password success', response);
                $scope.updateSuccessful = true;
            })
            .catch(function (response) {
                console.error('Update password failed', response);
                $scope.objectErrors = response.data;
            });
    };
}

angular.module('smigoModule').controller('PasswordController', PasswordController);