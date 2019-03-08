<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

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


<!-- Only brotherhood can access to this view -->

<form:form action="procession/brotherhood/edit.do"
	modelAttribute="procession">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="mode" />
	<form:hidden path="ticker" />
	<form:hidden path="brotherhood"/>

	<acme:textbox code="procession.title" path="title" />
	<acme:textbox code="procession.description" path="description" />
	<acme:numberbox code="procession.maxRows" path="maxRows" min="1" />
	<acme:numberbox code="procession.maxColumns" path="maxColumns" min="1" />
	<acme:textbox code="procession.moment" path="moment" placeholder="yyyy-MM-dd HH:mm"/> <spring:message code="date.pattern" />
	
	
	<acme:select items="${floatsAvailable}" itemLabel="title" code="procession.floats" path="floats" />
	<jstl:if test="${procession.id != 0}">
		<spring:message code="procession.requests" />:
			<display:table name="requests" id="row"
			requestURI="procession/brotherhood/edit.do?processionId=${procession.id}"
			pagesize="5" class="displaytag">
			<jstl:set value="${row.status} " var="colorStyle" />
			<jstl:choose>
				<jstl:when test="${row.status eq 'PENDING' }">
					<display:column>
						<a href="request/brotherhood/edit.do?requestId=${row.id}"> <spring:message
								code="procession.request.edit" />
						</a>
					</display:column>
				</jstl:when>
				<jstl:otherwise>
				</jstl:otherwise>
			</jstl:choose>
			<acme:dataTableColumn property="moment"
				code="procession.request.moment" />
			<display:column property="status"
				titleKey="procession.request.status" class="${colorStyle}" />
		</display:table>
		<br />
	</jstl:if>

	<input type="submit" name="save"
		value="<spring:message code="procession.save" />" />
	<jstl:if test="${procession.id != 0}">
		<input type="submit" name="delete"
			value="<spring:message code="procession.delete" />" />
	</jstl:if>

	<acme:button url="procession/brotherhood/list.do" name="cancel"
		code="procession.cancel" />

</form:form>