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

<script type="text/javascript">
		function Confirmacion(e) {
		//Ingresamos un mensaje a mostrar
		var mensaje = confirm("<spring:message code="proclaim.confirm" />");
		//Detectamos si el usuario acepto el mensaje
		if (mensaje) {
			relativeRedir('proclaim/chapter/list.do');
		}
		//Detectamos si el usuario denegó el mensaje
		else {
			e.preventDefault();
		}
		}
	</script>

<form:form action="proclaim/edit.do" modelAttribute="proclaim" method="POST">
    
    <form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="moment"/>
	<form:hidden path="chapter"/>
	
	<acme:textarea path="text" code="proclaim.text" />
	<br />

 	
	<input type="submit" name="save" onclick="Confirmacion(event)" value="<spring:message code="proclaim.save" />" />
	
	<input type="button" class="btn btn-danger" name="cancel"
           value="<spring:message code="general.cancel" />"
           onclick="relativeRedir('proclaim/chapter/list.do');"/>
	<br>
		
</form:form>