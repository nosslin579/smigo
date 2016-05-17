function PasswordController($scope, $http, $log) {
    'use strict';
    $scope.passwordBean = {};

    $scope.submitForm = function (form, passwordBean) {
//        $log.log('Submit ', [form, passwordBean]);
        $scope.updateSuccessful = false;
        form.pendingSave = true;
        $scope.objectErrors = [];
        if (form.$invalid) {
//            $log.log('Form is invalid', form);
            return;
        }

        $scope.passwordBean = {};
        $scope.verifyPassword = '';

        form.$setPristine();

        $http.post('change-password', passwordBean)
            .then(function (response) {
                $log.log('Update password success', response);
                $scope.updateSuccessful = true;
                form.pendingSave = false;
            })
            .catch(function (response) {
                $log.error('Update password failed', response);
                $scope.objectErrors = response.data;
                form.pendingSave = false;
            });
    };
}

angular.module('smigoModule').controller('PasswordController', PasswordController);