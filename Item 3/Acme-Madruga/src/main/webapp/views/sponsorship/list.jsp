<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
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

<display:table name="sponsorships" id="row" pagesize="5"
	requestURI="sponsorship/sponsor/list.do" class="displaytag">


	<display:column>
		<acme:link url="sponsorship/sponsor/edit.do?sponsorshipId=${row.id}" code="sponsorship.edit" />
	</display:column>

	<display:column titleKey="sponsorship.targetPage">
		<acme:url url="${row.targetPage}" code="sponsorship.my.targetPage"/>
	</display:column>

	<display:column titleKey="sponsorship.creditCard">
		<jstl:out value="${row.creditCard.number}" />
	</display:column>
	
	<display:column>
		<acme:link url="sponsorship/sponsor/displaySponsorship.do?sponsorshipId=${row.id}" code="sponsorship.display" />
	</display:column>

</display:table>

<br />

<a href="parade/sponsor/list.do">
		<spring:message code="sponsorship.create"/>
	</a>