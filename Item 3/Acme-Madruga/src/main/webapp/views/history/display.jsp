<%@page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
    uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<jstl:if test="${not empty history}">
    <%-- History --%>
    <div id="history">
        <ul style="list-style-type: disc">
            <li><b><spring:message code="history"/></b>
        </ul>
        <br/>
    </div>
    <%-- Inception record --%>
    <div id="inceptionRecord">
        <ul>
            <!--<u><b><spring:message code="history.inceptionRecord"></spring:message></b></u>
            <br/><br/>
            <img class="resize" src="<jstl:out value="${inceptionRecord.photos}"/>"
                alt="<spring:message
                        code="curriculum.alt.personalPhoto"/>" />
            <br/><br/>-->
            <li><b><spring:message
                        code="record.title"></spring:message>:</b> <jstl:out
                    value="${inceptionRecord.title}" /></li>
            <li><b><spring:message
                        code="record.description"></spring:message>:</b> <jstl:out
                    value="${inceptionRecord.description}" /></li>
        </ul>
        <br/>
        
    </div>
    <%-- Period records --%>
    <div id="peroidRecords">
        <ul>
            <jstl:if test="${not empty periodRecords}">
                <u><b><spring:message code="periodRecords"></spring:message></b></u>
                <br />
                <jstl:forEach var="periodRecord"
                    items="${periodRecords}" varStatus="loop">
                    <br/>
                    
                    <li><b><spring:message
		                        code="record.title"></spring:message>:</b> <jstl:out
		                    value="${periodRecords.title}" /></li>
		            <li><b><spring:message
		                        code="record.description"></spring:message>:</b> <jstl:out
		                    value="${periodRecords.description}" /></li>
                    <li><b><spring:message
                                code="periodRecord.starYear"></spring:message>:</b> <fmt:formatDate
                            pattern="yyyy" value="${periodRecords.starYear}" /></li>
                    <li><b><spring:message
                                code="periodRecord.endYear"></spring:message>:</b> <fmt:formatDate
                            pattern="yyyy" value="${periodRecords.endYear}" /></li>
                    <br />
                    <br />
                </jstl:forEach>
            </jstl:if>
        </ul>
        <br /> <br />
    </div>
    <%-- Legal records --%>
    <div id="legalRecords">
        <ul>
            <jstl:if test="${not empty legalRecords}">
                <u><b><spring:message code="legalRecords"></spring:message></b></u>
                <br />
                <jstl:forEach var="legalRecords"
                    items="${legalRecords}" varStatus="loop">
                    <br/>
                    <li><b><spring:message
		                        code="record.title"></spring:message>:</b> <jstl:out
		                    value="${legalRecords.title}" /></li>
		            <li><b><spring:message
		                        code="record.description"></spring:message>:</b> <jstl:out
		                    value="${legalRecords.description}" /></li>
                    <li><b><spring:message
	                        code="legalRecord.legalName"></spring:message>:</b> <jstl:out
	                    value="${legalRecords.legalName}" /></li>
                    <li><b><spring:message
	                        code="legalRecord.laws"></spring:message>:</b> <jstl:out
	                    value="${legalRecords.laws}" /></li>
                    <ul>
                        <jstl:forEach var="laws" items="${legalRecords.laws}">
                            <li><jstl:out value="${legalRecords.laws}" /></li>
                        </jstl:forEach>
                    </ul>
                    <br />
                </jstl:forEach>
            </jstl:if>
        </ul>
        <br /> <br />
    </div>
    <%-- Link records --%>
    <div id="linkRecords">
        <ul>
            <jstl:if test="${not empty linkRecords}">
                <u><b><spring:message code="linkRecords"/></b></u>
                <br />
                <jstl:forEach var="linkRecords"
                    items="${linkRecords}" varStatus="loop">
                    <br/>
                    
                    <li><b><spring:message
		                        code="record.title"></spring:message>:</b> <jstl:out
		                    value="${linkRecords.title}" /></li>
		            <li><b><spring:message
		                        code="record.description"></spring:message>:</b> <jstl:out
		                    value="${linkRecords.description}" /></li>
                    <li><b><spring:message
                                code="linkRecords.linkedBrotherhood"></spring:message>:</b> <a
                        href="<jstl:out value="${linkRecords.linkedBrotherhood}"/>"><jstl:out
                                value="${linkRecords.linkedBrotherhood}" /></a></li>
                    <br />
                    <br>
                </jstl:forEach>
            </jstl:if>
        </ul>
        <br /> <br />
    </div>
    <%-- Miscellaneous records --%>
    <div id="miscellaneousRecords">
        <ul>
            <jstl:if test="${not empty miscellaneousRecords}">
                <u><b><spring:message code="miscellaneousRecords"></spring:message></b></u>
                <br />
                <jstl:forEach var="miscellaneousRecord"
                    items="${miscellaneousRecords}" varStatus="loop">
                    <br/>
                    <li><b><spring:message
		                        code="record.title"></spring:message>:</b> <jstl:out
		                    value="${miscellaneousRecords.title}" /></li>
		            <li><b><spring:message
		                        code="record.description"></spring:message>:</b> <jstl:out
		                    value="${miscellaneousRecords.description}" /></li>
                    <br />
                </jstl:forEach>
            </jstl:if>
        </ul>
        <br /> <br /> <br />
    </div>
</jstl:if>
    <%-- ---------------------------------------------------------------------------------------------------------------------------------- --%>
    <!--<security:authorize access="hasRole('HANDYWORKER')">
        <jstl:if test="${originURL eq 1}">
            <form:form action="curriculum/handyWorker/edit.do"
                modelAttribute="curriculum">
                <form:hidden path="id" />
                <form:hidden path="version" />
                <form:hidden path="ticker" />
                <input type="submit" name="delete"
                    value="<spring:message code="curriculum.delete" />" />
            </form:form>
            <br />
        </jstl:if>
    </security:authorize>
<jstl:if test="${empty curriculum and (originURL eq 1)}">
    <input type="button" name="create"
        value="<spring:message code="curriculum.create" />"
        onclick="javascript: relativeRedir('curriculum/handyWorker/create.do');" />
</jstl:if>
<input type="button"
    name="back"
    value="<spring:message code="curriculum.back"/>"
    onclick="javascript:window.history.back();" />
<br />-->