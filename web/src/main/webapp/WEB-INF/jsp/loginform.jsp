<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:include page="header.jsp"/>

<div id="loginform" class="smigoframe largecenteredsmigoframe">
    <div class="smigoframeheader"><spring:message code="account.login"/></div>
    <div class="smigoframecontent">

        <c:if test="${not empty param.login_error}">
            <div>
                Your login attempt was not successful<br/><br/>
                Reason: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
            </div>
        </c:if>

        <form name="f" action="<c:url value='/j_spring_security_check'/>" method="POST">
            <table>
                <tr>
                    <td><spring:message code="username"/>:</td>
                    <td><input type='text' name='j_username'/></td>
                </tr>
                <tr>
                    <td><spring:message code="password"/>:</td>
                    <td><input type='password' name='j_password'></td>
                </tr>
                <tr>
                    <td colspan='2'><input id="submit-loginform-form" value="<spring:message code="account.login"/>" type="submit"></td>
                </tr>
            </table>
        </form>
    </div>
</div>

<jsp:include page="footer.jsp"/>