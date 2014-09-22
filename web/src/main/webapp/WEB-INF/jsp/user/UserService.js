function UserService($http, $timeout, $rootScope, $q, $location, Http, PlantService) {

    var state = {
        currentUser: initData.user,
        locales: initData.locales,
        connection: true
    };
    $timeout(pingServer, 120000, false);

    console.log('UserService', state);

    $rootScope.$on("$routeChangeStart", function (event, next, current) {
        if (state.currentUser && !state.currentUser.termsOfService) {
            console.log('User has not accepted terms of service', state);
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
        return $http.get('rest/user')
            .then(function (resonse) {
                var currentUser = resonse.data ? resonse.data : null;
                $rootScope.$broadcast('current-user-changed', currentUser);
            });
    }

    function pingServer() {
        $http.get('ping')
            .then(function (response) {
                console.log("Ping success", response);
                var username = state.currentUser ? state.currentUser.username : undefined;
                if (username !== response.data.name) {
                    console.log('Username mismatch detected', [state, response]);
                    reloadCurrentUser();
                }
                state.connection = true;
            })
            .catch(function (response) {
                console.warn("Ping fail", response);
                state.connection = false;
            });
        $timeout(pingServer, 120000);
    }

    function updateUser(userBean) {
        return Http.put('rest/user', userBean)
            .then(function (response) {
                console.log('Update user success', response);
                state.currentUser = userBean;
            });
    }

    function validateForm(form) {
        form.objectErrors = [];
        var deferred = $q.defer();
        if (form.$invalid) {
//            console.log('Form is invalid', form);
            deferred.reject('Form is invalid');
        } else {
            deferred.resolve();
        }
        return deferred.promise;
    }

    function login(form, formModel) {
        console.log('Login');
        formModel['remember-me'] = true;
        return validateForm(form)
            .then(function () {
                return Http.post('login', formModel);
            })
            .then(function (response) {
                console.log('Login success, response:', response);
            })
            .then(reloadCurrentUser)
            .then(function () {
                $location.path('/garden');
            }).catch(function (errorReason) {
                console.log('Login failed, reason:', errorReason);
                form.objectErrors = errorReason.data;
            });
    }

    return {
        getState: function () {
            return state;
        },
        register: function (form, formModel) {
            validateForm(form)
                .then(PlantService.save)
                .then(function () {
                    console.log('Registering');
                    return Http.post('register', formModel);
                })
                .then(function () {
                    return login(form, formModel);
                }).catch(function (errorReason) {
                    console.log('Login failed', errorReason);
                    form.objectErrors = errorReason.data;
                });
        },
        login: login,
        acceptTermsOfService: function () {
            console.log('Handle acceptTermsOfService');
            state.currentUser.termsOfService = true;
            updateUser(state.currentUser);
            $location.path('/garden');
        },
        logout: function () {
            PlantService.save()
                .then(function () {
                    return Http.get('logout');
                })
                .then(function () {
                    $rootScope.$broadcast('current-user-changed', null);
                    $location.path('/hasta-luego');
                })
                .catch(function (data, status, headers, config) {
                    console.error('Logout failed', data, status, headers, config);
                });
        },
        updateUser: updateUser
    };
}
angular.module('smigoModule').factory('UserService', UserService);