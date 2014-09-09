function AcceptTermsOfServiceController($scope, UserService) {
    $scope.acceptTermsOfService = UserService.acceptTermsOfService;
}
angular.module('smigoModule').controller('AcceptTermsOfServiceController', AcceptTermsOfServiceController);