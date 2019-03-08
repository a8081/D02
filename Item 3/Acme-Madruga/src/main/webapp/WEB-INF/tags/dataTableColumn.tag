<%--
 * dateTableColumn.tag
 *
 * Copyright (C) 2019 a8081 
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@ tag language="java" body-content="empty"%>

<%-- Taglibs --%>

<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<%-- Attributes --%>

<%@ attribute name="property" required="true"%>
<%@ attribute name="code" required="true"%>

<%@ attribute name="sortable" required="false"%>

<jstl:if test="${sortable == null}">
	<jstl:set var="sortable" value="true" />
</jstl:if>


<%-- Definition --%>

<jstl:choose>
	<jstl:when test="${lang eq 'en' }">
		<display:column property="${property}" titleKey="${code}"
			sortable="${sortable}" format="{0,date,yyyy/MM/dd HH:mm}" />
	</jstl:when>
	<jstl:otherwise>

		<display:column property="${property}" titleKey="${code}"
			sortable="${sortable}" format="{0,date,dd/MM/yyyy HH:mm}" />
	</jstl:otherwise>
</jstl:choose>