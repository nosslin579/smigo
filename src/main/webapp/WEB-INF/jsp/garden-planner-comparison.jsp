<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:if test="${empty param['hide-nav']}">
    <!DOCTYPE html>
    <html>
    <head>
        <title><spring:message code="msg.title.vegetablegardenplannercomparison"/></title>
        <meta name="description" content="<spring:message code="msg.metadescription.vegetablegardenplannercomparison"/>">
        <jsp:include page="head-common.jsp"/>
    </head>

    <jsp:include page="nav-top.jsp"/>


    <body>
</c:if>
<div class="container">
    <div class="page-header text-center">
        <h1>
            <spring:message code="msg.title.vegetablegardenplannercomparison"/>
        </h1>
        <p><spring:message code="msg.metadescription.vegetablegardenplannercomparison"/></p>
    </div>

    <div class="table-responsive">
        <table class="table table-bordered">
            <tr>
                <th rowspan="2"><spring:message code="name"/></th>
                <th rowspan="2" class="text-center"><spring:message code="msg.price"/></th>
                <th colspan="4" class="text-center"><spring:message code="msg.features"/></th>
                <th rowspan="2" class="text-center"><spring:message code="language"/></th>
                <th colspan="4" class="text-center"><spring:message code="msg.platformsupport"/></th>
            </tr>
            <tr>
                <th class="text-center"><spring:message code="msg.companionplanting"/></th>
                <th class="text-center"><spring:message code="msg.croprotation"/></th>
                <th class="text-center"><spring:message code="msg.socialmediasharing"/></th>
                <th class="text-center"><spring:message code="msg.varieties"/></th>
                <th class="text-center">Windows</th>
                <th class="text-center">Mac</th>
                <th class="text-center">iPad</th>
                <th class="text-center">Android</th>
            </tr>
            <tr>
                <th><a href="//smigo.org/">Smigo</a></th>
                <td><spring:message code="msg.free"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="text-center"><spring:message code="msg.multiplelanguagesupport"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
            </tr>
            <tr>
                <th class="text-nowrap"><a href="//www.smartgardener.com/">Smart Gardener</a></th>
                <td><spring:message code="msg.free"/></td>
                <td class="danger text-center"><spring:message code="no"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="danger text-center"><spring:message code="no"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="text-center"><spring:message code="msg.english"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
            </tr>
            <tr>
                <th class="text-nowrap"><a href="//www.growveg.com">Grow veg</a></th>
                <td>$25</td>
                <td class="danger text-center"><spring:message code="no"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="text-center"><spring:message code="msg.english"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="danger text-center"><spring:message code="no"/></td>
            </tr>
            <tr>
                <th><a href="//plangarden.com">Plangarden</a></th>
                <td>$20</td>
                <td class="danger text-center"><spring:message code="no"/></td>
                <td class="danger text-center"><spring:message code="no"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="text-center"><spring:message code="msg.english"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="danger text-center"><spring:message code="no"/></td>
                <td class="danger text-center"><spring:message code="no"/></td>
            </tr>
            <tr>
                <th class="text-nowrap">
                    <a href="//vegetableplanner.vegetable-gardening-online.com/">Vegetable planner</a></th>
                <td><spring:message code="msg.free"/></td>
                <td class="danger text-center"><spring:message code="no"/></td>
                <td class="danger text-center"><spring:message code="no"/></td>
                <td class="danger text-center"><spring:message code="no"/></td>
                <td class="danger text-center"><spring:message code="no"/></td>
                <td class="text-center"><spring:message code="msg.english"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="danger text-center"><spring:message code="no"/></td>
                <td class="danger text-center"><spring:message code="no"/></td>
            </tr>
            <tr>
                <th class="text-nowrap">
                    <a href="//anbeeten.de/">Anbeeten</a></th>
                <td><spring:message code="msg.free"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="danger text-center"><spring:message code="no"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="text-center"><spring:message code="msg.german"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
                <td class="success text-center"><spring:message code="yes"/></td>
            </tr>
        </table>
    </div>

    <jsp:include page="nav-bottom.jsp"/>
</div>
<c:if test="${empty param['hide-nav']}">
    </body>
    </html>
</c:if>