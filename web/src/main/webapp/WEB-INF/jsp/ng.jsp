<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://smigo.org/jsp/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<jsp:include page="header.jsp"/>
<body ng-app="speciesModule">

<%@ include file="menu.html" %>

<div ng-view></div>

<script type="application/javascript">
    <%@ include file="app.js" %>
</script>

<%--########## ng views ##############--%>
<script type="text/ng-template" id="garden.html">
    <%@ include file="ng-garden.html" %>
</script>
<script type="text/ng-template" id="help.html">
    <%@ include file="ng-help.html" %>
</script>
<script type="text/ng-template" id="login.html">
    <%@ include file="ng-login.html" %>
</script>

</body>

<jsp:include page="footer.jsp"/>