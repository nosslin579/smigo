function rememberScroll($timeout) {
    return {
        restrict: 'A',
        link: function (scope, $elm, attr) {
            scope.$watch('model.selectedYear', function (newYear, oldYear, wtf) {
//                console.log('Watching model.selectedYear',[newYear,oldYear,wtf]);
                $timeout(function () {
                    $elm[0].scrollLeft = 99999;
                    $elm[0].scrollLeft = $elm[0].scrollLeft / 2;
                    $elm[0].scrollTop = 99999;
                    $elm[0].scrollTop = $elm[0].scrollTop / 2;
                }, 0, false);
            }, true);
        }
    }
}
angular.module('smigoModule').directive('rememberScroll', rememberScroll);