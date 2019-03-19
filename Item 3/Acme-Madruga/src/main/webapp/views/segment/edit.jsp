<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="segment/brotherhood/edit.do" modelAttribute="segment">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<acme:textbox code="segment.originTime" path="originTime" placeholder="yyyy-MM-dd HH:mm"/> <spring:message code="date.pattern" />
	
	<acme:textbox code="segment.destinationTime" path="destinationTime" placeholder="yyyy-MM-dd HH:mm"/> <spring:message code="date.pattern" />
	
	<acme:textbox code="segment.originCoordinates" path="originCoordinates"/>
	
	<acme:textbox code="segment.destinationCoordinates" path="destinationCoordinates"/> 
	


	<input type="submit" name="save"
		value="<spring:message code="segment.save" />" />
	
	<acme:button url="segment/brotherhood/list.do" name="cancel"
		code="segment.cancel" />

</form:form>