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


<input type="button" name="create"
				value="<spring:message code="float.create" />"
				onclick="javascript: relativeRedir('float/create.do');" />

<display:table name="floats" id="row"
	requestURI="float/list.do" pagesize="5"
	class="displaytag">


	<display:column property="title" titleKey="float.title" />


	
	<display:column titleKey="float.edit">
		<a href="float/edit.do?floatId=${row.id}"> 
			<spring:message code="float.edit"/>
		</a>
	</display:column>
	
	<display:column titleKey="float.delete">
		<a href="float/delete.do?floatId=${row.id}"> 
			<spring:message code="float.delete"/>
		</a>
	</display:column>

	<display:column titleKey="float.display">
		<a href="float/display.do?floatId=${row.id}"> 
			<spring:message code="float.display"/>
		</a>
	</display:column>

</display:table>


