<%--
 * edit.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
        pageEncoding="ISO-8859-1" %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="security"
          uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<%--
  ~ Copyright © 2017. All information contained here included the intellectual and technical concepts are property of Null Point Software.
  --%>
  
<form:form action="message/move.do" modelAttribute="m">

    <form:hidden path="id"/>
    <form:hidden path="version"/>
    <form:hidden path="moment"/>
    <form:hidden path="sender"/>
    
    <spring:message code="message.subject" var="subject1"/>
	<h3><jstl:out value="${subject1}"/></h3>
	<jstl:out value="${m.subject}"/>
	
	<spring:message code="message.body" var="body1"/>
	<h3><jstl:out value="${body1}"/></h3>
	<jstl:out value="${m.body}"/>
    
    <spring:message code="message.receiver" var="receiver1"/>
	<h3><jstl:out value="${receiver1}"/></h3>
	<jstl:out value="${m.recipients}"/>
	
	<spring:message code="message.priority" var="priority1"/>
	<h3><jstl:out value="${priority1}"/></h3>
	<jstl:out value="${m.priority}"/>
	
	<spring:message code="message.tags" var="tags1"/>
	<h3><jstl:out value="${tags1}"/></h3>
	<jstl:out value="${m.tags}"/>
	
    <display:table pagesize="10" class="displaytag" keepStatus="true"
               name="folders" requestURI="${requestURI}" id="row">

    <!-- Attributes -->

	<display:column>
	<spring:message var="title" code="folder.name"/>
    <display:column property="name" title="${title}" sortable="true"/>
	<input type="button" class="btn btn-danger" name="saveMove"
		value="<spring:message code="general.move" />"
           onclick="relativeRedir('message/saveMove.do?messageId=${m.id}&folderId=${folder.id}&choosedFolderId=${row.id}');"/>
	
	</display:column>
	
</display:table>

    <!---------------------------- BOTONES -------------------------->
	
	
    <input type="button" class="btn btn-danger" name="cancel"
           value="<spring:message code="general.cancel" />"
           onclick="relativeRedir('folder/list.do');"/>

</form:form>