<%--
 * action-2.jsp
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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<form:form action="proclaim/chapter/edit.do" modelAttribute="proclaim" method="POST">
    
    <form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="moment"/>
	<form:hidden path="text"/>
	<form:hidden path="chapter"/>
	
	<form:label path="text">
		<spring:message code="proclaim.text" />: </form:label>
	<form:input path="keyword" />
	<form:errors cssClass="error" path="keyword" />
	<br />
	<br>
	<input type="submit" name="save" value="<spring:message code="proclaim.save" />" />
	<br>
		
</form:form>