<%--
 * edit.jsp
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

<form:form action="legalRecord/edit.do" modelAttribute="legalRecord">

	<form:hidden path="id"/>
    <form:hidden path="version"/>
    

    <acme:textbox path="title" code="record.title"/>
    <acme:textbox path="description" code="record.description"/>
    <acme:textbox path="legalName" code="record.legalName"/>
    <acme:textbox path="vat" code="record.vat"/><spring:message code="vat.pattern" />
    <acme:textbox path="laws" code="record.laws"/>
   

    <br/>

    <!---------------------------- BOTONES -------------------------->

 	<acme:submit name="save" code="general.save"/>
 	
    <input type="button" class="btn btn-danger" name="cancel"
           value="<spring:message code="general.cancel" />"
           onclick="relativeRedir('history/list.do');"/>



</form:form>