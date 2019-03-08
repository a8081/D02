<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<jstl:if test="${not empty alert}">
	<script>
		alert('<spring:message code="${alert}"/>');
	</script>
</jstl:if>


<form:form modelAttribute="socialProfile" action="socialProfile/edit.do" method="POST">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="actor"/>
	
	<acme:textbox code="socialProfile.nick" path="nick" />
	<acme:textbox code="socialProfile.socialNetwork" path="socialNetwork" />
	<acme:textbox code="socialProfile.link" path="profileLink" />
	<acme:submit code="socialProfile.edit.submit" name="submit"/>
</form:form>