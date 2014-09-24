<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://smigo.org/jsp/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<jsp:include page="header.jsp"/>
<body ng-app="smigoModule">

<%@ include file="menu.html" %>

<div ng-view></div>

<script type="application/javascript">

    <%@ include file="Customization.js" %>
    <%@ include file="app.js" %>
    (function () {

        var initData = {
            user: <c:out escapeXml="false" value="${f:toJson(user)}"/>,
            squares: <c:out escapeXml="false" value="${f:toJson(squares)}"/>,
            species: <c:out escapeXml="false" value="${f:toJson(species)}"/>
        };


        <%@ include file="garden/SpeciesFilter.js" %>
        <%@ include file="garden/GardenController.js" %>
        <%@ include file="garden/SpeciesService.js" %>
        <%@ include file="garden/PlantService.js" %>

        <%@ include file="user/AcceptTermsOfServiceController.js" %>
        <%@ include file="user/RegisterController.js" %>
        <%@ include file="user/LoginController.js" %>
        <%@ include file="user/AccountController.js" %>
        <%@ include file="user/PasswordController.js" %>
        <%@ include file="user/UserService.js" %>
        <%@ include file="user/RequestPasswordLinkController.js" %>

        <%@ include file="MainMenuController.js" %>

        <%@ include file="EqualsDirective.js" %>
        <%@ include file="TranslateFilter.js" %>
    })();
</script>

<%--########## ng views ##############--%>
<script type="text/ng-template" id="garden.html">
    <%@ include file="ng-garden.html" %>
</script>
<script type="text/ng-template" id="help.html">
    <%@ include file="ng-help.html" %>
</script>
<script type="text/ng-template" id="login.html">
    <%@ include file="ng-login.html" %>
</script>
<script type="text/ng-template" id="accept-terms-of-service.html">
    <%@ include file="accept-terms-of-service.html" %>
</script>
<script type="text/ng-template" id="account.html">
    <%@ include file="account.html" %>
</script>
<script type="text/ng-template" id="request-password-link.html">
    <%@ include file="request-password-link.html" %>
</script>

</body>

<jsp:include page="footer.jsp"/>