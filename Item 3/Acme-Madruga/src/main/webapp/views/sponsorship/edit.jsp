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

<form:form action="sponsorship/sponsor/edit.do"
	modelAttribute="sponsorship">

	<jstl:if test="${sponsorship.id != 0}">
		<form:hidden path="id" />
		<form:hidden path="version" />
	</jstl:if>

	<form:hidden path="tutorials" />

	<form:label path="banner">
		<spring:message code="sponsorship.banner" />
	</form:label>
	<form:input path="banner" />
	<form:errors cssClass="error" path="banner" />
	<br />
	<form:label path="targetPage">
		<spring:message code="sponsorship.targetPage" />
	</form:label>
	<form:input path="targetPage" />
	<form:errors cssClass="error" path="targetPage" />


	<br />

	<form:label path="creditCard">
		<spring:message code="application.creditCard" />:
			</form:label>
	<form:select id="cards" path="creditCard">
		<jstl:forEach var="card" items="${creditCards}">
			<form:option value="${card.id}">
				<jstl:out value="${card.brandName } - number: ${card.number}" />
			</form:option>
		</jstl:forEach>
	</form:select>
	<form:errors cssClass="error" path="creditCard" />
	<br />

	<jstl:if test="${sponsorship.id != 0}">
		<spring:message code="sponsorship.tutorials" />:
		<ul>
			<jstl:forEach var="tutorial" items="${sponsorship.tutorials}">
				<li><a href="tutorial/display.do?tutorialId=${tutorial.id}">${tutorial.title}</a>
					- <a href="sponsorship/sponsor/remove.do?sponsorshipId=${sponsorship.id}&tutorialId=${tutorial.id}"><spring:message
							code="sponsorship.deselect" /></a></li>
			</jstl:forEach>
		</ul>
	</jstl:if>

	<br />

	<input type="submit" name="save"
		value="<spring:message code="sponsorship.save"/> " />
	<jstl:if test="${sponsorship.id != 0}">
		<input type="submit" name="delete"
			value="<spring:message code="sponsorship.delete"/>" />
	</jstl:if>
	<input type="button" name="cancel"
		value="<spring:message code="sponsorship.cancel"/>" 
		onclick="javascript: relativeRedir('#');"/>

</form:form>