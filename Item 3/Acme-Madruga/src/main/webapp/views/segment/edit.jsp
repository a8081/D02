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

<jstl:if test="${not empty errors}">
	<div class="errorDiv">
		<ul>
			<jstl:forEach items="${errors}" var="error">
			<jstl:choose>
			<jstl:when test="${(error.field eq 'destinationCoordinates.longitude') or (error.field eq 'destinationCoordinates.latitude') or (error.field eq 'originCoordinates.longitude') or (error.field eq 'originCoordinates.latitude')}">
					<li><spring:message code="segment.edit.${error.field}" /> -
					<jstl:out value="${error.defaultMessage}" /></li>
			
			</jstl:when>
			<jstl:otherwise>
				<spring:message code="parade.commit.error"/>
			</jstl:otherwise>
			</jstl:choose>
			</jstl:forEach>
		</ul>
	</div>
</jstl:if>

<form:form action="segment/brotherhood/edit.do" modelAttribute="segment">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<input type="hidden" name="paradeId" value="${paradeId}" />

	<jstl:choose>
		<jstl:when test="${not empty suggestOriginTime}">
			<fmt:formatDate var="formateada" value="${suggestOriginTime}"
				pattern="yyyy-MM-dd HH:mm" />
			<form:hidden path="originTime" value="${formateada}" />
			<spring:message code="segment.originTime" />: <jstl:out
				value="${suggestOriginTime}" />
		</jstl:when>
		<jstl:otherwise>
			<acme:textbox code="segment.originTime" path="originTime"
				placeholder="yyyy-MM-dd HH:mm" />
			<spring:message code="date.pattern" />
		</jstl:otherwise>
	</jstl:choose>

	<acme:textbox code="segment.destinationTime" path="destinationTime"
		placeholder="yyyy-MM-dd HH:mm" />
	<spring:message code="date.pattern" />
	<br>
	<jstl:choose>
		<jstl:when test="${not empty suggestOriginCoordinates}">
			<form:hidden path="originCoordinates"
				value="${suggestOriginCoordinates.latitude}|${suggestOriginCoordinates.longitude}|" />
			<spring:message code="segment.originCoordinates.latitude" />: <jstl:out
				value="${suggestOriginCoordinates.latitude}" />
			<br>
			<spring:message code="segment.originCoordinates.longitude" />: <jstl:out
				value="${suggestOriginCoordinates.longitude}" />
		</jstl:when>
		<jstl:otherwise>
			<acme:textbox code="segment.originCoordinates"
				path="originCoordinates" />
		</jstl:otherwise>
	</jstl:choose>

	<acme:textbox code="segment.destinationCoordinates"
		path="destinationCoordinates" placeholder="0.0|0.0|" />
	<spring:message code="coordinate.pattern" />


	<br>
	<input type="submit" name="save"
		value="<spring:message code="segment.save" />" />

	<acme:button url="parade/brotherhood/display.do?paradeId=${paradeId}"
		name="cancel" code="segment.cancel" />

	<jstl:if test="${isLastSegment}">
		<input type="submit" name="delete"
			value="<spring:message code="segment.delete" />" />
	</jstl:if>

</form:form>

