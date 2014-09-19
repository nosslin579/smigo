function AcceptTermsOfServiceController($scope, UserService) {
    $scope.acceptTermsOfService = UserService.acceptTermsOfService;
    $scope.logout = UserService.logout;
}
angular.module('smigoModule').controller('AcceptTermsOfServiceController', AcceptTermsOfServiceController);