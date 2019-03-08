
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<h2><spring:message code="brotherhood.edit.msg"/></h2>
<jstl:if test="${not empty alert}">
	<script>
	 $(document).ready(function() {
		 alert('<spring:message code="${alert}"/>');
	    });
		
	</script>
</jstl:if>

<jstl:if test="${not empty errors}">
	<div class="errorDiv">
		<ul>
			<jstl:forEach items="${errors}" var="error">
				<li><spring:message code="brotherhood.edit.${error.field}"/> - <jstl:out value="${error.defaultMessage}" /></li>
			</jstl:forEach>
		</ul>
	</div>
</jstl:if>


<form:form action="brotherhood/assignArea.do" modelAttribute="brotherhood" method="POST">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<acme:select items="${areas}" itemLabel="name" code="brotherhood.area" path="area" />

	<br>
	<input type="submit" name="saveArea" value="<spring:message code="enrolment.save" />" />
		
	<acme:button url="brotherhood/display.do" name="cancel" code="enrolment.cancel" />
		
</form:form>
