<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://smigo.org/jsp/functions" %>

<jsp:include page="header.jsp"/>


<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.19/angular.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.19/angular-route.js"></script>

<div ng-app="speciesModule">
    <div ng-view></div>

    <script type="text/ng-template" id="garden">
        <div ng-repeat="s in species">
            <a href="" ng-click="selectAction(s)">{{s.scientificName}} {{s | translate}}</a>
        </div>
        <a href="" ng-click="refresh()">Refresh</a>
    </script>
</div>

<script type="application/javascript">
    var app = angular.module('speciesModule', ['ngRoute']);

    app.config(function ($routeProvider) {
        $routeProvider.
                otherwise({
                    templateUrl: 'garden',
                    controller: 'GardenController'
                });
    });

    app.filter('translate', function () {
        var msg = <c:out escapeXml="false" value="${f:toJson(messages)}" />;
//        smigolog('msg', msg);
        return function (messageObject) {
//            smigolog('translating', messageObject);
            if (messageObject.messageKey) {
                return msg[messageObject.messageKey];
            }
            return msg[messageObject];
        };
    });

    app.controller('GardenController', function ($scope, $http) {
        $scope.selectAction = function (species) {
            smigolog('currentSpecies set to', species);
            $scope.currentSpecies = species;
        };

        $scope.species = ${f:toJson(species)};

        $scope.refresh = function () {
            smigolog("refresh");
            $http.get('species').success(function (response) {
                $scope.species = response;
                smigolog($scope.species);
            });
        };

        <%--$scope.plants = ${f:toJson(plants)}--%>

    });


</script>

<jsp:include page="footer.jsp"/>