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

<form:form action="sponsorship/sponsor/edit.do"
	modelAttribute="sponsorship">

<jstl:choose>
	<jstl:when test="${sponsorship.id != 0}">
		<form:hidden path="id" />
		<form:hidden path="version" />
		<form:hidden path="parade" value="${sponsorship.parade.id}" />
	</jstl:when>
	<jstl:otherwise>
		<form:hidden path="parade" value="${parade.id}" />
	</jstl:otherwise>
</jstl:choose>
	
	<form:hidden path="activated" value="${sponsorship.activated}"/>
	
	<acme:textbox code="sponsorship.banner" path="banner"/>
	<acme:textbox code="sponsorship.targetPage" path="targetPage"/>
	<acme:textbox code="sponsorship.creditCard.holderName" path="holderName"/>

	<form:label path="make">
		<spring:message code="sponsorship.creditCard.brandName" />
	</form:label>	
	<form:select  path="make">
		<form:options items="${makes}"/>
	</form:select>
	<form:errors path="make" cssClass="error" />
	
	<acme:textbox code="sponsorship.creditCard.number" path="number"/>
	<acme:textbox code="sponsorship.creditCard.expirationMonth" path="expirationMonth" placeholder="09"/>
	<acme:textbox code="sponsorship.creditCard.expirationYear" path="expirationYear" placeholder="21"/>
	<acme:textbox code="sponsorship.creditCard.cvv" path="cvv"/>
	<br />

	<input type="submit" name="save" value="<spring:message code="sponsorship.save"/> " />
	<br/>
</form:form>

<jstl:if test="${sponsorship.id != 0}">
		<jstl:choose>
			<jstl:when test="${sponsorship.activated eq true}">
				<acme:button url="sponsorship/sponsor/deactivate.do?sponsorshipId=${sponsorship.id}" name="deactivate" code="sponsorship.deactivate"/>
			</jstl:when>
			<jstl:otherwise>
				<acme:button url="sponsorship/sponsor/reactivate.do?sponsorshipId=${sponsorship.id}" name="reactivate" code="sponsorship.reactivate"/>
			</jstl:otherwise>
		</jstl:choose>
	</jstl:if>
	
	<input type="button" name="cancel"
		value="<spring:message code="sponsorship.cancel"/>" 
		onclick="javascript: relativeRedir('#');"/>
