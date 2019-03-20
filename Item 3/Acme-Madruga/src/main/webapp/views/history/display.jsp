<%@page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
    uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<jstl:if test="${not empty history}">
    <%-- History --%>
   
    <!-- Inception Record -->
    <div id="inceptionRecord">
        <ul style="list-style-type: disc">
            <li><b><spring:message code="inceptionRecord"/></b>
        </ul>
    </div>
	<display:table pagesize="10" class="displaytag" keepStatus="true"
               name="inceptionRecord" requestURI="${requestURI}" id="row">
    <!-- Attributes -->
	
    <spring:message var="title" code="record.title"/>
    <display:column property="title" title="${title}" sortable="true"/>
    <spring:message var="description" code="record.description"/>
    <display:column property="description" title="${description}" sortable="true"/>
	<display:column>
                <input type="button" name="edit"
                    value="<spring:message code="record.edit" />"
                    onclick="relativeRedir('inceptionRecord/edit.do?inceptionRecordId=${row.id}')" />
	</display:column>
        
	</display:table>

   <%-- Period records --%>
   <div id="periodRecord">
        <ul style="list-style-type: disc">
            <li><b><spring:message code="periodRecords"/></b>
        </ul>
    </div>
    <input type="button" name="create"
    value="<spring:message code="record.create" />"
    onclick="relativeRedir('periodRecord/create.do')" />
    <br />
    <display:table pagesize="10" class="displaytag" keepStatus="true"
               name="history.periodRecords" requestURI="${requestURI}" id="row">
    <!-- Attributes -->
	
    <spring:message var="title" code="record.title"/>
    <display:column property="title" title="${title}" sortable="true"/>
    <spring:message var="description" code="record.description"/>
    <display:column property="description" title="${description}" sortable="true"/>
    <spring:message var="startYear" code="periodRecord.starYear"/>
    <display:column property="startYear" title="${startYear}" sortable="true"/>
    <spring:message var="endYear" code="periodRecord.endYear"/>
    <display:column property="endYear" title="${endYear}" sortable="true"/>
	<display:column>
            <input type="button" name="edit"
                value="<spring:message code="record.edit" />"
                onclick="relativeRedir('periodRecord/edit.do?periodRecordId=${row.id}')" />
	</display:column>
	<display:column>
			<input type="button" name="delete"
                value="<spring:message code="record.delete" />"
                onclick="relativeRedir('periodRecord/delete.do?periodRecordId=${row.id}')" />
	</display:column>
        
	</display:table>
	
	<%-- Legal records --%>
	<div id="legalRecord">
        <ul style="list-style-type: disc">
            <li><b><spring:message code="legalRecords"/></b>
        </ul>
    </div>
    <input type="button" name="create"
    value="<spring:message code="record.create" />"
    onclick="relativeRedir('legalRecord/create.do')" />
    <br />
    <display:table pagesize="10" class="displaytag" keepStatus="true"
               name="history.legalRecords" requestURI="${requestURI}" id="row">
    <!-- Attributes -->
	
    <spring:message var="title" code="record.title"/>
    <display:column property="title" title="${title}" sortable="true"/>
    <spring:message var="description" code="record.description"/>
    <display:column property="description" title="${description}" sortable="true"/>
    <spring:message var="legalName" code="legalRecord.legalName"/>
    <display:column property="legalName" title="${legalName}" sortable="true"/>
    <spring:message var="laws" code="legalRecord.laws"/>
    <display:column property="laws" title="${laws}" sortable="true"/>
	<display:column>
            <input type="button" name="edit"
                value="<spring:message code="record.edit" />"
                onclick="relativeRedir('legalRecord/edit.do?legalRecordId=${row.id}')" />
	</display:column>
	<display:column>
			<input type="button" name="delete"
                value="<spring:message code="record.delete" />"
                onclick="relativeRedir('legalRecord/delete.do?legalRecordId=${row.id}')" />
	</display:column>
        
	</display:table>
	
	<%-- Link records --%>
	<div id="linkRecords">
        <ul style="list-style-type: disc">
            <li><b><spring:message code="linkRecords"/></b>
        </ul>
    </div>
    <input type="button" name="create"
    value="<spring:message code="record.create" />"
    onclick="relativeRedir('linkRecord/create.do')" />
    <br />
    <display:table pagesize="10" class="displaytag" keepStatus="true"
               name="history.linkRecords" requestURI="${requestURI}" id="row">
    <!-- Attributes -->
	
    <spring:message var="title" code="record.title"/>
    <display:column property="title" title="${title}" sortable="true"/>
    <spring:message var="description" code="record.description"/>
    <display:column property="description" title="${description}" sortable="true"/>
    <spring:message var="linkedBrotherhood" code="linkRecords.linkedBrotherhood"/>
    <display:column property="linkedBrotherhood" title="${linkedBrotherhood}" sortable="true"/>
	<display:column>
            <input type="button" name="edit"
                value="<spring:message code="record.edit" />"
                onclick="relativeRedir('linkRecord/edit.do?linkRecordId=${row.id}')" />
	</display:column>
	<display:column>
			<input type="button" name="delete"
                value="<spring:message code="record.delete" />"
                onclick="relativeRedir('linkRecord/delete.do?linkRecordId=${row.id}')" />
	</display:column>
        
	</display:table>
	
	<%-- Miscellaneous records --%>
	<div id="miscellaneousRecords">
        <ul style="list-style-type: disc">
            <li><b><spring:message code="miscellaneousRecords"/></b>
        </ul>
    </div>
    <input type="button" name="create"
    value="<spring:message code="record.create" />"
    onclick="relativeRedir('miscellaneousRecord/create.do')" />
    <br />
    <display:table pagesize="10" class="displaytag" keepStatus="true"
               name="history.miscellaneousRecords" requestURI="${requestURI}" id="row">
    <!-- Attributes -->
	
    <spring:message var="title" code="record.title"/>
    <display:column property="title" title="${title}" sortable="true"/>
    <spring:message var="description" code="record.description"/>
    <display:column property="description" title="${description}" sortable="true"/>
	<display:column>
            <input type="button" name="edit"
                value="<spring:message code="record.edit" />"
                onclick="relativeRedir('miscellaneousRecord/edit.do?miscellaneousRecordId=${row.id}')" />
	</display:column>
	<display:column>
			<input type="button" name="delete"
                value="<spring:message code="record.delete" />"
                onclick="relativeRedir('miscellaneousRecord/delete.do?miscellaneousRecordId=${row.id}')" />
	</display:column>
        
	</display:table>

</jstl:if>