<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<nav class="navbar navbar-default" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="/">Smigo<span class="hidden-sm hidden-xs"> - <spring:message code="general.smigoslogan"/></span></a>
        </div>
        <ul class="nav navbar-nav navbar-right">
            <sec:authorize access="isAuthenticated()">
                <li><a href="/gardener/${pageContext.request.userPrincipal.name}" id="comments"><spring:message code="msg.comments"/></a></li>
            </sec:authorize>
            <li><a href="/garden-planner"><spring:message code="garden"/></a></li>
            <li><a href="/forum" id="forum-link"><spring:message code="msg.forum"/></a></li>
            <li><a href="/help" target="_self"><spring:message code="help"/></a></li>
            <sec:authorize access="isAnonymous()">
                <li><a href="/register" id="register-link"><spring:message code="msg.account.register"/></a></li>
                <li><a href="/login" id="login-link"><spring:message code="account.login"/></a></li>
            </sec:authorize>
            <sec:authorize access="isAuthenticated()">
                <li><a href="/account" id="account-link">${pageContext.request.userPrincipal.name}</a></li>
            </sec:authorize>
        </ul>
    </div>
</nav>