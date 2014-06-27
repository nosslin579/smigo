<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="msg" uri="http://smigo.org/jsp/msg" %>

<jsp:include page="header.jsp"/>

<spring:message code="general.select" var="mselect"/>

<div id="ruleform" class="smigoframe largecenteredsmigoframe">
    <spring:message var="hostTranslation" code="${msg:species(host)}"/>
    <div class="smigoframeheader"><spring:message code="rule.addto" arguments="${hostTranslation}"/></div>
    <div class="smigoframecontent">
        <form:form method="post" action="addrule" commandName="ruleFormModel" enctype="multipart/form-data">
            <form:errors path="*" cssClass="errorblock" element="div"/>
            <table id="ruleformtable">

                <tr>
                    <td><input name="host" value="${host.id}" type="hidden"/></td>
                </tr>

                <tr id="typeParameter">
                    <td><form:label path="type"><spring:message code="rule.type"/>:</form:label></td>
                    <td><form:select path="type" id="typeselectlist">
                        <form:option value="-1" label="-- ${mselect} --"/>
                        <c:forEach items="${ruleTypes}" var="ruleType">
                            <option value="${ruleType.id}"><spring:message code="${ruleType.translationKey}"/></option>
                        </c:forEach>
                    </form:select></td>
                    <td><form:errors path="type" cssClass="errorblock"/></td>
                </tr>

                <tr id="speciesParameter" class="toggleoff">
                    <td><form:label path="causer"><spring:message code="general.species"/>:</form:label></td>
                    <td><form:select path="causer">
                        <form:option value="-1" label="-- ${mselect} --"/>
                        <form:options items="${listofallspecies}" itemValue="id" itemLabel="translation"/>
                    </form:select></td>
                    <td><form:errors path="causer" cssClass="errorblock"/></td>
                </tr>

                <tr id="gapParameter" class="toggleoff">
                    <td><form:label path="gap"><spring:message code="rule.gap"/>:</form:label></td>
                    <td><form:select path="gap">
                        <c:forEach var="number" begin="1" end="20">
                            <option value="${number}">${number}</option>
                        </c:forEach>
                    </form:select></td>
                    <td><form:errors path="gap" cssClass="errorblock"/></td>
                </tr>

                <tr id="familyParameter" class="toggleoff">
                    <td><form:label path="causerfamily"><spring:message code="family"/>:</form:label></td>
                    <td><form:select path="causerfamily">
                        <form:option value="0" label="-- ${mselect} --"/>
                        <form:options items="${families}" itemValue="id" itemLabel="name"/>
                    </form:select></td>
                    <td><form:errors path="causerfamily" cssClass="errorblock"/></td>
                </tr>


                <tr>
                    <td colspan="3">
                        <div class="buttons">
                            <input type="submit" value="<spring:message code="ok"/>"/>
                            <input type="reset" value="<spring:message code="reset"/>"/>
                            <input type="button" value="<spring:message code="cancel"/>" onclick="history.go(-1)"/>
                        </div>
                    </td>
                </tr>

            </table>
        </form:form>
    </div>
</div>

<script type="text/javascript" charset="utf-8">
    $(function () {
    });
    $('#typeselectlist').on('change', function () {
        var $this = $(this);
        var typeValue = $this.val();
        smigolog("Selectlist new value:" + typeValue);
        $('.toggleoff').toggle(false);
        if (typeValue <= 4) {
            $('#speciesParameter').toggle(true);
        } else if (typeValue == 5 || typeValue == 6) {
            $('#familyParameter').toggle(true);
        } else if (typeValue == 7) {
            $('#gapParameter').toggle(true);
        }
    });

</script>

<jsp:include page="footer.jsp"/>