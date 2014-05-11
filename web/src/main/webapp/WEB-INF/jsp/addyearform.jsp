<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:include page="header.jsp"/>

<div id="loginform" class="smigoframe largecenteredsmigoframe">
    <div class="smigoframeheader"><spring:message code="addyear"/></div>
    <div class="smigoframecontent">


        <form:form method="post" action="${pageContext.request.contextPath}/addyear" modelAttribute="addYearFormBean">
            <form:errors path="*" cssClass="errorblock" element="div"/>
            <table>
                <tr>
                    <td><form:label path="year"><spring:message code="year"/></form:label></td>
                    <td><form:input path="year"/></td>
                    <td><form:errors path="year" cssClass="error"/></td>
                    <td><input type="submit" value="<spring:message code="add" />"/></td>
                </tr>
            </table>
        </form:form>


    </div>
</div>

<jsp:include page="footer.jsp"/>