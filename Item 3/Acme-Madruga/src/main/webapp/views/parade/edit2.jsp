
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<jstl:if test="${ok}">
<form:form action="parade/chapter/edit.do" modelAttribute="parade" method="POST">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="status" value="SUBMITTED"/>

	<acme:textarea code="parade.rejectionReason" path="rejectionReason"/>

	<br>
	<input type="submit" name="reject" value="<spring:message code="parade.save" />" />
	
		
	<acme:button url="parade/chapter/listSubmitted.do" name="cancel" code="parade.cancel" />
		
</form:form>
</jstl:if>