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

        <form name="f" action="<c:url value="/login"/>" method="POST">
            <input type="hidden" name="remember-me" value="true"/>
            <table>
                <tr>
                    <td><spring:message code="username"/>:</td>
                    <td><input type="text" name="username"/></td>
                </tr>
                <tr>
                    <td><spring:message code="password"/>:</td>
                    <td><input type="password" name="password"/></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <spring:message code="account.login" var="loginMsg"/>
                        <input id="submit-loginform-form" value="${loginMsg}" type="submit">
                    </td>
                </tr>
            </table>
        </form>
        <hr/>
        <form action="/web/login-openid" id="googleOpenId" method="post">
        <input id="openid_identifier" name="openid_identifier" type="hidden"
                   value="https://www.google.com/accounts/o8/id"/>
            <input type="hidden" name="remember-me" value="true">
            <input type="submit" value="Google Login" onClick="submit('googleOpenId')"/>
        </form>
        <hr/>
        <form action="/web/login-openid" method="POST">
            <table>
                <tr>
                    <td>Identity:</td>
                    <td><input type="text" size="30" name="openid_identifier"/></td>
                </tr>
                <tr>
                    <td><input type="checkbox" name="remember-me"></td>
                    <td>Remember me on this computer.</td>
                </tr>
                <tr>
                    <td colspan="2"><input name="submit" type="submit" value="Login"/></td>
                </tr>
            </table>
        </form>
    </div>
</div>


<jsp:include page="footer.jsp"/>