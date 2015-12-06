function ResourceLoadService($http, $window, $timeout, $rootScope, $q, $log) {

    $window.fbAsyncInit = function () {
        $log.debug('Init facebook plugin');
        FB.init({
            appId: '1486322734976014',
            status: false,
            cookie: true,
            version: 'v2.1',
            xfbml: false
        });
    };

    function load(url, id) {
        var s = 'script';
        $log.debug('Retrieving javascript ', [url, id, document.getElementsByTagName(s)]);
        var js, fjs = document.getElementsByTagName(s)[0];
        if (document.getElementById(id)) return;
        js = document.createElement(s);
        js.id = id;
        js.src = url;
        fjs.parentNode.insertBefore(js, fjs);
    }

    return {
        facebook: function () {
            load('//connect.facebook.net/en/sdk.js', 'facebook-jssdk');
        },
        twitter: function () {
            load('//platform.twitter.com/widgets.js', 'twitter-widgets');
        }
    }

}
angular.module('smigoModule').factory('ResourceLoadService', ResourceLoadService);