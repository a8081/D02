<%@page language="java" contentType="text/html; charset=ISO-8859-1"
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

<acme:display code="parade.title" value="${parade.title}" />
<acme:display code="parade.description" value="${parade.description}" />
	
<jstl:choose>
	<jstl:when test="${lang eq 'en' }">
		<spring:message code="parade.moment" />: <fmt:formatDate
			value="${parade.moment}" type="both" pattern="yyyy/MM/dd HH:mm" />
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="parade.moment" />: <fmt:formatDate
			value="${parade.moment}" type="both" pattern="dd/MM/yyyy HH:mm" />
	</jstl:otherwise>
</jstl:choose>
</br>

<acme:display code="parade.ticker" value="${parade.ticker}" />
<spring:message code="parade.mode" />:
<acme:modeChoose mode="${parade.mode}"/>
<br />
<acme:display code="parade.maxRows" value="${parade.maxRows}" />
<acme:display code="parade.maxColumns"
	value="${parade.maxColumns}" />

<jstl:forEach items="${parade.floats}" var="f" varStatus="loop">
	<jstl:out value="${loop.index+1} " /><spring:message code="parade.float" />: 
		<a href="float/display.do?floatId=${f.id}">
	<jstl:out value="${f.title}"/></a><br />
</jstl:forEach>

<spring:message code="parade.brotherhood" />:
<a href="brotherhood/displayTabla.do?brotherhoodId=${parade.brotherhood.id}">
<jstl:out value="${parade.brotherhood.title}"/>
</a><br />
<br />

<security:authorize access="hasRole('BROTHERHOOD')">
	<spring:message code="parade.requests" />:
	<display:table name="requests" id="fila" pagesize="5" class="displaytag" requestURI="parade/brotherhood/display.do?paradeId=${parade.id}">
		<jstl:set var="colorStyle" value="${fila.status}" />
		<acme:dataTableColumn property="moment" code="request.moment" />
		<display:column titleKey="request.status" class="${colorStyle}">
				<acme:statusChoose status="${fila.status}"/>
		</display:column>		
		<display:column>
			<jstl:if test="${fila.status eq 'PENDING'}">
				<acme:button url="request/brotherhood/approve.do?requestId=${fila.id}&paradeId=${fila.parade.id}" name="approve" code="request.approve"/>
			</jstl:if>
		</display:column>
		<display:column>
			<jstl:if test="${fila.status eq 'PENDING'}">
				<acme:button url="request/brotherhood/reject.do?requestId=${fila.id}" name="reject" code="request.reject"/>
			</jstl:if>
		</display:column>
	</display:table>
	<br />
</security:authorize>

<acme:button url="parade${rolURL}/list.do" name="back"
	code="parade.list.button" />
<br />