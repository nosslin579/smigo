<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:include page="header.jsp"/>

<div id="help" class="smigoframe largecenteredsmigoframe">
    <div class="smigoframeheader"><spring:message code="changepassword"/></div>
    <div class="smigoframecontent">
        <form:form method="post" action="${pageContext.request.contextPath}/changepassword"
                   modelAttribute="passwordFormBean">
            <form:errors path="*" cssClass="errorblock" element="div"/>
            <table>
                <c:if test="${!requireCurrentPassword}">
                    <input name="oldPassword" type="hidden" value=""/>
                </c:if>
                <c:if test="${requireCurrentPassword}">
                    <tr>
                        <td><form:label path="oldPassword"><spring:message
                                code="account.currentpassword"/></form:label></td>
                        <td><form:password path="oldPassword"/></td>
                        <td><form:errors path="oldPassword" cssClass="error"/></td>
                    </tr>
                </c:if>
                <tr>
                    <td><form:label path="newPassword"><spring:message code="password"/></form:label></td>
                    <td><form:password path="newPassword"/></td>
                    <td><form:errors path="newPassword" cssClass="error"/></td>
                </tr>
                <tr>
                    <td><label for="passwordagain"><spring:message code="passwordagain"/></label></td>
                    <td><input id="passwordagain" type="password"/></td>
                    <td></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input id="submit-password-button" type="submit" value="<spring:message code="ok"/>"/>
                    </td>
                </tr>
            </table>

        </form:form>
    </div>
</div>

<jsp:include page="footer.jsp"/>