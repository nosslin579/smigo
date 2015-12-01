function AddYearModalController($scope, $uibModalInstance, StateService, $log) {

    $log.log('AddYearModalController', $scope);

    var garden = StateService.getGarden();

    var last = garden.getAvailableYears().last();
    $scope.forwardYear = garden.yearSquareMap[last].length !== 0 ? (last + 1) : false;

    var first = garden.getAvailableYears()[0];
    $scope.backwardYear = garden.yearSquareMap[first].length !== 0 ? (first - 1) : false;

    $scope.addYear = function (year) {
        garden.addYear(year);
        $uibModalInstance.close(year);
    };

    $scope.close = function () {
        $uibModalInstance.dismiss('cancel');
    };


}

angular.module('smigoModule').controller('AddYearModalController', AddYearModalController);