function Http($http) {
    return {
        post: function (url, data) {
            var params = data && $.param(data);
            return $http({
                method: 'POST',
                url: url,
                data: params,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            });
        },
        get: $http.get,
        put: $http.put
    }
}

angular.module('smigoModule').factory('Http', Http);