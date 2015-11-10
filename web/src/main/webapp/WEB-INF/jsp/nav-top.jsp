<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<nav class="navbar navbar-default" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="/">Smigo<span class="hidden-sm hidden-xs"> - <spring:message code="general.smigoslogan"/></span></a>
        </div>
        <div class="navbar-collapse collapse" id="navbar-right-collapse">
            <ul class="nav navbar-nav navbar-right">
                <li><a href="/garden"><spring:message code="garden"/></a></li>
                <li><a href="/forum" id="forum-link"><spring:message code="msg.forum"/></a></li>
                <li><a href="/help"><spring:message code="help"/></a></li>
                <sec:authorize access="isAnonymous()">
                    <li><a href="/register" id="register-link"><spring:message code="msg.account.register"/></a></li>
                    <li><a href="/login" id="login-link"><spring:message code="account.login"/></a></li>
                </sec:authorize>
                <sec:authorize access="isAuthenticated()">
                    <li><a href="/account" id="account-link"><spring:message code="msg.settings"/></a></li>
                    <li><a href="/logout2" id="logout-link"><spring:message code="account.logout"/></a></li>
                </sec:authorize>
            </ul>
        </div>
    </div>
</nav>