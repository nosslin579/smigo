<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:include page="header.jsp"/>

<spring:message code="general.select" var="selecttranslated"/>
<spring:message code="perennial" var="perennialtranslated"/>
<spring:message code="annual" var="annualtranslated"/>


<div id="speciesform" class="smigoframe largecenteredsmigoframe">
    <div class="smigoframeheader"><spring:message code="addspecies"/></div>
    <div class="smigoframecontent">
        <jsp:useBean id="speciesFormBean" class="org.smigo.species.SpeciesFormBean" scope="request"/>
        <form:form method="post" action="add-species" commandName="speciesFormBean">
            <form:errors path="*" cssClass="errorblock" element="div"/>
            <table id="speciesformtable">
                <tr>
                    <td><form:label path="vernacularName"><spring:message code="name"/></form:label></td>
                    <td><form:input path="vernacularName"/></td>
                    <td><form:errors path="vernacularName" cssClass="errorblock"/></td>
                </tr>
                <tr>
                    <td><form:label path="scientificName"><spring:message code="scientificname"/></form:label></td>
                    <td><form:input path="scientificName"/></td>
                    <td><form:errors path="scientificName" cssClass="errorblock"/></td>
                </tr>
                <tr>
                    <td><form:label path="family"><spring:message code="family"/></form:label></td>
                    <td><form:select path="family">
                        <form:option value="0" label="-- ${selecttranslated} --"/>
                        <form:options items="${families}" itemValue="id" itemLabel="name"/>
                    </form:select></td>
                    <td><form:errors path="family" cssClass="errorblock"/></td>
                </tr>
                <tr>
                    <td><form:label path="annual"><spring:message code="type"/></form:label></td>
                    <td><form:radiobutton path="annual" value="true" label="${annualtranslated}"/><br/>
                        <form:radiobutton path="annual" value="false" label="${perennialtranslated}"/></td>
                    <td><form:errors path="annual" cssClass="errorblock"/></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input id="submit-speciesform-button" type="submit" value="<spring:message code="ok"/>"/>
                        <input type="reset" value="<spring:message code="reset"/>"/>
                        <input type="button" value="<spring:message code="cancel"/>" onclick="history.go(-1)"/>
                    </td>
                </tr>

            </table>
        </form:form>
    </div>
</div>
<!-- smigoframe -->

<jsp:include page="footer.jsp"/>