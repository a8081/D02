<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>



<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="positions" requestURI="${requestURI}" id="row">
	
	<!-- Action links -->

	<security:authorize access="hasRole('ADMIN')">
		
		<spring:message code="position.edit.header" var="editHeader"></spring:message>
		<display:column title="${editHeader}" sortable="false">
			<spring:url value="position/administrator/edit.do" var="editURL">
				<spring:param name="positionId" value="${row.id}"/> <!-- es el parámetro que va a acompañar a la url -->
			</spring:url>
			<a href="${editURL}"><spring:message code="position.edit"/></a>
		</display:column>	
		</security:authorize>		
	
	
		<!-- Attributes of the positions -->
 
		<spring:message code="position.nameEnglish" var="nameEnglishHeader" />
		<display:column property="nameEnglish" title="${nameEnglishHeader}" sortable="false" />
		
		<spring:message code="position.nameSpanish" var="nameSpanishHeader" />
		<display:column property="nameSpanish" title="${nameSpanishHeader}" sortable="false" />

	
</display:table>
	
<!-- Action links -->

<security:authorize access="hasRole('ADMIN')">
	<div>
		<a href="position/administrator/create.do"> <spring:message
				code="position.create" />
		</a>
	</div>
</security:authorize>    