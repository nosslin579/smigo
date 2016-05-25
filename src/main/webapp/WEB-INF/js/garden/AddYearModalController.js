function AddYearModalController($scope, $log, garden) {
    'use strict';
    $log.log('AddYearModalController', $scope);

    var last = garden.getAvailableYears().smigoLast();
    $scope.forwardYear = garden.yearSquareMap[last].length !== 0 ? (last + 1) : false;

    var first = garden.getAvailableYears()[0];
    $scope.backwardYear = garden.yearSquareMap[first].length !== 0 ? (first - 1) : false;
}
angular.module('smigoModule').controller('AddYearModalController', AddYearModalController);