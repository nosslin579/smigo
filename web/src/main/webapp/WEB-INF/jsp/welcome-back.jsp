<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
    <title><spring:message code="msg.title.welcome-back"/> - Smigo</title>
    <jsp:include page="head-common.jsp"/>
    <meta name="robots" content="noindex">
    <meta name="description" content="<spring:message code="msg.metadescription.welcome-back"/>">
</head>

<jsp:include page="nav-top.jsp"/>
<body>
<div class="container">
    <!-- Default panel contents -->
    <div class="page-header text-center">
        <h1><spring:message code="msg.title.welcome-back"/></h1>

        <h3><a href="/">Smigo</a></h3>
    </div>
</div>
<jsp:include page="nav-bottom.jsp"/>
</body>
</html>