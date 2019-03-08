<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>



<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table pagesize="10" class="displaytag" keepStatus="true"
	name="areas" requestURI="${requestURI}" id="row">
	
	<!-- Action links -->

	<security:authorize access="hasRole('ADMIN')">
		
		<spring:message code="area.edit.header" var="editHeader"></spring:message>
		<display:column title="${editHeader}" sortable="false">
			<spring:url value="area/administrator/edit.do" var="editURL">
				<spring:param name="areaId" value="${row.id}"/> <!-- es el par�metro que va a acompa�ar a la url -->
			</spring:url>
			<a href="${editURL}"><spring:message code="area.edit"/></a>
		</display:column>
		
	</security:authorize>		
		
		<spring:message code="area.display.header" var="displayHeader"></spring:message>
		<display:column title="${displayHeader}" sortable="false">
			<spring:url value="area/administrator/display.do" var="displayURL">
				<spring:param name="areaId" value="${row.id}"/> <!-- es el par�metro que va a acompa�ar a la url -->
			</spring:url>
			<a href="${displayURL}"><spring:message code="area.display"/></a>
		</display:column>
	
	
		<!-- Attributes of the positions -->
 
		<spring:message code="area.name" var="nameHeader" />
		<display:column property="name" title="${nameHeader}" sortable="true" />
		
		


	
</display:table>
	
<!-- Action links -->

<security:authorize access="hasRole('ADMIN')">
	<div>
		<a href="area/administrator/create.do"> <spring:message
				code="area.create" />
		</a>
	</div>
</security:authorize>  