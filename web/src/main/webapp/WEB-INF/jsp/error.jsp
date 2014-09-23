<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="header.jsp"/>

<body>
<div class="container">
    <div class="page-header text-center">
        <h1>
            Error:<c:out value="${statusCode}"/>
        </h1>

        <h3><a href="<c:url value="/"/>">Back to Smigo</a></h3>
    </div>

</div>
</body>
<jsp:include page="footer.jsp"/>