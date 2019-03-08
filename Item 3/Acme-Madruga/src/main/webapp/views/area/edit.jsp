<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<head>
<style type="text/css">
.input > textarea{height: 100px; width: 700px}
</style>
</head>


<form:form action="area/administrator/edit.do"  modelAttribute="area" >

	<form:hidden path="id" />
	<form:hidden path="version" /> 


	
	<security:authorize access="hasRole('ADMIN')">
	
	<form:label path="name">
		<spring:message code="area.name" />
	</form:label>
	<form:input path="name" />
	<form:errors cssClass="error" path="name" />
	
	<br>
	<br>
	
	<form:label path="pictures">
		<spring:message code="area.pictures" />
	</form:label>
	<br>
	<spring:message code="area.text.area" var="warning" />
	<jstl:out value="${warning}" />
	<div class="input">
	<form:textarea path="pictures" />
	<form:errors cssClass="error" path="pictures" />
	</div>
	
	
	
	<!-- BOTONES --> 
	
	 <input type="submit" name="save"
		value="<spring:message code="area.save"/>" />&nbsp;
		
	<jstl:if test="${area.id!=0}">
		<input type="submit" name="delete"
			value="<spring:message code="area.delete"/>"
			onclick="javascript: return confirm('<spring:message code="area.confirm.delete"/>')" />&nbsp;
	</jstl:if>
		
		<!--<jstl:if test="${area.id!=0}">
		<input type="submit" name="delete"
			value="<spring:message code="area.delete"/>"  />&nbsp;
	</jstl:if>-->
	
	  <input type="button" name="cancel"
		value="<spring:message code="area.cancel"/>"
		onclick="javascript: window.location.replace('area/administrator/list.do')" />
	<br />
	
	</security:authorize>
		
</form:form>

