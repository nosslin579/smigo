<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://smigo.org/jsp/functions" %>

<jsp:include page="header.jsp"/>

<div ng-app="speciesModule">
    <div ng-view></div>

    <script type="application/javascript">
        <%@ include file="app.js" %>
    </script>

    <%--########## ng views ##############--%>
    <script type="text/ng-template" id="garden">
        <%@ include file="ng-garden.html" %>
    </script>
</div>

<jsp:include page="footer.jsp"/>