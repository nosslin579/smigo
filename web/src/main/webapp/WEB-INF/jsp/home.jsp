<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <jsp:include page="head-common.jsp"/>
    <link rel="alternate" hreflang="x-default" href="http://smigo.org/">
    <link rel="alternate" hreflang="sv" href="http://sv.smigo.org/">
    <link rel="alternate" hreflang="en" href="http://en.smigo.org/">

    <style>
        <%@ include file="bootstrap.min.css" %>
        <%@ include file="style.css" %>
    </style>
</head>
<body>

<jsp:include page="nav-top.jsp"/>
<div class="jumbotron" style="text-align: center;">
    <div class="container">

        <h1><spring:message code="msg.front.header"/></h1>
        <p>
            <spring:message code="msg.front.intro"/>
        </p>
        <p><a class="btn btn-primary btn-lg" href="/garden" role="button"><spring:message code="msg.front.tryit"/></a>
        </p>
    </div>
</div>
<div class="container">
    <div class="row">
        <div class="col-md-4">
            <h2><spring:message code="msg.front.head1"/></h2>
            <p><spring:message code="msg.front.desc1"/></p>
        </div>
        <div class="col-md-4">
            <h2><spring:message code="msg.front.head2"/></h2>
            <p><spring:message code="msg.front.desc2"/></p>
        </div>
        <div class="col-md-4">
            <img src="/static/icon/snapshot.png" alt="vegetable garden layout" class="img-rounded full-width">
        </div>
    </div>

    <div class="row">
        <div class="col-md-4 hidden-xs hidden-sm">
            <img src="/static/icon/kitchengarden.jpg" alt="kitchen garden" class="img-rounded full-width">
        </div>
        <div class="col-md-8">
            <h2><spring:message code="msg.front.head3"/></h2>
            <p><spring:message code="msg.front.desc3a"/> <a href="https://github.com/nosslin579/smigo">Github</a></p>
            <p><spring:message code="msg.front.desc3b"/></p>
        </div>
    </div>
</div>
<jsp:include page="nav-bottom.jsp"/>
</body>
</html>
