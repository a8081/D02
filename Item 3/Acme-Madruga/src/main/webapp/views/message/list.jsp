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
	
	<h3><jstl:out value="${folder.name}"/></h3>

	<jstl:if test="${folder.isSystemFolder == false}">
	<div>
        <input type="button" class="btn btn-danger" name="createFolder"
               value="<spring:message code="general.createFolder" />"
               onclick="relativeRedir('folder/createInFolder.do?fatherId=${folder.id}');"/>
    </div>
    </jstl:if>

<!-- Listing grid -->
<jstl:if test="${not empty folders}">
<display:table pagesize="5" class="displaytag" keepStatus="true"
               name="folders" requestURI="${requestURI}" id="row">
	<display:column>
        <input type="button" class="btn btn-danger" name="open"
               value="<spring:message code="general.open" />"
               onclick="relativeRedir('folder/view.do?folderId=${row.id}');"/>
    </display:column>
    <spring:message var="title" code="folder.name"/>
    <display:column property="name" title="${title}" sortable="true"/>
                        
        <display:column>
            <input type="button" class="btn btn-danger" name="edit"
                   value="<spring:message code="general.edit" />"
                   onclick="relativeRedir('folder/edit.do?folderId=${row.id}');"/>

            <input type="button" class="btn btn-danger" name="edit"
                   value="<spring:message code="general.delete" />"
                   onclick="relativeRedir('folder/delete.do?folderId=${row.id}');"/>
        </display:column>

	               
</display:table>
</jstl:if>

<jstl:if test="${not empty m}">
<display:table pagesize="5" class="displaytag" keepStatus="true"
               name="m" requestURI="${requestURI}" id="row">

    <!-- Attributes -->

        <display:column>
            <input type="button" class="btn btn-danger" name="deleteMessage"
                   value="<spring:message code="general.delete" />"
                   onclick="relativeRedir('message/delete.do?messageId=${row.id}&folderId=${folder.id}');"/>
        </display:column>

	<display:column property="subject" titleKey="message.subject" />
	<display:column property="sender.name" value="name" titleKey="message.sender" />
	<display:column property="recipients"  value="name" titleKey="message.recipients"/>
	<display:column property="priority" titleKey="message.priority" />
	
	<display:column>
            <input type="button" class="btn btn-danger" name="moveMessage"
                   value="<spring:message code="general.move" />"
                   onclick="relativeRedir('message/move.do?messageId=${row.id}&folderId=${folder.id}');"/>
    </display:column>
    
    <display:column>
            <input type="button" class="btn btn-danger" name="copyMessage"
                   value="<spring:message code="general.copy" />"
                   onclick="relativeRedir('message/copy.do?messageId=${row.id}&folderId=${folder.id}');"/>
    </display:column>
    
    <display:column>
            <input type="button" class="btn btn-danger" name="displayMessage"
                   value="<spring:message code="general.display" />"
                   onclick="relativeRedir('message/display.do?messageId=${row.id}&folderId=${folder.id}');"/>
    </display:column>
	
</display:table>
</jstl:if>

<br>

<input type="button" class="btn btn-danger" name="back"
       value="<spring:message code="general.cancel" />"
       onclick="relativeRedir('folder/list.do');"/>
