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
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title><spring:message code="${msgTitle}"/></title>
    <meta name="description" content="<spring:message code="${msgDescription}"/>">
    <meta name="keywords" content="kitchen vegetable garden crop rotation companion planting square foot gardening layout design">
    <meta name="author" content="Christian Nilsson">
    <c:if test="${addEscapeFragment}">
        <meta name="fragment" content="!">
        <link rel="alternate" hreflang="sv" href="http://sv.smigo.org${requestScope['javax.servlet.forward.request_uri']}">
        <link rel="alternate" hreflang="en" href="http://en.smigo.org${requestScope['javax.servlet.forward.request_uri']}">
    </c:if>
    <c:if test="${param.addRobotsNoIndex || addRobotsNoIndex}">
        <meta name="robots" content="noindex">
    </c:if>
    <%--<meta name="referrer" content="never">--%>
    <%--<link rel="icon" href="../../favicon.ico">--%>
    <base href="/">

    <script src="http://code.jquery.com/jquery-2.1.3.min.js"></script>
    <script>
        if (!window.jQuery) {
            document.write('<script src="/static/jquery-2.1.3.min.js"><\/script>');
        }
    </script>

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

    <%--other--%>
    <script src="//www.google.com/recaptcha/api.js?render=explicit" async defer></script>

    <style>
        <%@ include file="bootstrap.min.css" %>
        <%@ include file="style.css" %>
    </style>
</head>


