function AcceptTermsOfServiceController($scope, UserService, StateService) {
    $scope.acceptTermsOfService = UserService.acceptTermsOfService;
    $scope.logout = UserService.logout;
    $scope.user = StateService.getUser();


}
angular.module('smigoModule').controller('AcceptTermsOfServiceController', AcceptTermsOfServiceController);