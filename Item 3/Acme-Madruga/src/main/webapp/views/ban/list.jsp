<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>



<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<!--<jstl:if test="${not empty message}" >
	<spring:message code="${message}" var="error" />
	<jstl:out value="${error}" />
</jstl:if>-->


<table>
<tr>
<th> <spring:message code="ban.actor.name" var="nameHeader" /><jstl:out value="${nameHeader }" /></th>
<th> <spring:message code="ban.actor.spammer" var="spammerHeader" /><jstl:out value="${spammerHeader }" /></th>
<th> <spring:message code="ban.actor.score" var="scoreHeader" /><jstl:out value="${scoreHeader }" /></th>
<th> <spring:message code="ban.actor.banned" var="bannedHeader" /><jstl:out value="${bannedHeader }" /></th>
<th> <spring:message code="ban.header" var="banHeader" /><jstl:out value="${banHeader }" /></th>
</tr>



<jstl:forEach var="actor" items="${actors}" varStatus="theCount" >
    <tr>
    <td>${actor.name}</td>
    <td>${actor.spammer}</td>
    <td>${actor.score}</td>
    <td>${bannedList[theCount.index]}</td>
    <jstl:choose>
			<jstl:when test="${bannedList[theCount.index]==true}">
				<td><spring:url value="ban/administrator/unban.do" var="unbanURL">
						<spring:param name="actorId" value="${actor.id}"/>
					</spring:url>
					<a href="${unbanURL}"><spring:message code="actor.unban"/></a></td>
			</jstl:when>
			<jstl:when test="${bannedList[theCount.index]==false}">
				<td><spring:url value="ban/administrator/ban.do" var="banURL">
						<spring:param name="actorId" value="${actor.id}"/>
					</spring:url>
					<a href="${banURL}"><spring:message code="actor.ban"/></a></td>
			</jstl:when>
	</jstl:choose>
    </tr>
</jstl:forEach>

</table>
	


