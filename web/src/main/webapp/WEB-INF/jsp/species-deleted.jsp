<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="msg" uri="http://smigo.org/jsp/msg" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<jsp:include page="header.jsp"/>

<div id="help" class="smigoframe largecenteredsmigoframe">
    <div class="smigoframeheader">Smigo</div>
    <div class="smigoframecontent">
        <spring:message code="${msg:species(speciesId)}" var="speciesVernacularName"/>
        <h4 style="margin:auto"><spring:message code="general.deleted" arguments="${speciesVernacularName}"/></h4>
    </div>
</div>

<jsp:include page="footer.jsp"/>