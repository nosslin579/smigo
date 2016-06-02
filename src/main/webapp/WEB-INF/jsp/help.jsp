<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:if test="${empty param['hide-nav']}">
    <!DOCTYPE html>
    <html>
    <head>
        <title><spring:message code="msg.title.help"/> | Smigo</title>
        <meta name="description" content="<spring:message code="msg.metadescription.help"/>">
        <jsp:include page="head-common.jsp"/>
    </head>

    <jsp:include page="nav-top.jsp"/>

    <body>
</c:if>
<div class="container">
    <div class="page-header text-center">
        <h1>
            <spring:message code="general.faq"/>
        </h1>
    </div>

    <h3><spring:message code="msg.question.whatisthis"/></h3>
    <p><spring:message code="msg.answer.whatisthis"/></p>

    <h3><spring:message code="msg.question.howdoesitwork"/></h3>

    <div>
        <div><spring:message code="msg.answer.howdoesitwork1"/></div>
        <div><spring:message code="msg.answer.howdoesitwork2"/></div>
        <div><spring:message code="msg.answer.howdoesitwork3"/></div>
        <div><spring:message code="msg.answer.howdoesitwork4"/></div>
        <div>
            <spring:message code="msg.viewexample" arguments="<a href=\"/gardener/example\">,</a>,<a href=\"https://youtu.be/3HhEkv2oLzA\">,</a>"/></div>
    </div>

    <h3><spring:message code="msg.question.howaddspecies"/></h3>
    <p><spring:message code="msg.answer.howaddspecies"/></p>

    <h3><spring:message code="msg.question.howdelete"/></h3>
    <p><spring:message code="msg.answer.howdelete"/></p>

    <h3><spring:message code="msg.question.isitfree"/></h3>
    <p><spring:message code="msg.answer.isitfree"/></p>

    <h3><spring:message code="msg.question.relationtokga"/></h3>
    <p><spring:message code="msg.answer.relationtokga"/></p>

    <h3><spring:message code="msg.question.companionplanting"/></h3>
    <p><spring:message code="msg.answer.companionplanting"/></p>

    <h3><spring:message code="msg.question.croprotation"/></h3>
    <p><spring:message code="msg.answer.croprotation"/></p>

    <h3><spring:message code="msg.question.squarefootgardening"/></h3>
    <p><spring:message code="msg.answer.squarefootgardening"/></p>

    <h3><spring:message code="msg.question.other"/></h3>
    <p><spring:message code="msg.answer.other" arguments="<a class=\"text-lowercase\" href=\"/forum\">,</a>"/></p>

    <div class="media">
        <a class="pull-left" href="mailto:smigo@smigo.org" target="_blank"><span class="social-sprite email"></span></a>
        <a class="pull-left" href="https://www.facebook.com/smigogarden" target="_blank"><span class="social-sprite facebook"></span></a>
        <a class="pull-left" href="https://www.twitter.com/smigogarden" target="_blank"><span class="social-sprite twitter"></span></a>
        <a class="pull-left" href="http://se.linkedin.com/pub/christian-nilsson/3b/798/a5b/" target="_blank"><span class="social-sprite linkedin"></span></a>
        <a class="pull-left" href="http://www.reddit.com/r/smigo" target="_blank"><span class="social-sprite reddit"></span></a>
    </div>

    <jsp:include page="nav-bottom.jsp"/>
</div>
<c:if test="${empty param['hide-nav']}">
    </body>
    </html>
</c:if>