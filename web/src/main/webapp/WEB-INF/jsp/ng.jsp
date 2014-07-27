<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="msg" uri="http://smigo.org/jsp/functions" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<jsp:include page="header.jsp"/>


<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.19/angular.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.19/angular-route.js"></script>

<div ng-app="speciesModule">
    <div ng-view></div>

    <script type="text/ng-template" id="species.html">
        <div ng-repeat="s in species">
            <a href="" ng-click="selectAction(s)">{{s.scientificName}} {{s | translate}}</a>
        </div>
    </script>
</div>

<script>
    var app = angular.module('speciesModule', ['ngRoute']);

    app.config(function ($routeProvider) {
        $routeProvider.
                when('/', {
                    templateUrl: 'species.html',
                    controller: 'SpeciesController'
                }).
                otherwise({
                    redirectTo: '/'
                });
    });

    app.filter('translate', function () {
        var msg = <c:out escapeXml="false" value="${msg:toJson(messages)}" />;
        smigolog('msg', msg);
        return function (messageObject) {
            smigolog('translating', messageObject);
            if (messageObject.messageKey) {
                return msg[messageObject.messageKey];
            }
            return msg[messageObject];
        };
    });

    app.controller('SpeciesController', function ($scope, $http) {
        $scope.selectAction = function (species) {
            smigolog('currentSpecies set to', species);
            $scope.currentSpecies = species;
        };

        $scope.species = ${msg:toJson(species)};

//        $http.get('species').success(function (response) {
//            smigolog(response);
//            $scope.species = response;
//        });
    });


</script>

<jsp:include page="footer.jsp"/>