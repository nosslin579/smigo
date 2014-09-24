function AccountController($scope, UserService, $http, $log) {

    $scope.userBean = angular.copy(UserService.getState().currentUser);

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
                return $http.get('rest/translation/' + userBean.locale)
                    .then(function (response) {
                        $scope.$emit('newLanguageAvailable', response.data);
                    });
            })
            .then(function () {
                $scope.updateSuccessful = true;
            })
            .catch(function (response) {
                $log.error('Update user failed', response);
                $scope.objectErrors = response.data;
            });
    };
}

angular.module('smigoModule').controller('AccountController', AccountController);