<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>
    #mainmenu .mainmenuitem:nth-of-type(7) {
        background: #466919 !important;
    }
</style>

<jsp:include page="header.jsp"/>

<div id="about" class="smigoframe largecenteredsmigoframe">
    <div class="smigoframeheader"><spring:message code="about"/></div>
    <div class="smigoframecontent">

        <p>
            19 jan 2013 - Added support for adding rules and converted to UTF-8.
        </p>
        <p>
            1 jan 2014 - Fixed various bugs. Removed support for uploading. Removed support for custom icon.
        </p>
        <p>
            7 jan 2014 - Added support for touch device.
        </p>
    </div>
</div>

<jsp:include page="footer.jsp"/>