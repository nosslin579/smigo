function RequestPasswordLinkController($http, $scope) {
    $scope.resetBean = {};
    $scope.resetBean = {email: 'user7389327855123@mailinator.com'};

    $scope.submitForm = function (form, resetBean) {
        console.log('Submit ', [form, resetBean]);
        $scope.updateSuccessful = false;
        $scope.objectErrors = [];
        if (form.$invalid) {
            console.log('Form is invalid', form);
            return;
        }

        $scope.email = resetBean.email;
        $scope.resetBean = {};

        form.$setPristine();

        $http.post('request-password-link', resetBean)
            .then(function (response) {
                console.log('Reset password success', response);
                $scope.updateSuccessful = true;
            })
            .catch(function (response) {
                console.error('Reset password failed', response);
                $scope.objectErrors = response.data;
            });
    };
}

angular.module('smigoModule').controller('RequestPasswordLinkController', RequestPasswordLinkController);