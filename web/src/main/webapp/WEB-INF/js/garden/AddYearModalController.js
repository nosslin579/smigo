function AddYearModalController($scope, $modalInstance, PlantService, $log) {

    $log.log('AddYearModalController', $scope, PlantService.nisse());

    var garden = PlantService.nisse();
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