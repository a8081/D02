<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

	
<jstl:choose>
	<jstl:when test="${lang eq 'en' }">
		<spring:message code="segment.originTime" />: <fmt:formatDate
			value="${segment.originTime}" type="both" pattern="yyyy/MM/dd HH:mm" />
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="segment.originTime" />: <fmt:formatDate
			value="${segment.originTime}" type="both" pattern="dd/MM/yyyy HH:mm" />
	</jstl:otherwise>
</jstl:choose>
<br>

<jstl:choose>
	<jstl:when test="${lang eq 'en' }">
		<spring:message code="segment.destinationTime" />: <fmt:formatDate
			value="${segment.destinationTime}" type="both" pattern="yyyy/MM/dd HH:mm" />
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="segment.destinationTime" />: <fmt:formatDate
			value="${segment.destinationTime}" type="both" pattern="dd/MM/yyyy HH:mm" />
	</jstl:otherwise>
</jstl:choose>
<br>



<acme:display code="segment.originCoordinates.latitude" value="${segment.originCoordinates.latitude}" />
<acme:display code="segment.originCoordinates.longitude" value="${segment.originCoordinates.longitude}" />

<acme:display code="segment.destinationCoordinates.latitude" value="${segment.destinationCoordinates.latitude}" />
<acme:display code="segment.destinationCoordinates.longitude" value="${segment.destinationCoordinates.longitude}" />
<br>

	<jstl:choose>
		<jstl:when test="${rol eq brotherhood }">
			<acme:button url="parade/brotherhood/display.do?paradeId=${parade.id}" name="cancel" code="segment.back"/>
		</jstl:when>
		
		<jstl:otherwise>
			<acme:button url="parade/all/display.do?paradeId=${parade.id}" name="cancel" code="segment.back"/>
		</jstl:otherwise>

	</jstl:choose>
