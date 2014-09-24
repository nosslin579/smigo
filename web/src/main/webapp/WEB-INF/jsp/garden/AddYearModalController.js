function AddYearModalController($scope, $modalInstance, PlantService, $log) {

    $log.log('AddYearModalController');

    $scope.plantsState = PlantService.getState();

    $scope.addYear = function (year) {
        PlantService.addYear(year);
        $modalInstance.close(year);
    };

    $scope.close = function () {
        $modalInstance.dismiss('cancel');
    };


}

angular.module('smigoModule').controller('AddYearModalController', AddYearModalController);