function RequestPasswordLinkController($http, $scope, $log) {
    $scope.resetBean = {};
//    $scope.resetBean = {email: 'user7389327855123@mailinator.com'};

    $scope.submitForm = function (form, resetBean) {
        $log.log('Submit ', [form, resetBean]);
        $scope.updateSuccessful = false;
        $scope.objectErrors = [];
        if (form.$invalid) {
            $log.log('Form is invalid', form);
            return;
        }

        $scope.email = resetBean.email;

        form.$setPristine();

        $http.post('request-password-link', resetBean)
            .then(function (response) {
                $log.log('Reset password success', response);
                $scope.updateSuccessful = true;
            })
            .catch(function (response) {
                $log.error('Reset password failed', response);
                $scope.objectErrors = response.data;
            });
    };
}

angular.module('smigoModule').controller('RequestPasswordLinkController', RequestPasswordLinkController);