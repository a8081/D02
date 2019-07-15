
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form modelAttribute="float" action="float/edit.do" method="POST">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="brotherhood"/>
	
	<acme:textbox code="float.title" path="title"/>
	<acme:textarea code="float.description" path="description"/>
	<acme:textarea code="float.pictures" path="pictures"/>
	<acme:submit name="submit" code="float.save"/>
</form:form>