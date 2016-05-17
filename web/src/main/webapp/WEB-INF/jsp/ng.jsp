<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://smigo.org/jsp/functions" %>

<!DOCTYPE html>
<html>
<head>
    <title><spring:message code="${msgTitle}" arguments="${titleArg}"/> | Smigo</title>
    <meta name="description" content="<spring:message code="${msgDescription}" arguments="${descriptionArg}"/>">

    <jsp:include page="head-common.jsp"/>

    <base href="/">

    <script src="http://code.jquery.com/jquery-2.1.3.min.js"></script>
    <script>window.jQuery || document.write('<script src="/static/jquery-2.1.3.min.js">\x3C/script>')</script>

    <%--angular--%>
    <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular.min.js"></script>
    <script>window.angular || document.write('<script src="/static/angular1.5.5/angular.min.js">\x3C/script>')</script>
    <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular-sanitize.min.js"></script>
    <script>
        try {
            window.angular.module('ngSanitize');
        } catch (e) {
            document.write('<script src="/static/angular1.5.5/angular-sanitize.min.js">\x3C/script>');
        }
    </script>
    <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular-route.min.js"></script>
    <script>
        try {
            window.angular.module('ngRoute');
        } catch (e) {
            document.write('<script src="/static/angular1.5.5/angular-route.min.js">\x3C/script>');
        }
    </script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/1.3.2/ui-bootstrap-tpls.min.js"></script>
    <script>
        try {
            window.angular.module('ui.bootstrap');
        } catch (e) {
            document.write('<script src="/static/ui-bootstrap-tpls-1.3.2.min.js">\x3C/script>');
        }
    </script>

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
            plantDataArray: <c:out escapeXml="false" value="${f:toJson(plantData)}"/>
        };
        console.log('Init data', initData);

        <%@ include file="../js/app.js" %>
        <%@ include file="../js/garden/VernacularService.js" %>
        <%@ include file="../js/garden/VarietyService.js" %>
        <%@ include file="../js/garden/SpeciesFilter.js" %>
        <%@ include file="../js/garden/GardenController.js" %>
        <%@ include file="../js/garden/AddYearModalController.js" %>
        <%@ include file="../js/garden/SpeciesService.js" %>
        <%@ include file="../js/garden/GardenService.js" %>
        <%@ include file="../js/garden/SquareDirective.js" %>
        <%@ include file="../js/garden/GridDirective.js" %>
        <%@ include file="../js/garden/FocusOnKeyPressedDirective.js" %>

        <%@ include file="../js/species/SpeciesModalController.js" %>

        <%@ include file="../js/user/RegisterController.js" %>
        <%@ include file="../js/user/LoginController.js" %>
        <%@ include file="../js/user/AccountController.js" %>
        <%@ include file="../js/user/PasswordController.js" %>
        <%@ include file="../js/user/UserService.js" %>
        <%@ include file="../js/user/RequestPasswordLinkController.js" %>

        <%@ include file="../js/translation/TranslateService.js" %>
        <%@ include file="../js/translation/TranslateDirective.js" %>
        <%@ include file="../js/translation/TranslateFilter.js" %>

        <%@ include file="../js/forum/ForumController.js" %>

        <%@ include file="../js/wall/WallController.js" %>

        <%@ include file="../js/social/FacebookDirective.js" %>
        <%@ include file="../js/social/TwitterDirective.js" %>

        <%@ include file="../js/MainMenuController.js" %>

        <%@ include file="../js/EqualsDirective.js" %>
        <%@ include file="../js/VernacularFilter.js" %>
    })();
</script>

<script type="text/ng-template" id="footer.html" charset="UTF-8">
    <jsp:include page="nav-bottom.jsp"/>
</script>

</body>
</html>