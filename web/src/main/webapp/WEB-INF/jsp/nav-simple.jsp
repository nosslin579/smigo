<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav class="navbar navbar-default" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="/">Smigo<span class="hidden-sm hidden-xs"><spring:message code="general.smigoslogan"/></span></a>
        </div>
        <div class="navbar-collapse collapse" id="navbar-right-collapse">
            <ul class="nav navbar-nav navbar-right">
                <c:if test="false">
                    <li><a href="/garden"><spring:message code="garden"/></a></li>
                    <li><a href="/forum" id="forum-link"><spring:message code="msg.forum"/></a></li>
                    <li><a href="/help"><spring:message code="help"/></a></li>
                    <li><a href="/register" id="register-link"><spring:message code="msg.account.register"/></a></li>
                    <li><a href="/login" id="login-link"><spring:message code="account.login"/></a></li>
                    <li><a href="/account" id="account-link"><spring:message code="msg.settings"/></a></li>
                </c:if>
                <c:if test="${param.showLogout}">
                    <li><a href="/logout2" id="logout-link"><spring:message code="account.logout"/></a></li>
                </c:if>
            </ul>
        </div>
    </div>
</nav>