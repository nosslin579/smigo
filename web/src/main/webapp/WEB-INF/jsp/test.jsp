<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<jsp:include page="header.jsp"/>

<div id="peephole" style="width: 600px;height: 500px;overflow: scroll;margin: 30px">
    <div id="grid" style="background-color: grey;width: 9999px;height: 9999px">
        <c:forEach items="${viewBean.getSquares()}" var="s">
            <div style="background-color: green;width: 48px;height: 48px;position: relative;top: ${s.y * 48}px;left: ${s.x * 48}px;">
                <c:forEach items="${s.getSpecies()}" var="sp">
                    ${sp.getScientificName()}<br/>
                </c:forEach>
            </div>
        </c:forEach>
    </div>
</div>

<script>


    $(function () {
        console.log("hej");
        var gridElement = document.getElementById('grid');
        Hammer(gridElement).on('tap', function (event) {
            console.log('triggerd: tap', event);
            $('#debug').text('triggerd  tap ' + event.type);
        });
    });
</script>

<jsp:include page="footer.jsp"/>