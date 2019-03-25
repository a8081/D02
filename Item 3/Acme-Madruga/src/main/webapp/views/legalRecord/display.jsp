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

	
<acme:display code="record.title" value="${legalRecord.title}" />
<acme:display code="record.description" value="${legalRecord.description}" />
<acme:display code="legalRecord.legalName" value="${legalRecord.legalName}" />
<acme:display code="legalRecord.vat" value="${legalRecord.vat}" />
<acme:display code="legalRecord.laws" value="${legalRecord.laws}" />

<br>

<input type="button" class="btn btn-danger" name="cancel"
           value="<spring:message code="general.cancel" />"
           onclick="relativeRedir('history/list.do');"/>




