<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Error - Smigo</title>
    <jsp:include page="head-common.jsp"/>
    <meta name="robots" content="noindex">
</head>
<body>
<div class="container">
    <div class="page-header text-center">
        <h1>
            Error:<c:out value="${statusCode}"/>
        </h1>

        <h3><a href="<c:url value="/"/>" target="_self">Smigo</a></h3>
    </div>

</div>
</body>
</html>