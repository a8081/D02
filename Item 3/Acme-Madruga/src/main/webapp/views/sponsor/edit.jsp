
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<h2><spring:message code="sponsor.edit.msg"/></h2>
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
					<li><spring:message code="sponsor.edit.${error.field}"/> - <jstl:out value="${error.defaultMessage}" /></li>
				</jstl:forEach>
			</ul>
		</div>
	</jstl:if>



<form:form modelAttribute="actorForm" action="sponsor/edit.do" method="POST">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<acme:textbox code="sponsor.edit.userAccountuser" path="userAccountuser" />
	<acme:password code="sponsor.edit.userAccountpassword" path="userAccountpassword" />

	<acme:textbox code="sponsor.edit.name" path="name" />
	<acme:textbox code="sponsor.edit.middleName" path="middleName" />
	<acme:textbox code="sponsor.edit.surname" path="surname" />
	<acme:textbox code="sponsor.edit.photo" path="photo" />
	<acme:textbox code="sponsor.edit.email" path="email" />
	<acme:textbox code="sponsor.edit.phone" path="phone" />
	<acme:textbox code="sponsor.edit.address" path="address" />

	<jstl:choose>
	    <jstl:when test="${actorForm.termsAndCondicions == true}">
	        <form:hidden path="termsAndCondicions"/>
	    </jstl:when>    
	    <jstl:otherwise>
			<form:checkbox path="termsAndCondicions"/><a href="profile/terms.do"><spring:message code="edit.accepted"/> <spring:message code="edit.termsAndConditions"/></a>
			<br>
	    </jstl:otherwise>
	</jstl:choose>

	<acme:submit code="sponsor.edit.submit" name="save"/>
</form:form>