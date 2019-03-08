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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<form:form action="finder/member/edit.do" modelAttribute="finder" method="POST">

	<%-- <form:hidden path="id" />
	<form:hidden path="version" />

	<br/>
    <acme:textbox path="keyword" code="finder.keyword"/>
    <br/>
    <acme:textbox path="areaName" code="finder.areaName"/>
	<br/>
    <acme:textbox path="maxDate" code="finder.maxDate"/>
    <br/>
    <acme:textbox path="minDate" code="finder.minDate"/>
    <br/> --%>
    
    <form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="processions"/>
	<form:hidden path="creationDate"/>
	
	<form:label path="keyword">
		<spring:message code="finder.keyword" />: </form:label>
	<form:input path="keyword" />
	<form:errors cssClass="error" path="keyword" />
	<br />
	<form:label path="areaName">
		<spring:message code="finder.areaName" />
	</form:label>	
	<form:select path="areaName" >
		<form:option value=""/>
		<jstl:forEach items="${areas}" var="mar">
			<form:option value="${mar.name}"/>		
		</jstl:forEach>
	</form:select>
	<form:errors path="areaName" cssClass="error" />
	<br />
	
	<form:label path="minDate">
		<spring:message code="finder.minDate" />: </form:label>
	<form:input path="minDate" placeholder = "yyyy-MM-dd HH:mm" />
	<form:errors cssClass="error" path="minDate" />
	<br />
	<form:label path="maxDate">
		<spring:message code="finder.maxDate" />: </form:label>
	<form:input path="maxDate" placeholder = "yyyy-MM-dd HH:mm" />
	<form:errors cssClass="error" path="maxDate" />
	<br />


	<br>
	<input type="submit" name="save" value="<spring:message code="finder.search" />" />
	<input type="submit" name="clear" value="<spring:message code="finder.clear" />" />
	<br>
	<br>
	<spring:message code="finder.results" />
	<br>
	
<display:table name="${finder.processions}" id="row" requestURI="/finder/member/edit.do" pagesize="15" class="displaytag">
	<display:column property="title" titleKey="procession.title" />
	
	<display:column property="ticker" titleKey="procession.ticker" />

	<acme:dataTableColumn code="procession.moment" property="moment" />
	
	<display:column titleKey="procession.brotherhood">
		<jstl:out value="${row.brotherhood.title}" />
		
	</display:column>
	
	<display:column>
		<acme:link url="procession${rolURL}/display.do?processionId=${row.id}"
			code="procession.display" />
	</display:column>
	
	
</display:table>
		
</form:form>