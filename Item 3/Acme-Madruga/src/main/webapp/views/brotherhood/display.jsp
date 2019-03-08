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

<script>
function generatePDF(){
	alert('<spring:message code="display.document.alert"/>')
	var doc = new jsPDF()
	doc.text('<spring:message code="display.document.title"/>', 20, 10)
	doc.text('', 10, 20)
	doc.text('<spring:message code="actor.name"/> : <jstl:out value="${brotherhood.name}"/>', 10, 30)
	doc.text('<spring:message code="actor.middleName"/> : <jstl:out value="${brotherhood.middleName}"/>', 10, 40)
	doc.text('<spring:message code="actor.surname"/> : <jstl:out value="${brotherhood.surname}"/>', 10, 50)
	doc.text('<spring:message code="actor.photo"/> : <jstl:out value="${brotherhood.photo}"/>', 10, 60)
	doc.text('<spring:message code="actor.phone"/> : <jstl:out value="${brotherhood.phone}"/>', 10, 70)
	doc.text('<spring:message code="actor.email"/> : <jstl:out value="${brotherhood.email}"/>', 10, 80)
	doc.text('<spring:message code="actor.address"/> : <jstl:out value="${brotherhood.address}"/>', 10, 90)
	doc.save('<spring:message code="display.document.fileName"/>.pdf')
}
function deletePersonalData(){
	var r = confirm('<spring:message code="display.deletePersonalData"/>');
	if (r == true) {
		location.href = "brotherhood/deletePersonalData.do";
	}
}
</script>



<acme:display code="actor.name" value="${brotherhood.name}"/>
<spring:message code="actor.photo"/>:<br>
<img src="${brotherhood.photo}" alt="<spring:message code="brotherhood.alt.image"/>" width="20%" height="20%"/>
<br>
<acme:display code="actor.middleName" value="${brotherhood.middleName}"/>
<acme:display code="actor.surname" value="${brotherhood.surname}"/>
<acme:display code="actor.email" value="${brotherhood.email}"/>
<acme:display code="actor.phone" value="${brotherhood.phone}"/>
<acme:display code="actor.address" value="${brotherhood.address}"/>
<acme:display code="actor.score" value="${brotherhood.score}"/>

<jstl:choose>
	<jstl:when test="${brotherhood.spammer}">
		<spring:message code="actor.spammer"/>
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="actor.spammer.no"/>
	</jstl:otherwise>
</jstl:choose>
<br>

<jstl:choose>
	<jstl:when test="${empty brotherhood.area and displayButtons}">
	<acme:button url="brotherhood/assignArea.do" name="assign" code="brotherhood.selectArea"/>
	<br>
	</jstl:when>
	<jstl:otherwise>
	<acme:display code="brotherhood.area" value="${brotherhood.area.name}"/>
	</jstl:otherwise>
</jstl:choose>
<br>
	<acme:button url="procession/listByBrotherhood.do?brotherhoodId=${brotherhood.id}" name="processionList" code="brotherhood.processionList"/>
<br>
<jstl:if test="${displayButtons}">
	<button onClick="generatePDF()"><spring:message code="display.getData"/></button>
	<button onClick="deletePersonalData()"><spring:message code="display.button.deletePersonalData"/></button>
</jstl:if>
<br>
<security:authorize access="hasRole('MEMBER')">
	<acme:button url="brotherhood/list.do" name="back" code="procession.back.list"/>
</security:authorize>


