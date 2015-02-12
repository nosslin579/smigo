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
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="description" content="Design and plan your vegetable garden and share with friends. Get advice for crop rotation and companion planting. Based on square foot gardening.">
    <meta name="keywords" content="kitchen vegetable garden crop rotation companion planting square foot gardening.">
    <meta name="author" content="Christian Nilsson">
    <c:if test="${hasEscapeFragment}">
        <meta name="fragment" content="!">
    </c:if>
    <c:if test="${param.noCrawl || noCrawl}">
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


