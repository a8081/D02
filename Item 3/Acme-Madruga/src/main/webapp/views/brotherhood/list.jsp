<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<display:table name="brotherhoods" id="row"
	requestURI="${requestURI}" pagesize="5"
	class="displaytag">

	<security:authorize access="hasRole('MEMBER')">
	<jstl:if test="${not empty row.area and ok}">
		<display:column>
			<a href="enrolment/member/create.do?brotherhoodId=${row.id}">
				<spring:message code="brotherhood.enrolment.edit" />
			</a>
		</display:column>
	</jstl:if>
	<jstl:if test="${leave}">
		<display:column>
			<a href="member/leave.do?brotherhoodId=${row.id}">
				<spring:message code="brotherhood.member.leave" />
			</a>
		</display:column>
	</jstl:if>
	</security:authorize>

	<display:column property="name" titleKey="actor.name" />
	
	<display:column property="title" titleKey="actor.title" />
	
	<acme:dataTableColumn code="actor.date" property="date" />
	
	<jstl:if test="${displayEnrolment}">
	<display:column>
	<a href="enrolment/member/display.do?brotherhoodId=${row.id}"> <spring:message
				code="member.enrolment"/>
		</a>
	</display:column>
	</jstl:if>
	
	<display:column>
		<a href="brotherhood/displayTabla.do?brotherhoodId=${row.id}"> <spring:message
				code="brotherhood.display" />
		</a>
	</display:column>


</display:table>
