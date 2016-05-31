<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://smigo.org/jsp/functions" %>

<!DOCTYPE html>
<html>
<head>
    <title><spring:message code="${msgTitle}" arguments="${titleArg}"/> | Smigo</title>
    <meta name="description" content="<spring:message code="${msgDescription}" arguments="${descriptionArg}"/>">

    <c:if test="${requestScope['javax.servlet.forward.request_uri'] eq '/accept-terms-of-service'}">
        <meta name="robots" content="noindex">
    </c:if>

    <jsp:include page="head-common.jsp"/>

    <base href="/">

</head>

<body ng-app="smigoModule">

<%@ include file="../views/nav-top.html" %>

<div ng-view class="angular-view"></div>

<script type="application/javascript">

    <%@ include file="../js/Customization.js" %>
    (function () {

        var initData = {
            species: <c:out escapeXml="false" value="${f:toJson(species)}"/>,
            rules: <c:out escapeXml="false" value="${f:toJson(rules)}"/>
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
        <%@ include file="../js/species/VernacularFilter.js" %>
        <%@ include file="../js/species/VernacularDirective.js" %>

        <%@ include file="../js/user/RegisterController.js" %>
        <%@ include file="../js/user/LoginController.js" %>
        <%@ include file="../js/user/AccountController.js" %>
        <%@ include file="../js/user/UserService.js" %>
        <%@ include file="../js/user/UserInterceptor.js" %>
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
    })();
</script>

<script type="text/ng-template" id="footer.html" charset="UTF-8">
    <jsp:include page="nav-bottom.jsp"/>
</script>
<script type="text/ng-template" id="views/garden-planner.html" charset="UTF-8">
    <%@include file="../views/garden-planner.html" %>
</script>
<script type="text/ng-template" id="views/square.html" charset="UTF-8">
    <%@include file="../views/square.html" %>
</script>
<script type="text/ng-template" id="views/grid.html" charset="UTF-8">
    <%@include file="../views/grid.html" %>
</script>
<script type="text/ng-template" id="views/login.html" charset="UTF-8">
    <%@include file="../views/login.html" %>
</script>

</body>
</html>