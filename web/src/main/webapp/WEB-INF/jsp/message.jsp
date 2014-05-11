<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<jsp:include page="header.jsp"/>

<div id="help" class="smigoframe largecenteredsmigoframe">
    <div class="smigoframeheader">Smigo</div>
    <div class="smigoframecontent">
        <c:catch var="exception1">
            <h4 style="margin:auto"><c:out value="${message}"/></h4>
        </c:catch>
        <c:catch var="exception2">
            <h4 style="margin:auto"><spring:message code="${translatedmessage}" arguments="${argument1}"/></h4>
        </c:catch>
    </div>
</div>

<jsp:include page="footer.jsp"/>