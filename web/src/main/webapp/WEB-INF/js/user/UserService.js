function UserService($log, $http, $timeout, $rootScope, $q, $location) {

    var state = {
            currentUser: initData.user,
            locales: initData.locales,
            connection: true
        },
        pingCounter = 0;
    $timeout(pingServer, 480000, false);

    $log.log('UserService', state);

    $rootScope.$on("$routeChangeStart", function (event, next, current) {
        if (state.currentUser && !state.currentUser.termsOfService) {
            $log.log('User has not accepted terms of service', state);
            if (next.templateUrl == "accept-terms-of-service.html") {
                // already going to #accept, no redirect needed
            } else {
                // not going to #accept, we should redirect now
                $location.path("/accept-terms-of-service");
            }
        }
    });


    $rootScope.$on('current-user-changed', function (event, user) {
        state.currentUser = user;
    });

    function reloadCurrentUser() {
        return $http.get('/rest/user')
            .then(function (resonse) {
                var currentUser = resonse.data ? resonse.data : null;
                $rootScope.$broadcast('current-user-changed', currentUser);
            });
    }

    function pingServer() {
        $http.get('ping')
            .then(function (response) {
                $log.debug("Ping success nr:" + pingCounter, response);
                var username = state.currentUser ? state.currentUser.username : undefined;
                if (username !== response.data.name) {
                    $log.log('Username mismatch detected', [state, response]);
                    reloadCurrentUser();
                }
                state.connection = true;
            })
            .catch(function (response) {
                $log.warn("Ping fail", response);
                state.connection = false;
            });
        if (pingCounter++ < 100) {
            $timeout(pingServer, 960000);
        }
    }

    function updateUser(userBean) {
        return $http.put('/rest/user', userBean)
            .then(function (response) {
                $log.info('Update user success', [userBean, response]);
                if (state.currentUser.locale != userBean.locale) {
                    $rootScope.$broadcast('current-user-changed', userBean);
                }
                state.currentUser = userBean;
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
                $log.log('Login success, response:', response);
            })
            .then(reloadCurrentUser)
            .then(function () {
                $location.path('/garden');
            }).catch(function (errorReason) {
                $log.log('Login failed, reason:', errorReason);
                form.objectErrors = errorReason.data;
                form.processing = false;
            });
    }

    return {
        getState: function () {
            return state;
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
        acceptTermsOfService: function () {
            $log.log('Handle acceptTermsOfService');
            state.currentUser.termsOfService = true;
            updateUser(state.currentUser);
            $location.path('/garden');
        },
        logout: function () {
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