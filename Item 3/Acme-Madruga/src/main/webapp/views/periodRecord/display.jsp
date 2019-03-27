<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

	
<acme:display code="record.title" value="${periodRecord.title}" />
<acme:display code="record.description" value="${periodRecord.description}" />
<acme:display code="record.startYear" value="${periodRecord.startYear}" />
<acme:display code="record.endYear" value="${periodRecord.endYear}" />
<acme:display code="record.photos" value="${periodRecord.photos}" />

<br>
<jstl:if test="${buttons}">
<input type="button" class="btn btn-danger" name="cancel"
           value="<spring:message code="general.cancel" />"
           onclick="relativeRedir('history/list.do');"/>

</jstl:if>

