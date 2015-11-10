<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<hr style="border-top-color:#B8B8B8;margin-top: 100px">

<div class="row">
    <div class="col-sm-offset-1 col-sm-10 col-md-offset-2 col-md-8">
        <footer>
            <div class="row">
                <div class="col-sm-offset-1 col-xs-4">
                    <div style="text-decoration: underline;"><spring:message code="about"/></div>
                    <a href="https://github.com/nosslin579/smigo"><spring:message code="msg.sourccode"/></a><br/>
                    <a href="/static/terms-of-service.html" rel="nofollow"><spring:message code="termsofservice"/></a><br/>
                    <a href="/help"><spring:message code="help"/></a><br/>
                    <a href="/forum"><spring:message code="msg.forum"/></a><br/>
                </div>
                <div class="col-xs-4">
                    <div style="text-decoration: underline;"><spring:message code="contact"/></div>
                    <a href="http://www.reddit.com/r/smigo" target="_blank">Reddit</a><br/>
                    <a href="https://www.facebook.com/smigogarden" target="_blank">Facebook</a><br/>
                    <a href="https://www.twitter.com/smigogarden" target="_blank">Twitter</a><br/>
                    <a href="http://se.linkedin.com/in/christiannilsson1979" target="_blank">Linkedin</a><br/>
                </div>
                <div class="col-xs-4 col-xs-3">
                    <div style="text-decoration: underline;"><spring:message code="msg.links"/></div>
                    <a href="<spring:message code="msg.link1.href"/>" target="_blank"><spring:message code="msg.link1.text"/></a><br/>
                    <a href="<spring:message code="msg.link2.href"/>" target="_blank"><spring:message code="msg.link2.text"/></a><br/>
                    <a href="<spring:message code="msg.link3.href"/>" target="_blank"><spring:message code="msg.link3.text"/></a><br/>
                    <a href="<spring:message code="msg.link4.href"/>" target="_blank"><spring:message code="msg.link4.text"/></a><br/>
                </div>
            </div>
            <div class="row" style="margin: 16px;">
                <div class="text-center">Copyright (C) 2011-2015 Christian Nilsson - christian1195@gmail.com</div>
            </div>
        </footer>
    </div>
</div>