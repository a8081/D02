<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<head>
<style type="text/css">
.photo img{max-width: 700px}
</style>
</head>

<input type="button" name="back"
		value="<spring:message code="area.back"/>"
		onclick="javascript: window.location.replace('area/administrator/list.do')" />
		
<br>
<br>

<h2>
<jstl:out value="${area.name}"/>
</h2>

<br>
<br>

<jstl:forEach items="${area.pictures}" var="picture">
	<div class="photo">
	<a href="#"><img src="${picture}" alt="Not found" /></a>
	</div>
	<br>
	<br>
</jstl:forEach>

