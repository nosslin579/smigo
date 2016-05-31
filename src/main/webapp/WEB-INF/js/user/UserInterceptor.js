angular.module('smigoModule').factory('UserInterceptor', function UserInterceptor($log, $rootScope) {

    function broadCastIfNotGet(response) {
        //$log.info('responset', [response.headers('SmigoUser'), response.headers(), response]);
        if (response.config.method !== 'GET') {
            $rootScope.$broadcast('response-received', response);
        }
        return response;
    }

    return {
        responseError: broadCastIfNotGet,
        response: broadCastIfNotGet
    }
});