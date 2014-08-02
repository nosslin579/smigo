<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <title>Smigo - <spring:message code="general.smigoslogan"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <%--<meta name="viewport" content="user-scalable=no, width=device-width, initial-scale=1, maximum-scale=1">    --%>
    <%--<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-2" />--%>
    <link rel="stylesheet" type="text/css" href="/static/css/structure.css">
    <link rel="stylesheet" type="text/css" href="/static/css/firstlady.css">
    <%--<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.js"></script>--%>
    <script type="text/javascript" src="/static/js/common.js"></script>
    <script type="text/javascript" src="/static/js/angular.js"></script>
    <script type="text/javascript" src="/static/js/angular-route.js"></script>
    <%--<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.19/angular.js"></script>--%>
    <%--<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.19/angular-route.js"></script>--%>

</head>

<body>
<div id="smigotitle">
    <a href="${pageContext.request.contextPath}">Smigo</a>
    <span><spring:message code="general.smigoslogan"/></span>
    <c:if test="${hostEnvironmentInfo.isDebug()}">
        <span style="color: red"> - ${hostEnvironmentInfo.getEnv()}</span>
    </c:if>
</div>

<div id="accountbox">

    <sec:authorize access="isAnonymous()">
        <a id="signup-link" class="smigolink" href="${pageContext.request.contextPath}/signup"><spring:message code="signup"/></a>
        <a id="login-link" class="smigolink" href="${pageContext.request.contextPath}/login"><spring:message code="account.login"/></a>
    </sec:authorize>

    <sec:authorize access="isAuthenticated()">
        <a id="account-details-link" class="smigolink" href="${pageContext.request.contextPath}/user"><sec:authentication
                property="principal.username"/></a>
        <a id="logout-link" class="smigolink" href="${pageContext.request.contextPath}/logout"><spring:message
                code="account.logout"/></a>
    </sec:authorize>

</div>


<!-- ######## main menu ######## -->
<div id=mainmenu>
    <div class="mainmenuitem">
        <div class="separator"></div>
        <a id="garden-menu-item" href="${pageContext.request.contextPath}/garden"><spring:message code="garden"/></a>
    </div>
    <div class="separator"></div>
    <div class="mainmenuitem">
        <a id="plants-menu-item" href="${pageContext.request.contextPath}/listspecies"><spring:message code="plants"/></a>
    </div>
    <div class="separator"></div>
    <div class="mainmenuitem">
        <a id="help-menu-item" class="commandlink" href="${pageContext.request.contextPath}/help"><spring:message code="help"/></a>
    </div>
    <div class="separator"></div>
    <div class="mainmenuitem">
        <a id="about-menu-item" href="${pageContext.request.contextPath}/about"><spring:message code="about"/></a>
    </div>
    <div class="separator"></div>
</div>
<c:if test="${hostEnvironmentInfo.isDebug()}">
<div id="debug">

</div>
</c:if>

