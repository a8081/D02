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

<jstl:if test="${not empty rol}">
	<jstl:set var="rolURL" value="/${rol}" />
</jstl:if>

<display:table name="parades" id="row"
	requestURI="parade${rolURL}/list.do" pagesize="5"
	class="displaytag">

	<security:authorize access="hasRole('BROTHERHOOD')">
		<display:column>
			<jstl:if test="${row.mode eq 'DRAFT'}">
				<acme:link
					url="parade/brotherhood/edit.do?paradeId=${row.id}"
					code="parade.edit" />
			</jstl:if>
		</display:column>
	</security:authorize>

	<display:column property="title" titleKey="parade.title" />
	
	<display:column property="ticker" titleKey="parade.ticker" />

	<acme:dataTableColumn code="parade.moment" property="moment" />
	
	<display:column titleKey="parade.brotherhood">
		<a href="brotherhood/displayTabla.do?brotherhoodId=${row.brotherhood.id}">
			<jstl:out value="${row.brotherhood.title}" />
		</a>
	</display:column>
	
	<display:column>
		<acme:link url="parade${rolURL}/display.do?paradeId=${row.id}"
			code="parade.display" />
	</display:column>
	
	<security:authorize access="hasRole('MEMBER')">
		<jstl:if test="${not empty rol}">
			<jstl:set var="ctrl" value="0" />
			<jstl:forEach var="r" items="${memberparades}">
				<jstl:if test="${r eq row}">
					<jstl:set var="ctrl" value="1" />
				</jstl:if>
			</jstl:forEach>
			<display:column>
				<jstl:choose>
					<jstl:when test="${ctrl == 0}">
						<acme:link url="request/member/create.do?paradeId=${row.id}"
							code="parade.apply" />
					</jstl:when>
					<jstl:otherwise>
						<acme:link url="request/member/displayByParade.do?paradeId=${row.id}"
							code="parade.applied" />
					</jstl:otherwise>
				</jstl:choose>
			</display:column>
		</jstl:if>
	</security:authorize>
	
	<security:authorize access="hasRole('BROTHERHOOD')">
		<display:column>
			<jstl:if test="${row.mode eq 'DRAFT'}">
				<acme:link
					url="parade/brotherhood/finalMode.do?paradeId=${row.id}"
					code="parade.finalMode" />
			</jstl:if>
		</display:column>
	</security:authorize>

</display:table>

<security:authorize access="hasRole('BROTHERHOOD')">
	<acme:link url="parade/brotherhood/create.do"
		code="parade.create" />
</security:authorize>