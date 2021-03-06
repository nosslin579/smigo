<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="author" content="Christian Nilsson">

<link rel="alternate" hreflang="x-default" href="http://smigo.org${requestScope['javax.servlet.forward.request_uri']}">
<link rel="alternate" hreflang="sv" href="http://sv.smigo.org${requestScope['javax.servlet.forward.request_uri']}">
<link rel="alternate" hreflang="en" href="http://en.smigo.org${requestScope['javax.servlet.forward.request_uri']}">
<link rel="alternate" hreflang="de" href="http://de.smigo.org${requestScope['javax.servlet.forward.request_uri']}">
<link rel="alternate" hreflang="es" href="http://es.smigo.org${requestScope['javax.servlet.forward.request_uri']}">

<%--http://stackoverflow.com/questions/2208933/how-do-i-force-a-favicon-refresh--%>
<link rel="icon" href="/static/species/28.png?v=6">

<link rel="stylesheet" type="text/css" href="/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="/css/style.css">

<script src="http://code.jquery.com/jquery-2.1.3.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular-sanitize.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular-route.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/1.3.2/ui-bootstrap-tpls.min.js"></script>
