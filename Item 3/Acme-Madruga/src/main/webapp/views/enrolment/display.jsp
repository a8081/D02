<%--
 * action-2.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<acme:display code="enrolment.member" value="${enrolment.member.name}"/>
<acme:display code="enrolment.brotherhood" value="${enrolment.brotherhood.name}"/>

<jstl:choose>
	<jstl:when test="${lang eq 'en' }">
		<spring:message code="enrolment.moment" />: <fmt:formatDate
			value="${enrolment.moment}" type="both" pattern="yyyy/MM/dd HH:mm" />
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="enrolment.moment" />: <fmt:formatDate
			value="${enrolment.moment}" type="both" pattern="dd/MM/yyyy HH:mm" />
	</jstl:otherwise>
</jstl:choose>
<br>

<jstl:if test="${not empty enrolment.moment}">
<jstl:choose>
	<jstl:when test="${enrolment.enrolled}">
		<jstl:choose>
			<jstl:when test="${lang eq 'en' }">
				<acme:display code="enrolment.position" value="${enrolment.position.nameEnglish}"/>
				<security:authorize access="hasRole('BROTHERHOOD')">
				<a href="enrolment/brotherhood/edit.do?memberId=${enrolment.member.userAccount.id}">
				<spring:message code="edit.position" />	</a>
				<br>
				</security:authorize>
			</jstl:when>
			<jstl:otherwise>
				<acme:display code="enrolment.position" value="${enrolment.position.nameSpanish}"/>
				<security:authorize access="hasRole('BROTHERHOOD')">
				<a href="enrolment/brotherhood/edit.do?memberId=${enrolment.member.userAccount.id}">
				<spring:message code="edit.position" />	</a>
				<br>
				</security:authorize>			
			</jstl:otherwise>
			</jstl:choose>
	</jstl:when>

	<jstl:otherwise> 
	<security:authorize access="hasRole('BROTHERHOOD')">
		<a href="enrolment/brotherhood/edit.do?memberId=${enrolment.member.userAccount.id}">
			<spring:message code="enrolment.member.position.assign" />
		</a>
		<br>
	</security:authorize>
	
	<security:authorize access="hasRole('MEMBER')">
		<spring:message code="enrolment.member.position" />
		<br>
	</security:authorize>
	</jstl:otherwise>
</jstl:choose>

 </jstl:if>
<jstl:choose>
	<jstl:when test="${enrolment.dropOut ne null}">
		<acme:display code="enrolment.dropOut" value="${enrolment.dropOut}"/>		
		<br>
	</jstl:when>

	<jstl:otherwise> 
	
	</jstl:otherwise>
</jstl:choose>


<br>
<security:authorize access="hasRole('MEMBER')">
	<acme:button url="brotherhood/list.do" name="back" code="member.back"/>
</security:authorize>

<security:authorize access="hasRole('BROTHERHOOD')">
	<acme:button url="member/list.do" name="back" code="member.back"/>
</security:authorize>

