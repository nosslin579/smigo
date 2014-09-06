function Http($http) {
    return {
        post: function (url, data) {
            var params = $.param(data);
            return $http({
                method: 'POST',
                url: url,
                data: params,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            });
        }
    }
}

angular.module('smigoModule').factory('Http', Http);