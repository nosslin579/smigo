<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:include page="header.jsp"/>

<div id="loginform" class="smigoframe largecenteredsmigoframe">
    <div class="smigoframeheader"><spring:message code="msg.resetpassword"/></div>
    <div class="smigoframecontent">


        <form:form method="post" action="${pageContext.request.contextPath}/reset-password"
                   modelAttribute="resetFormBean">
            <form:errors path="*" cssClass="errorblock" element="div"/>
            <table>
                <tr>
                    <td><form:label path="email"><spring:message code="email"/></form:label></td>
                    <td><form:input path="email"/></td>
                    <td><form:errors path="email" cssClass="error"/></td>
                    <td><input type="submit" value="<spring:message code="msg.resetpassword" />"/></td>
                </tr>
            </table>
        </form:form>


    </div>
</div>

<jsp:include page="footer.jsp"/>