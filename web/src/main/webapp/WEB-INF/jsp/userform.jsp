<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:include page="header.jsp"/>

<div id="userform" class="smigoframe largecenteredsmigoframe">
    <div class="smigoframeheader"><spring:message code="signup"/></div>
    <div class="smigoframecontent">
        <form:form method="post" action="${pageContext.request.contextPath}/cuuser" modelAttribute="user">
            <form:errors path="*" cssClass="errorblock" element="div"/>
            <table>
                <tr>
                    <th class="namefield"></th>
                    <th class="valuefield"></th>
                    <th char="validationfield"></th>
                </tr>
                <tr>
                    <td><input name="id" value="${user.id}" type="hidden"/></td>
                </tr>

                <tr>
                    <c:if test="${user.id == 0}">
                        <td><form:label path="username"><spring:message code="username"/></form:label></td>
                        <td><form:input path="username"/></td>
                        <td><form:errors path="username" cssClass="error"/></td>
                    </c:if>
                    <c:if test="${user.id != 0}">
                        <td><input name="username" value="asdf1234567890" type="hidden"/></td>
                    </c:if>
                </tr>

                <tr>
                    <c:if test="${user.id == 0}">
                        <td><form:label path="password"><spring:message code="password"/></form:label></td>
                        <td><form:password path="password"/></td>
                        <td><form:errors path="password" cssClass="error"/></td>
                    </c:if>
                    <c:if test="${user.id != 0}">
                        <td><input name="password" value="asdf1234567890" type="hidden"/></td>
                    </c:if>
                </tr>


                <tr>
                    <c:if test="${user.id == 0}">
                        <td><form:label path="passwordagain"><spring:message code="passwordagain"/></form:label></td>
                        <td><form:password path="passwordagain"/></td>
                        <td><form:errors path="passwordagain"/></td>
                    </c:if>
                    <c:if test="${user.id != 0}">
                        <td><input name="passwordagain" value="asdf1234567890" type="hidden"/></td>
                    </c:if>
                </tr>


                <tr>
                    <td><form:label path="email"><spring:message code="email"/></form:label></td>
                    <td><form:input path="email"/></td>
                    <td><form:errors path="email"/></td>
                </tr>


                <tr>
                    <td><form:label path="locale"><spring:message code="translation"/></form:label></td>
                    <td>
                        <form:select path="locale">
                            <form:options items="${availableLocales}"/>
                        </form:select>
                    </td>
                    <td><form:errors path="locale"/></td>
                </tr>

                <tr>
                    <td><form:label path="displayname"><spring:message code="name"/></form:label></td>
                    <td><form:input path="displayname"/></td>
                    <td><form:errors path="displayname"/></td>
                </tr>

                <tr>
                    <td><form:label path="about"><spring:message code="about"/></form:label></td>
                    <td><form:textarea path="about" rows="3" cols="20"/></td>
                    <td><form:errors path="about"/></td>
                </tr>


                <tr>
                    <c:if test="${user.id == 0}">
                        <td><form:label path="termsofservice"><a href=/tos.html><spring:message
                                code="termsofservice"/></a></form:label></td>
                        <td><form:checkbox path="termsofservice"/><spring:message code="general.iagree"/></td>
                        <td><form:errors path="termsofservice"/></td>
                    </c:if>
                    <c:if test="${user.id != 0}">
                        <td><input name="termsofservice" value="true" type="hidden"/></td>
                    </c:if>
                </tr>


                <tr>
                    <td colspan="2">
                        <input type="submit" value="<spring:message code="ok"/>"/>
                    </td>
                </tr>
            </table>

        </form:form>
    </div>
</div>

<jsp:include page="footer.jsp"/>