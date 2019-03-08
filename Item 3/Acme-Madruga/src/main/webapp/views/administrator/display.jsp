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
	alert('<spring:message code="display.member.document.alert"/>')
	var doc = new jsPDF()
	doc.text('<spring:message code="display.document.title"/>', 20, 10)
	doc.text('', 10, 20)
	doc.text('<spring:message code="actor.name"/> : <jstl:out value="${administrator.name}"/>', 10, 30)
	doc.text('<spring:message code="actor.middleName"/> : <jstl:out value="${administrator.middleName}"/>', 10, 40)
	doc.text('<spring:message code="actor.surname"/> : <jstl:out value="${administrator.surname}"/>', 10, 50)
	doc.text('<spring:message code="actor.photo"/> : <jstl:out value="${administrator.photo}"/>', 10, 60)
	doc.text('<spring:message code="actor.phone"/> : <jstl:out value="${administrator.phone}"/>', 10, 70)
	doc.text('<spring:message code="actor.email"/> : <jstl:out value="${administrator.email}"/>', 10, 80)
	doc.text('<spring:message code="actor.address"/> : <jstl:out value="${administrator.address}"/>', 10, 90)
	doc.save('<spring:message code="display.document.fileName"/>.pdf')
}
function deletePersonalData(){
	var r = confirm('<spring:message code="display.deletePersonalData"/>');
	if (r == true) {
		location.href = "administrator/deletePersonalData.do";
	}
}
</script>


<acme:display code="administrator.edit.name" value="${administrator.name}"/>
<spring:message code="administrator.edit.photo"/>:<br>
<img src="${member.photo}" alt="<spring:message code="administrator.alt.image"/>" width="20%" height="20%"/>
<br>
<acme:display code="administrator.edit.middleName" value="${administrator.middleName}"/>
<acme:display code="administrator.edit.surname" value="${administrator.surname}"/>
<acme:display code="administrator.edit.email" value="${administrator.email}"/>
<acme:display code="administrator.edit.phone" value="${administrator.phone}"/>
<acme:display code="administrator.edit.email" value="${administrator.email}"/>
<acme:display code="administrator.edit.address" value="${administrator.address}"/>
<acme:display code="administrator.edit.score" value="${administrator.score}"/>

<jstl:choose>
	<jstl:when test="${administrator.spammer}">
		<spring:message code="actor.spammer"/>
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="actor.spammer.no"/>
	</jstl:otherwise>
</jstl:choose>
<br>
	<button onClick="generatePDF()"><spring:message code="display.getData"/></button>
	<button onClick="deletePersonalData()"><spring:message code="display.button.deletePersonalData"/></button>
