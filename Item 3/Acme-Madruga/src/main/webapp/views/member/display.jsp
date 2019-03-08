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
	doc.text('<spring:message code="actor.name"/> : <jstl:out value="${member.name}"/>', 10, 30)
	doc.text('<spring:message code="actor.middleName"/> : <jstl:out value="${member.middleName}"/>', 10, 40)
	doc.text('<spring:message code="actor.surname"/> : <jstl:out value="${member.surname}"/>', 10, 50)
	doc.text('<spring:message code="actor.photo"/> : <jstl:out value="${member.photo}"/>', 10, 60)
	doc.text('<spring:message code="actor.phone"/> : <jstl:out value="${member.phone}"/>', 10, 70)
	doc.text('<spring:message code="actor.email"/> : <jstl:out value="${member.email}"/>', 10, 80)
	doc.text('<spring:message code="actor.address"/> : <jstl:out value="${member.address}"/>', 10, 90)
	doc.save('<spring:message code="display.document.fileName"/>.pdf')
}
function deletePersonalData(){
	var r = confirm('<spring:message code="display.deletePersonalData"/>');
	if (r == true) {
		location.href = "member/deletePersonalData.do";
	}
}
</script>


<acme:display code="actor.name" value="${member.name}"/>
<spring:message code="actor.photo"/>:<br>
<img src="${member.photo}" alt="<spring:message code="member.alt.image"/>" width="20%" height="20%"/>
<br>
<acme:display code="actor.middleName" value="${member.middleName}"/>
<acme:display code="actor.surname" value="${member.surname}"/>
<acme:display code="actor.email" value="${member.email}"/>
<acme:display code="actor.phone" value="${member.phone}"/>
<acme:display code="actor.email" value="${member.email}"/>
<acme:display code="actor.address" value="${member.address}"/>
<acme:display code="actor.score" value="${member.score}"/>

<jstl:choose>
	<jstl:when test="${member.spammer}">
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
	<acme:button url="/member/list.do" name="back" code="member.back"/>
</security:authorize>