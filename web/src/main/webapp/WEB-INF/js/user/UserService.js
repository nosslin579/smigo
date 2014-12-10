function UserService($log, $http, $timeout, $rootScope, $q, $location, $route) {

    $log.log('UserService');

    function updateUser(userBean) {
        return $http.put('/rest/user', userBean)
            .then(function (response) {
                $log.info('Update user success', [userBean, response]);
            });
    }

    function validateForm(form, skipValidate) {
        form.objectErrors = [];
        form.submitted = true;
        form.processing = true;
        var deferred = $q.defer();
        if (form.$invalid && !skipValidate) {
            $log.debug('Form is invalid', form);
            deferred.reject('Form is invalid');
        } else {
            deferred.resolve();
        }
        return deferred.promise;
    }

    function login(form, formModel) {
//        $log.log('Login', [form, formModel]);
//        http://stackoverflow.com/questions/14965968/angularjs-browser-autofill-workaround-by-using-a-directive
        if (!formModel.username) {
            $log.warn('Possible autocomplete detected, formModel contains no username.');
            formModel.username = $('#username').val();
        }
        if (!formModel.password) {
            $log.warn('Possible autocomplete detected, formModel contains no password.');
            formModel.password = $('#password').val();
        }
        return validateForm(form, true)
            .then(function () {
                return $http({
                    method: 'POST',
                    url: 'login',
                    data: $.param(formModel),
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                });
            })
            .then(function (response) {
                return $http.get('/rest/user');
            })
            .then(function (response) {
                $location.path('/garden');
                $rootScope.$broadcast('current-user-changed', response.data);
            })
            .catch(function (errorReason) {
                $log.log('Login failed, reason:', errorReason);
                form.objectErrors = errorReason.data;
                form.processing = false;
            });
    }

    return {
        register: function (form, formModel, widgetId) {
            validateForm(form)
                .then(function () {
                    formModel.reCaptcha = grecaptcha.getResponse(widgetId);
                    $log.log('Registering');
                    return $http.post('/rest/user', formModel);
                })
                .then(function () {
                    return login(form, formModel);
                }).catch(function (errorReason) {
                    $log.log('Login failed', errorReason);
                    form.objectErrors = errorReason.data;
                    form.processing = false;
                });
        },
        login: login,
        acceptTermsOfService: function (user) {
            $log.log('Handle acceptTermsOfService');
            user.termsOfService = true;
            updateUser(user);
            $location.path('/garden');
        },
        logout: function () {
            $rootScope.$broadcast('user-logout');
            $http.get('logout')
                .then(function () {
                    $rootScope.$broadcast('current-user-changed', null);
                    $location.path('/hasta-luego');
                })
                .catch(function (data, status, headers, config) {
                    $log.error('Logout failed', data, status, headers, config);
                });
        },
        updateUser: updateUser,
        requestFeature: function (feature) {
            $http.post('/rest/log/feature', {feature: feature});
            alert("This service is not yet available. Please try again later.");
        }
    };
}
angular.module('smigoModule').factory('UserService', UserService);