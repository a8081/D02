<%--
 * list.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

    <div>
        <input type="button" class="btn btn-danger" name="createFolder"
               value="<spring:message code="general.createFolder" />"
               onclick="relativeRedir('folder/create.do');"/>
    </div>

    <div>
        <input type="button" class="btn btn-danger" name="createMessage"
               value="<spring:message code="message.createMessage" />"
               onclick="relativeRedir('message/create.do');"/>
    </div>
<security:authorize access="hasRole('ADMIN')">
    <div>
        <input type="button" class="btn btn-danger" name="createBroadcast"
               value="<spring:message code="message.createBroadcast" />"
               onclick="relativeRedir('message/broadcast.do');"/>
    </div>
</security:authorize>

	<!-- Listing grid -->
	<display:table pagesize="10" class="displaytag" keepStatus="true"
               name="folders" requestURI="${requestURI}" id="row">
    <display:column>
        <input type="button" class="btn btn-danger" name="open"
               value="<spring:message code="general.open" />"
               onclick="relativeRedir('folder/view.do?folderId=${row.id}');"/>

    </display:column>
    <!-- Attributes -->


    <spring:message var="title" code="folder.name"/>
    <display:column property="name" title="${title}" sortable="true"/>
	
	
           
        <display:column>
        	<jstl:if test="${row.isSystemFolder eq false}">     
            <input type="button" class="btn btn-danger" name="edit"
                   value="<spring:message code="general.edit" />"
                   onclick="relativeRedir('folder/edit.do?folderId=${row.id}');"/>

            <input type="button" class="btn btn-danger" name="edit"
                   value="<spring:message code="general.delete" />"
                   onclick="relativeRedir('folder/delete.do?folderId=${row.id}');"/>
                   </jstl:if>
        </display:column>
        

</display:table>