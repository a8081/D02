<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<display:table name="segments" id="row"
	requestURI="segment/brotherhood/list.do" pagesize="5"
	class="displaytag">


	<acme:dataTableColumn code="segment.originTime" property="originTime" />
	<acme:dataTableColumn code="segment.destinationTime" property="destinationTime" />
	
	<display:column titleKey="segment.originCoordinates" property="originCoordinates"/>
	<display:column titleKey="segment.destinationCoordinates" property="destinationCoordinates"/>
	
	
</display:table>