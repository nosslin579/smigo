<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<jsp:include page="header.jsp"/>

<div id="loginform" class="smigoframe largecenteredsmigoframe">
    <div class="smigoframeheader">
        <spring:message code="deleteyear"/>
    </div>
    <div class="smigoframecontent floatcontainer">
        <form method="post" action="deleteyear">
            <label for="deleteyear"><spring:message code="year"/></label>
            <input type="text" name="deleteyear" size="4" maxlength="4"/> <input type="submit" value="<spring:message code="delete"/>"/>
        </form>
    </div>
</div>

<jsp:include page="footer.jsp"/>