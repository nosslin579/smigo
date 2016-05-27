function UserService($log, $http, $rootScope, $q, $location) {
    'use strict';
    var state = {currentUser: null};

    $log.log('UserService', [$http.defaults.headers, state]);

    $rootScope.$on('$routeChangeStart', function (angularEvent, next, current) {
        $log.log('$routeChangeStart', [angularEvent, next, current]);
        //when user tries to avoid accepting terms of service page
        if (state.currentUser && !state.currentUser.termsOfService) {
            $location.url('/accept-terms-of-service');
        }
    });

    $http.get('/rest/user').then(function (response) {
        var newUser = response.data;
        setUser(null, newUser);
        //on page load, check that user has accepted terms of service
        if (newUser && !newUser.termsOfService) {
            $location.url('/accept-terms-of-service');
        }
    });

    $rootScope.$on('current-user-changed', setUser);

    function setUser(event, newUser) {
        if (newUser) {
            state.currentUser = newUser;
            $http.defaults.headers.common.SmigoUser = newUser.username;
        } else {
            state.currentUser = null;
            delete $http.defaults.headers.common.SmigoUser;
        }
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
                $location.path('/garden-planner');
                $rootScope.$broadcast('current-user-changed', response.data);
            })
            .catch(function (errorReason) {
                $log.log('Login failed, reason:', errorReason);
                form.objectErrors = errorReason.data;
                form.processing = false;
            });
    }

    return {
        changePassword: function (form, passwordBean) {
            $log.log('changePassword', [form, passwordBean]);
            form.updateSuccessful = false;
            form.pendingSave = true;
            form.objectErrors = [];
            if (form.$invalid) {
                $log.warn('Form is invalid', form);
                form.pendingSave = false;
                return;
            }

            form.$setPristine();

            $http.post('/change-password', passwordBean).then(function (response) {
                $log.log('Update password success', response);
                form.updateSuccessful = true;
                form.pendingSave = false;
            }).catch(function (response) {
                $log.error('Update password failed', response);
                form.objectErrors = response.data;
                form.pendingSave = false;
            }).finally(function () {
                passwordBean.oldPassword = '';
                passwordBean.newPassword = '';
                passwordBean.verifyPassword = '';
            });
        },
        register: function (form, formModel) {
            validateForm(form)
                .then(function () {
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
        updateUser: function updateUser(form, userBean, redirectOnSuccess) {
            $log.log('Update user', [form, userBean]);
            form.updateSuccessful = false;
            form.objectErrors = [];
            if (form.$invalid) {
                $log.log('Form is invalid', form);
                return;
            }

            form.pendingSave = true;
            return $http.put('/rest/user', userBean).then(function (response) {
                $log.info('Update user success', [userBean, response]);
                form.pendingSave = false;
                form.updateSuccessful = true;
                $rootScope.$broadcast('current-user-changed', userBean);
                redirectOnSuccess && $location.url(redirectOnSuccess);
            }).catch(function (response) {
                $log.error('Update user failed', response);
                form.objectErrors = response.data;
                form.pendingSave = false;
                form.updateSuccessful = false;
            });

        },
        requestFeature: function (feature) {
            $http.post('/rest/log/feature', {feature: feature});
            alert("This service is not yet available. Please try again later.");
        },
        getState: function () {
            return state;
        }
    };
}
angular.module('smigoModule').factory('UserService', UserService);