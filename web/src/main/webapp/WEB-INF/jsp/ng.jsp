<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://smigo.org/jsp/functions" %>

<jsp:include page="header.jsp"/>


<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.19/angular.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.19/angular-route.js"></script>

<style>
    .square {
        background-color: green;
        width: 3em;
        height: 3em;
        position: absolute;
    }

    #grid {
        position: relative;
        background-color: grey;
        width: 10040em;
        height: 10020em;
        margin-left: -9960em;
        margin-top: -9980em;
    }

    #peephole {
        width: 60em;
        height: 30em;
        overflow: scroll;
        margin: 3em;
    }

</style>

<div ng-app="speciesModule">
    <div ng-view></div>

    <script type="text/ng-template" id="garden">
        <div id="asdf"></div>
        <div>
            <a ng-repeat="year in garden.years" href="" ng-click="selectYear(year)">{{year}}</a>
        </div>

        <div>
            <div id="peephole">
                <div id="grid" style="padding: 0" ng-click="addPlant($event)" ng-style="gridSize(squares)">
                    <div ng-repeat="s in squares | expandGrid" class="square" ng-style="squarePostion(s)">
                        <div ng-repeat="p in s.plants">
                            <%--{{p.species.messageKey | translate}}--%>{{'x:'+p.x + ', y:' + p.y}}
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div>
            <div ng-repeat="s in species">
                <a href="" ng-click="selectAction(s)">{{s.scientificName}} {{s | translate}}</a>
            </div>
            <a href="" ng-click="refresh()">Refresh</a>
        </div>
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
        return function (messageObject) {
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

        $scope.selectYear = function (year) {
            smigolog('year set to', year);
            $scope.currentYear = year;
            $scope.squares = $scope.garden.squares[year];
        };

        $scope.addPlant = function (clickEvent) {
            console.log("addplant", [clickEvent, this]);
/*
            var parentOffset = $(this).parent().offset();
            $scope.squares.push({
                x: 5,
                y: 5,
                year: $scope.currentYear,
                plants: [
                    {
                        speciesId: $scope.currentSpecies.id,
                        species: $scope.currentSpecies,
                        x: 5,
                        y: 5,
                        year: $scope.currentYear
                    }
                ]
            });
            */
        };

        $scope.gridSize = function (squares) {
            var xmax = -9999, ymax = -99999, xmin =9999, ymin = 9999;
            angular.forEach(squares, function (square, index) {
                xmax = Math.max(square.x, xmax);
                ymax = Math.max(square.y, ymax);
                xmin = Math.min(square.x, xmin);
                ymin = Math.min(square.y, ymin);
            });
            return {
                    'margin-top': (- 10000 + 3 + Math.abs(ymin) * 3) + 'em',
                    'width': (10000 + 6 + Math.abs(xmax) * 3) + 'em',
                    'height': (10000 + 6 + Math.abs(ymax) * 3) + 'em',
                    'margin-left': (-10000 + 3 + Math.abs(xmin) * 3) + 'em'
                    };
        };

        $scope.squarePostion =function (square) {
            smigolog('squarePostion',square);
            return {
                top: square.y * 3 + 10000 + 'em',
                left: square.x * 3 + 10000 + 'em'
            };
        };

        $scope.species = ${f:toJson(species)};
        $scope.garden = ${f:toJson(garden)};
        smigolog("garden", $scope.garden);

        $scope.refresh = function () {
            smigolog("refresh");
            $http.get('species').success(function (response) {
                $scope.species = response;
                smigolog($scope.species);
            });
        };

        $scope.selectAction($scope.species["1"]);
    });


</script>

<jsp:include page="footer.jsp"/>