<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:include page="header.jsp"/>

<div id="userinfo" class="smigoframe largecenteredsmigoframe">
    <div class="smigoframeheader">${user.username}</div>
    <div class="smigoframecontent">
        <table>
            <tr>
                <td><spring:message code="name"/></td>
                <td>${user.displayname}</td>
            </tr>

            <c:if test="${showall}">
                <tr>
                    <td><spring:message code="email"/></td>
                    <td>${user.email}</td>
                </tr>
                <tr>

                <tr>
                    <td><spring:message code="username"/></td>
                    <td>${user.username}</td>
                </tr>

                <tr>
                    <td><spring:message code="translation"/></td>
                    <td>${user.locale.displayName}</td>
                </tr>
            </c:if>

            <tr>
                <td><spring:message code="about"/></td>
                <td>${user.about}</td>
            </tr>
            <tr>
                <td><spring:message code="registrationDate"/></td>
                <td><fmt:formatDate type="date" value="${user.registrationDate}"/></td>
            </tr>

        </table>
        <c:if test="${showall}">
            <div class="useraction"><a href="${pageContext.request.contextPath}/edituser"><spring:message code="edit"/></a></div>
            <div class="useraction"><a href="${pageContext.request.contextPath}/changepassword"><spring:message code="changepassword"/></a></div>
        </c:if>
    </div>
</div>

<jsp:include page="footer.jsp"/>