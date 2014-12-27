<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://smigo.org/jsp/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<jsp:include page="header.jsp"/>
<body ng-app="smigoModule">

<%@ include file="../views/menu.html" %>

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

        <%@ include file="../js/user/AcceptTermsOfServiceController.js" %>
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
<script type="text/ng-template" id="garden.html">
    <%@ include file="../views/garden.html" %>
</script>
<script type="text/ng-template" id="help.html">
    <%@ include file="../views/help.html" %>
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
<script type="text/ng-template" id="squareinfo.html">
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

</body>

<jsp:include page="footer.jsp"/>