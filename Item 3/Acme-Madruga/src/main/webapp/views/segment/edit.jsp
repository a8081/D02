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
	<form:hidden path="paradeId" value="${paradeId}"/>
	
	<jstl:choose>
		<jstl:when test="${not empty suggestOriginTime}">
			<acme:display code="segment.originTime" value="${suggestOriginTime}"/>
		</jstl:when>
		
		<jstl:otherwise>
			<acme:textbox code="segment.originTime" path="originTime" placeholder="yyyy-MM-dd HH:mm"/> <spring:message code="date.pattern" />
		</jstl:otherwise>
	</jstl:choose>
	
	<acme:textbox code="segment.destinationTime" path="destinationTime" placeholder="yyyy-MM-dd HH:mm"/> <spring:message code="date.pattern" />
	<br>
	<jstl:choose>
		<jstl:when test="${not empty suggestOriginCoordinates}">
			<acme:display code="segment.originCoordinates.latitude" value="${suggestOriginCoordinates.latitude}" />
			<acme:display code="segment.originCoordinates.longitude" value="${suggestOriginCoordinates.longitude}" />
		</jstl:when>
		
		<jstl:otherwise>
			<acme:textbox code="segment.originCoordinates" path="originCoordinates"/>
		</jstl:otherwise>
	</jstl:choose>

	<acme:textbox code="segment.destinationCoordinates" path="destinationCoordinates" placeholder="0.0|0.0|"/> <spring:message code="coordinate.pattern" />
	

<br>
	<input type="submit" name="save"
		value="<spring:message code="segment.save" />" />
	
	<acme:button url="segment/brotherhood/list.do" name="cancel"
		code="segment.cancel" />

</form:form>