<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
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

<style>
img.resize {
  max-width:15%;
  max-height:15%;
}
</style>


<spring:message code="sponsorship.banner" />:
<br/>
<a href="<jstl:out value="${sponsorship.targetPage}"/>">
<img class="resize" src="${sponsorship.banner}" alt="<spring:message code='sponsorship.my.banner'/>"/></a>
<br /><br />
<acme:url labelCode="sponsorship.targetPage" url="${sponsorship.targetPage}" code="sponsorship.my.targetPage" />

<h3><spring:message code="sponsorship.creditCard"/></h3>

<acme:display code="sponsorship.creditCard.holderName" value="${sponsorship.creditCard.holderName}" />
<acme:display code="sponsorship.creditCard.brandName" value="${sponsorship.creditCard.make}" />
<acme:display code="sponsorship.creditCard.number" value="${sponsorship.creditCard.number}" />
<acme:display code="sponsorship.creditCard.expirationMonth" value="${sponsorship.creditCard.expirationMonth}" />
<acme:display code="sponsorship.creditCard.expirationYear" value="${sponsorship.creditCard.expirationYear}" />
<acme:display code="sponsorship.creditCard.cvv" value="${sponsorship.creditCard.cvv}" />
<br>

<jstl:if test="${sponsorship.activated eq false}">
	<strong><spring:message code="sponsorship.deactivated"/></strong><br><br>
</jstl:if>

<acme:button url="parade/display.do?paradeId=${sponsorship.parade.id}" name="parade" code="sponsorship.parade"/>





