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


<form:form action="enrolment/brotherhood/edit.do" modelAttribute="enrolment" method="POST">

	<form:hidden path="id" />
	<form:hidden path="version" />

<jstl:choose>
	<jstl:when test="${lang eq 'en' }">
		<acme:select items="${positions}" itemLabel="nameEnglish" code="enrolment.positions" path="position" />
	</jstl:when>
	<jstl:otherwise>
		<acme:select items="${positions}" itemLabel="nameSpanish" code="enrolment.positions" path="position" />
	</jstl:otherwise>
</jstl:choose>

	<br>
	<input type="submit" name="save" value="<spring:message code="enrolment.save" />" />
		
	<acme:button url="member/list.do" name="cancel" code="enrolment.cancel" />
		
</form:form>