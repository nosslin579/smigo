function AddYearModalController($scope, $modalInstance, StateService, $log) {

    $log.log('AddYearModalController', $scope);

    var garden = StateService.getGarden();
    $scope.forwardYear = garden.getAvailableYears().last() + 1;
    $scope.backwardYear = garden.getAvailableYears()[0] - 1;

    $scope.addYear = function (year) {
        garden.addYear(year);
        $modalInstance.close(year);
    };

    $scope.close = function () {
        $modalInstance.dismiss('cancel');
    };


}

angular.module('smigoModule').controller('AddYearModalController', AddYearModalController);