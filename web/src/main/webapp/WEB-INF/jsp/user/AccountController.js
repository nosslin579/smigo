function AccountController($scope, UserService, $http) {

    $scope.userBean = angular.copy(UserService.getState().currentUser);

    $http.get('locales').then(function (resopnse) {
        console.log('Locales retrieved', resopnse);
        $scope.locales = resopnse.data;
    });

    $scope.submitAccountDetailsForm = function (form, userBean) {
        console.log('Submit ', [form, userBean]);
        $scope.updateSuccessful = false;
        $scope.objectErrors = [];
        if (form.$invalid) {
            console.log('Form is invalid', form);
            return;
        }
        UserService.updateUser(userBean)
            .then(function () {
                $scope.updateSuccessful = true;
            })
            .catch(function (response) {
                console.error('Update user failed', response);
                $scope.objectErrors = response.data;
            });
    };

    $scope.submitPasswordForm = function (form, passwordBean) {
        console.log('Submit ', [form, passwordBean]);
        $scope.passwordUpdateSuccessful = false;
        $scope.objectErrors = [];
        if (form.$invalid) {
            console.log('Form is invalid', form);
            return;
        }
        UserService.updateUser(userBean)
            .then(function () {
                $scope.updateSuccessful = true;
            })
            .catch(function (response) {
                console.error('Update user failed', response);
                $scope.objectErrors = response.data;
            });
    };
}

angular.module('smigoModule').controller('AccountController', AccountController);