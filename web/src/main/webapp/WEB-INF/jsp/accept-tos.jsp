<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:include page="header.jsp">
    <jsp:param name="noCrawl" value="true"/>
</jsp:include>

<jsp:include page="nav-simple.jsp">
    <jsp:param name="showLogout" value="true"/>
</jsp:include>


<body>
<div class="container">
    <div class="page-header text-center">
        <h1>
            Smigo - <spring:message code="termsofservice"/>
        </h1>
    </div>

    <div class="embed-responsive embed-responsive-4by3">
        <iframe class="embed-responsive-item" src="/static/terms-of-service.html"></iframe>
    </div>

    <c:url value="/accept-termsofservice" var="actionUrl"/>
    <form:form role="form" method="post" action="${actionUrl}" commandName="acceptTermsOfService">
        <div class="has-error">
            <form:errors path="*" cssClass="help-block" element="span"/>
        </div>
        <div class="checkbox">
            <form:label path="termsOfService">
                <form:checkbox path="termsOfService"/>
                <spring:message code="msg.agreetos"/>
            </form:label>
        </div>
        <button type="submit" class="btn btn-default"><spring:message code="ok"/></button>
        <a href="/logout2" class="btn btn-default"><spring:message code="cancel"/></a>
    </form:form>
</div>
</body>

<jsp:include page="footer.jsp"/>
