<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://smigo.org/jsp/functions" %>

<!DOCTYPE html>
<html>
<head>
    <title><spring:message code="${msgTitle}" arguments="${titleArg}"/></title>
    <meta name="description" content="<spring:message code="${msgDescription}" arguments="${descriptionArg}"/>">

    <jsp:include page="head-common.jsp"/>

    <base href="/">

    <script src="http://code.jquery.com/jquery-2.1.3.min.js"></script>
    <script>window.jQuery || document.write('<script src="/static/jquery-2.1.3.min.js">\x3C/script>')</script>

    <%--bootstrap and styling--%>
    <%--<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">--%>
    <%--<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">--%>
    <%--<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootswatch/3.2.0/flatly/bootstrap.min.css">--%>
    <%--<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootswatch/3.2.0/sandstone/bootstrap.min.css">--%>

    <%--angular--%>
    <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.3.3/angular.min.js"></script>
    <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.3.3/angular-route.min.js"></script>
    <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.3.3/angular-touch.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/0.12.0/ui-bootstrap-tpls.min.js"></script>

</head>

<body ng-app="smigoModule">

<%@ include file="../views/nav-top.html" %>

<div ng-view class="angular-view"></div>

<script type="application/javascript">

    <%@ include file="../js/Customization.js" %>
    (function () {

        var initData = {
            user: <c:out escapeXml="false" value="${f:toJson(user)}"/>,
            species: <c:out escapeXml="false" value="${f:toJson(species)}"/>,
            rules: <c:out escapeXml="false" value="${f:toJson(rules)}"/>,
            plantDataArray:<c:out escapeXml="false" value="${f:toJson(plantData)}"/>
        };

        <%@ include file="../js/app.js" %>
        <%@ include file="../js/garden/SpeciesFilter.js" %>
        <%@ include file="../js/garden/GardenController.js" %>
        <%@ include file="../js/garden/AddYearModalController.js" %>
        <%@ include file="../js/garden/SpeciesService.js" %>
        <%@ include file="../js/garden/GardenService.js" %>
        <%@ include file="../js/garden/SpeciesTooltipDirective.js" %>
        <%@ include file="../js/garden/SquareDirective.js" %>
        <%@ include file="../js/garden/GridDirective.js" %>

        <%@ include file="../js/species/SpeciesController.js" %>
        <%@ include file="../js/species/RuleController.js" %>

        <%@ include file="../js/user/RegisterController.js" %>
        <%@ include file="../js/user/LoginController.js" %>
        <%@ include file="../js/user/AccountController.js" %>
        <%@ include file="../js/user/PasswordController.js" %>
        <%@ include file="../js/user/UserService.js" %>
        <%@ include file="../js/user/RequestPasswordLinkController.js" %>

        <%@ include file="../js/forum/ForumController.js" %>

        <%@ include file="../js/wall/WallController.js" %>
        <%@ include file="../js/wall/WallService.js" %>

        <%@ include file="../js/social/FacebookDirective.js" %>
        <%@ include file="../js/social/TwitterDirective.js" %>

        <%@ include file="../js/MainMenuController.js" %>
        <%@ include file="../js/StateService.js" %>

        <%@ include file="../js/EqualsDirective.js" %>
        <%@ include file="../js/TranslateFilter.js" %>
    })();
</script>

<%--########## ng views ##############--%>
<script type="text/ng-template" id="garden-planner.html">
    <%@ include file="../views/garden-planner.html" %>
</script>
<script type="text/ng-template" id="login.html">
    <%@ include file="../views/login.html" %>
</script>
<script type="text/ng-template" id="request-password-link.html">
    <%@ include file="../views/request-password-link.html" %>
</script>
<script type="text/ng-template" id="species-tooltip.html">
    <%@ include file="../views/species-tooltip.html" %>
</script>
<script type="text/ng-template" id="square.html">
    <%@ include file="../views/square.html" %>
</script>
<script type="text/ng-template" id="wall.html">
    <%@ include file="../views/wall.html" %>
</script>
<script type="text/ng-template" id="grid.html">
    <%@ include file="../views/grid.html" %>
</script>
<script type="text/ng-template" id="hasta-luego.html">
    <%@ include file="../views/hasta-luego.html" %>
</script>
<script type="text/ng-template" id="footer.html" charset="UTF-8">
    <jsp:include page="nav-bottom.jsp"/>
</script>

</body>
</html>