function AccountController($scope, $http, $log, StateService, UserService) {

    $scope.userBean = angular.copy(StateService.getUser().currentUser);

    $http.get('locales').then(function (resopnse) {
        $log.log('Locales retrieved', resopnse);
        $scope.locales = resopnse.data;
    });

    $scope.submitAccountDetailsForm = function (form, userBean) {
        $log.log('Submit ', [form, userBean]);
        $scope.updateSuccessful = false;
        $scope.objectErrors = [];
        if (form.$invalid) {
            $log.log('Form is invalid', form);
            return;
        }
        UserService.updateUser(userBean)
            .then(function () {
                $scope.updateSuccessful = true;
                $scope.$emit('current-user-changed', userBean);
            })
            .catch(function (response) {
                $log.error('Update user failed', response);
                $scope.objectErrors = response.data;
            });
    };
}

angular.module('smigoModule').controller('AccountController', AccountController);