<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<form:form action="${RequestURI }"  modelAttribute="position" >

	<form:hidden path="id" />
	<form:hidden path="version" /> 


	
	<security:authorize access="hasRole('ADMIN')">
	
	<form:label path="nameEnglish">
		<spring:message code="position.nameEnglish" />
	</form:label>
	<form:input path="nameEnglish" />
	<form:errors cssClass="error" path="nameEnglish" />
	
	<br>
	<br>
	
	<form:label path="nameSpanish">
		<spring:message code="position.nameSpanish" />
	</form:label>
	<form:input path="nameSpanish" />
	<form:errors cssClass="error" path="nameSpanish" />
	
	
	

	<br>
	<br>

	
	<!-- BOTONES --> 
	
	 <input type="submit" name="save"
		value="<spring:message code="position.save"/>" />&nbsp;
		
	<!--<jstl:if test="${position.id!=0}">
		<input type="submit" name="delete"
			value="<spring:message code="position.delete"/>"
			onclick="javascript: return confirm('<spring:message code="position.confirm.delete"/>')" />&nbsp;
	</jstl:if>-->
		
		<jstl:if test="${position.id!=0}">
		<input type="submit" name="delete"
			value="<spring:message code="position.delete"/>"  />&nbsp;
	</jstl:if>
	
	  <input type="button" name="cancel"
		value="<spring:message code="position.cancel"/>"
		onclick="javascript: window.location.replace('position/administrator/list.do')" />
	<br />
	
	</security:authorize>
</form:form>

