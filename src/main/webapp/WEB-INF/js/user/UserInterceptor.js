angular.module('smigoModule').factory('UserInterceptor', function UserInterceptor($log, $rootScope, $q) {

    return {
        responseError: function broadCastIfNotGet(response) {
            if (response.config.method !== 'GET') {
                $rootScope.$broadcast('response-received', response);
            }
            return $q.reject(response);
        },
        response: function broadCastIfNotGet(response) {
            //$log.info('responset', [response.headers('SmigoUser'), response.headers(), response]);
            if (response.config.method !== 'GET') {
                $rootScope.$broadcast('response-received', response);
            }
            return response;
        }
    }
});