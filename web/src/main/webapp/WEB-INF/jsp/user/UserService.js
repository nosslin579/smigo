function UserService($rootScope, $http, $location, PlantService) {
    return {
        loginOrRegister: function (form, scope, action) {
            scope.objectErrors = [];
            if (form.$invalid) {
                console.log('Form is invalid:' + action);
                //set all values to trigger dirty, so validation messages become visible
                angular.forEach(form, function (value) {
                    if (value.$setViewValue) {
                        value.$setViewValue(value.$viewValue);
                    }
                });
                return;
            }

            scope.formModel['remember-me'] = true;

            PlantService.save().then(
                $http({
                    method: 'POST',
                    url: action,
                    data: $.param(scope.formModel),
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                }).success(function (data, status, headers, config) {
                    $rootScope.currentUser = scope.formModel.username;
                    PlantService.reloadGarden().then(function () {
                        console.log("Redirecting to garden");
                        $location.path('/garden');
                    });
                    console.log('Performed successfully action:' + action, [data, status, headers, config]);
                }).error(function (data, status, headers, config) {
                    scope.objectErrors = data;
                    delete $rootScope.currentUser;
                    console.log('Performed unsuccessfully action:' + action, [form, data, status, headers, config]);
                })
            );
        }};
}
angular.module('smigoModule').factory('UserService', UserService);