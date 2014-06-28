<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="msg" uri="http://smigo.org/jsp/msg" %>

<jsp:include page="header.jsp"/>

<div id="speciesinfo" class="largecenteredsmigoframe">

    <div class="selectspeciestooltipcontainer smigoframe">
        <div class="speciestranslation smigoframeheader"><spring:message code="${msg:species(species.getId())}"/></div>
        <div class="smigoframecontent">
            <div class="speciesscientificname">${species.scientificName}</div>
            <div class="speciesicon"><img src="/pic/${species.iconFileName}"/></div>
            <div class="speciesactions">
                <sec:authorize access="isAuthenticated()">
                    <c:if test="${not species.item}">
                        <a href="${pageContext.request.contextPath}/addrule?species=${species.id}"><spring:message code="rule.add"/></a>
                    </c:if>
                    <c:if test="${species.creator != null}">
                        <a href="${pageContext.request.contextPath}/deletespecies/${species.id}"><spring:message code="delete"/></a>
                    </c:if>
                </sec:authorize>
            </div>

            <c:if test="${!species.item}">
                <div class="familyheader"><spring:message code="family"/></div>
                <div class="familyitem"><spring:message code="${species.family.translationKey}"/>
                    - ${species.family.name}</div>
                <div class="typeheader"><spring:message code="type"/></div>
                <div class="typeitem"><spring:message code="${species.annual  ? 'annual' : 'perennial'}"/></div>


                <c:if test="${!empty species.companionPlantingRules}">
                    <div class="companionplantingheader ruleheader"><spring:message code="companionplanting"/></div>
                    <c:forEach var="currentRule" items="${species.companionPlantingRules}">
                        <div id="jsrule_${currentRule.id}" class="companionplantinghint ruleitem floatcontainer">
                            <span class="left">
                                <c:out value="${currentRule.info}"/>
                            </span>
                            <sec:authorize access="isAuthenticated()">
                                <span id="visibilitychanger_${currentRule.id}" class="right visibilitychanger smigolink">
                                    <spring:message code="delete"/>
                                </span>
                            </sec:authorize>
                        </div>
                    </c:forEach> <!-- end hint -->
                </c:if>


                <c:if test="${!empty species.cropRotationRules}">
                    <div class="croprotationheader ruleheader"><spring:message code="croprotation"/></div>
                    <c:forEach var="currentRule" items="${species.cropRotationRules}">
                        <div id="jsrule_${currentRule.id}" class="croprotationhint ruleitem floatcontainer">
                            <span class="left">
                                <c:out value="${currentRule.info}"/>
                            </span>
                            <sec:authorize access="isAuthenticated()">
                            <span id="visibilitychanger_${currentRule.id}" class="right visibilitychanger smigolink">
                                <spring:message code="delete"/>
                            </span>
                            </sec:authorize>
                        </div>
                    </c:forEach> <!-- end hint -->
                </c:if>


            </c:if>

        </div>
    </div>
    <!-- end tooltip -->

</div>
<!-- middlewindow-->
<sec:authorize access="isAuthenticated()">
    <script type="text/javascript">
        $(function () {
            $('.visibilitychanger').on('click', function () {
                var $this = $(this);
                $this.css('text-decoration', 'line-through').unbind('click');
                var rule = parseInt($this.attr('id').split('_')[1]);
                smigolog("Deleting " + rule);
                $.ajax({
                    url: '${pageContext.request.contextPath}/deleterule',
                    type: "GET",
                    data: {ruleId: rule},
                    success: function (data) {
                        $('#jsrule_' + rule).remove();
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        alert("Error");
                        smigolog("ajax error> jqXHR=" + jqXHR + " textStatus=" + textStatus + " errorThrown=" + errorThrown);
                    },
                    complete: function (jqXHR, textStatus) {
                        smigolog("#jsrule_" + rule, $("#jsrule_" + rule));
                    }
                });
            });
        });
    </script>
</sec:authorize>

<jsp:include page="footer.jsp"/>