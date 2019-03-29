<%--
 * button.tag
 *
 * Copyright (C) 2019 a8081 
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@ tag language="java" body-content="empty"%>

<%-- Taglibs --%>

<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<%-- Attributes --%>

<%@ attribute name="status" required="true"%>


<%-- Definition --%>

<jstl:choose>
	<jstl:when test="${status eq 'DEFAULT'}">
		<spring:message code="default" />
	</jstl:when>
	<jstl:when test="${status eq 'ACCEPTED'}">
		<spring:message code="accepted" />
	</jstl:when>
	<jstl:when test="${status eq 'REJECTED'}">
		<spring:message code="rejected" />
	</jstl:when>
	<jstl:when test="${status eq 'SUBMITTED'}">
		<spring:message code="submitted" />
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="invalid.status" />
	</jstl:otherwise>
</jstl:choose>
