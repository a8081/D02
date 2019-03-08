<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<style type="text/css">
.PENDING {
	color: grey;
}

.APPROVED {
	color: green;
}

.REJECTED {
	color: orange;
}
</style>

<jstl:if test="${not empty rol}">
	<jstl:set var="rolURL" value="/${rol}" />
</jstl:if>

<jstl:if test="${not empty request.member.middleName}">
	<jstl:set var="middlename" value=" ${request.member.middleName}" />
</jstl:if>

<acme:display code="request.procession.title" value="${request.procession.title}"/>
<acme:display code="request.member.name" value="${request.member.name}${middlename} ${request.member.surname}"/>

<jstl:choose>
	<jstl:when test="${lang eq 'en' }">
		<spring:message code="request.moment" />: <fmt:formatDate
			value="${request.moment}" type="both" pattern="yyyy/MM/dd HH:mm" />
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="request.moment" />: <fmt:formatDate
			value="${request.moment}" type="both" pattern="dd/MM/yyyy HH:mm" />
	</jstl:otherwise>
</jstl:choose>
</br>

<spring:message code="request.status" />:
<span class="${request.status}">
	<acme:statusChoose status="${request.status}"/>
</span>
<br />


<jstl:choose>
	<jstl:when test="${request.status eq 'REJECTED'}">
		<acme:display code="request.explanation" value="${request.explanation}"/>
	</jstl:when>
	<jstl:when test="${request.status eq 'APPROVED'}">
		<acme:display code="request.row" value="${request.row}"/>
		<acme:display code="request.column" value="${request.column}"/>
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="request.status.pending"/></br></br>
		<security:authorize access="hasRole('MEMBER')">
			<form:form action="request/member/edit.do" modelAttribute="request">
			<form:hidden path="id"/>
			<form:hidden path="version"/>
			<acme:submit name="delete" code="request.delete"/></form:form>
		</security:authorize>
	</jstl:otherwise>
</jstl:choose>
<br />
<br />

<acme:button name="back" code="request.list.button" url="request${rolURL}/list.do"/>

