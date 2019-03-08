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

<style type="text/css">
.PENDING {
	background-color: #d9d9d9;
}

.APPROVED {
	background-color: green;
}

.REJECTED {
	background-color: orange;
}
</style>

<jstl:if test="${not empty rol}">
	<jstl:set var="rolURL" value="/${rol}" />
</jstl:if>

<jstl:choose>
	<jstl:when test="${empty requests}">
		<spring:message code="request.no.list" />
	</jstl:when>
	<jstl:otherwise>
		<display:table name="requests" id="row" requestURI="request${rolURL}/list.do" pagesize="5" class="displaytag">

			<jstl:set value="${row.status} " var="colorStyle" />
			
			<acme:dataTableColumn code="request.moment" property="moment" />
			<display:column titleKey="request.status" class="${colorStyle}">
				<acme:statusChoose status="${row.status}"/>
			</display:column>
			<display:column property="procession.title" titleKey="request.procession.title" />
			<display:column>
				<acme:link url="request${rolURL}/display.do?requestId=${row.id}" code="request.display"/>
			</display:column>

			<security:authorize access="hasRole('BROTHERHOOD')">
				<display:column>
					<jstl:if test="${row.status eq 'PENDING'}">
						<acme:button url="request/brotherhood/approve.do?requestId=${row.id}&processionId=${row.procession.id}" name="approve" code="request.approve"/>
					</jstl:if>
				</display:column>
				<display:column>
					<jstl:if test="${row.status eq 'PENDING'}">
						<acme:button url="request/brotherhood/reject.do?requestId=${row.id}" name="reject" code="request.reject"/>
					</jstl:if>
				</display:column>
			</security:authorize>
			
		</display:table>
	</jstl:otherwise>
</jstl:choose>

<security:authorize access="hasRole('MEMBER')">
	<jstl:choose>
		<jstl:when test="${theresProcessionsAvailable}">
			<acme:button url="procession/member/list.do" name="more" code="request.more"/>
		</jstl:when>
		<jstl:otherwise>
			<spring:message code="request.create.no" />
		</jstl:otherwise>
	</jstl:choose>
</security:authorize>