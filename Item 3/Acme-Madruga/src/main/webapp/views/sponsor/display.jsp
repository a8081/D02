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
<script src="https://unpkg.com/jspdf@latest/dist/jspdf.min.js"></script>
<script src="https://unpkg.com/jspdf@latest/dist/jspdf.min.js"></script>

<script>
function generatePDF(){
	alert('<spring:message code="display.sponsor.document.alert"/>')
	var doc = new jsPDF()
	doc.text('<spring:message code="display.document.title"/>', 20, 10)
	doc.text('', 10, 20)
	doc.text('<spring:message code="actor.name"/> : <jstl:out value="${sponsor.name}"/>', 10, 30)
	doc.text('<spring:message code="actor.middleName"/> : <jstl:out value="${sponsor.middleName}"/>', 10, 40)
	doc.text('<spring:message code="actor.surname"/> : <jstl:out value="${sponsor.surname}"/>', 10, 50)
	doc.text('<spring:message code="actor.photo"/> : <jstl:out value="${sponsor.photo}"/>', 10, 60)
	doc.text('<spring:message code="actor.phone"/> : <jstl:out value="${sponsor.phone}"/>', 10, 70)
	doc.text('<spring:message code="actor.email"/> : <jstl:out value="${sponsor.email}"/>', 10, 80)
	doc.text('<spring:message code="actor.address"/> : <jstl:out value="${sponsor.address}"/>', 10, 90)
	doc.save('<spring:message code="display.document.fileName"/>.pdf')
}
function deletePersonalData(){
	var r = confirm('<spring:message code="display.deletePersonalData"/>');
	if (r == true) {
		location.href = "sponsor/deletePersonalData.do";
	}
}
</script>


<acme:display code="actor.name" value="${sponsor.name}"/>
<spring:message code="actor.photo"/>:<br>
<img src="${sponsor.photo}" alt="<spring:message code="sponsor.alt.image"/>" width="20%" height="20%"/>
<br>
<acme:display code="actor.middleName" value="${sponsor.middleName}"/>
<acme:display code="actor.surname" value="${sponsor.surname}"/>
<acme:display code="actor.email" value="${sponsor.email}"/>
<acme:display code="actor.phone" value="${sponsor.phone}"/>
<acme:display code="actor.email" value="${sponsor.email}"/>
<acme:display code="actor.address" value="${sponsor.address}"/>
<acme:display code="actor.score" value="${sponsor.score}"/>

<jstl:choose>
	<jstl:when test="${sponsor.spammer}">
		<spring:message code="actor.spammer"/>
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="actor.spammer.no"/>
	</jstl:otherwise>
</jstl:choose>


<jstl:if test="${displayButtons}">
<br>
	<button onClick="generatePDF()"><spring:message code="display.getData"/></button>
	<button onClick="deletePersonalData()"><spring:message code="display.button.deletePersonalData"/></button>
	
<br>
</jstl:if>

<security:authorize access="hasRole('BROTHERHOOD')">
	<acme:button url="/sponsor/list.do" name="back" code="sponsor.back"/>
</security:authorize>