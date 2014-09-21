<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:include page="header.jsp"/>


<body>
<div class="container">
    <div class="page-header text-center">
        <h1>
            <spring:message code="msg.enternewpassword"/>
        </h1>
    </div>

    <c:url value="/reset-password" var="actionUrl"/>
    <form:form role="form" method="post" action="${actionUrl}" commandName="resetKeyPasswordFormBean">
        <div class="has-error">
            <form:errors path="*" cssClass="help-block" element="span"/>
        </div>
        <form:hidden path="resetKey"/>
        <div class="form-group">
            <form:label path="password"><spring:message code="password"/></form:label>
            <form:password path="password" cssClass="form-control"/>
            <form:errors path="password" cssClass="errorblock"/>
        </div>
        <button type="submit" class="btn btn-default"><spring:message code="ok"/></button>
    </form:form>
</div>
</body>

<jsp:include page="footer.jsp"/>
