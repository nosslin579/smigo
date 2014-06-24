<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="header.jsp"/>

<style>
    #mainmenu .mainmenuitem:nth-of-type(3) {
        background: #466919 !important;
    }
</style>

<div id="speciesoverview" class="smigoframe smigosingledialog">
    <div class="plantsheader smigoframeheader"><spring:message code="plants"/></div>
    <div class="addplant">
        <sec:authorize access="isAuthenticated()">
            <a id="add-species-link" class="smigolink" href="${pageContext.request.contextPath}/add-species"><spring:message code="addspecies"/></a>
        </sec:authorize>
    </div>
    <div id="speciesoverviewcontainer">
        <table id="speciesoverviewtable">
            <thead>
            <tr>
            <th class="translationheader"><spring:message code="name"/></th>
            <th class="sientificnameheader"><spring:message code="scientificname"/></th>
            </thead>
            <tbody>
            <c:forEach items="${listofallspecies}" var="currentspecies" varStatus="status">
                <tr id="jsspeciesrow_${currentspecies.id}" class="speciesrow show${currentspecies.display}">
                    <td class="translation">
                        <a class="smigolink" href="${pageContext.request.contextPath}/species/${currentspecies.id}">
                            <c:out value="${currentspecies.translation}" escapeXml="false"/>
                        </a>
                    </td>
                    <td class="scientificname">
                        <a class="smigolink" href="${pageContext.request.contextPath}/species/${currentspecies.id}">
                            <c:out value="${currentspecies.scientificName}"/>
                        </a>
                    </td>
                    <sec:authorize access="isAuthenticated()">
                        <td class="showorhide">
                            <span id="jsshow_${currentspecies.id}" class="jsshow smigolink display${!currentspecies.display}"><spring:message
                                    code="show"/></span>
                            <span id="jshide_${currentspecies.id}" class="jshide smigolink display${currentspecies.display}"><spring:message
                                    code="hide"/></span>
                            <a href="${pageContext.request.contextPath}/update-species?id=${currentspecies.id}"><spring:message code="edit"/></a>
                        </td>
                    </sec:authorize>

                    <c:if test="false">
                        <sec:authorize access="isAuthenticated()">
                            <c:if test="${currentspecies.creator.username != null}">
                                <td class="delete">
                                    <a href="${pageContext.request.contextPath}/deletespecies/${currentspecies.id}"><spring:message
                                            code="delete"/></a>
                                </td>
                            </c:if>
                        </sec:authorize>
                    </c:if>

                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<!-- speciesoverview-->

<sec:authorize access="isAuthenticated()">

    <script type="text/javascript">

        $(function () {
            $('.jsshow').on('click', function () {
                setSpeciesDisplay($(this).extractIdVariable(), true);
            });
            $('.jshide').on('click', function () {
                setSpeciesDisplay($(this).extractIdVariable(), false);
            });
        });

        var settingInProgress = false;

        function setSpeciesDisplay(species, displayValue) {
            if (settingInProgress)
                return;
            settingInProgress = true;

            smigolog("Setting species display for " + species + " to " + displayValue);
            $.ajax({
                url: '${pageContext.request.contextPath}/display',
                type: "GET",
                data: {speciesId: species, display: displayValue},
                success: function (data) {
                    $('#jsspeciesrow_' + species).removeClass('show' + !displayValue).addClass('show' + displayValue);
                    $('#jsshow_' + species).toggle();
                    $('#jshide_' + species).toggle();
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert("Error");
                    smigolog("ajax error> jqXHR=" + jqXHR + " textStatus=" + textStatus + " errorThrown=" + errorThrown);
                },
                complete: function (jqXHR, textStatus) {
                    settingInProgress = false;
                    smigolog("#jsspeciesrow_" + species, $('#jsspeciesrow_' + species));
                }
            });
        }
    </script>

</sec:authorize>

<jsp:include page="footer.jsp"/>