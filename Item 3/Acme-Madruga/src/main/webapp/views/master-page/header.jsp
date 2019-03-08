<%--
 * header.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div>
	<a href="#"><img src="${bannerURL}" alt="Acme Madruga Co., Inc." /></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		
		<security:authorize access="isAuthenticated()">
			<li><a href="folder/list.do"><spring:message code="master.page.folder.list" /></a></li>
		</security:authorize>
		
		<!-- ========================================================================================================= -->
		<!-- ========================================  ADMINISTRATOR  ================================================ -->
		<!-- ========================================================================================================= -->
		
		<security:authorize access="hasRole('ADMIN')">
			

			<li><a href="brotherhood/listAll.do"><spring:message code="master.page.brotherhood.allBrotherhoods" /></a></li>
			
			<li><a class="fNiv"><spring:message	code="master.page.configurationParameters" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="configurationParameters/administrator/edit.do"><spring:message code="master.page.configurationParameters.edit" /></a></li>
				</ul>
			</li>
			
			<li><a class="fNiv"><a href="position/administrator/list.do"><spring:message	code="master.page.position" /></a></a>
				
			</li>
			
			<li><a class="fNiv"><a href="area/administrator/list.do"><spring:message	code="master.page.area" /></a></a>
				
			</li>
			
			<li><a class="fNiv"><a href="ban/administrator/list.do"><spring:message	code="master.page.ban" /></a></a>
				
			</li>
			
			<li><a class="fNiv"><a href="dashboard/administrator/statistics.do"><spring:message	code="master.page.dashboard" /></a></a>
			</li>
			
		</security:authorize>
		
		<!-- ========================================================================================================= -->
		<!-- ========================================= BROTHERHOOD =================================================== -->
		<!-- ========================================================================================================= -->	
	
		<security:authorize access="hasRole('BROTHERHOOD')">
		
			
			
		<%-- FLOATS --%>
			
			<li><a class="fNiv"><spring:message	code="master.page.float" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="float/list.do"><spring:message code="master.page.float.list" /></a></li>
					<li><a href="float/create.do"><spring:message code="master.page.float.create" /></a></li>
				</ul>
			</li>

		<%-- PROCESSIONS --%>
			<li><a class="fNiv"><spring:message	code="master.page.procession" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="procession/list.do"><spring:message code="master.page.all.processions" /></a></li>
					<li><a href="procession/brotherhood/create.do"><spring:message code="master.page.procession.create" /></a></li>
					<li><a href="procession/brotherhood/list.do"><spring:message code="master.page.procession.list" /></a></li>
				</ul>
			</li>
			
		<%-- MEMBERS --%>
			
			<li><a href="member/list.do"><spring:message code="master.page.member.list" /></a></li>
		
		<%-- REQUESTS --%>
			
			<li><a class="fNiv"><spring:message	code="master.page.request.brotherhood.list" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="request/brotherhood/list.do"><spring:message code="master.page.all.request" /></a></li>
					<li><a href="request/brotherhood/listApproved.do"><spring:message code="master.page.approved.request" /></a></li>
					<li><a href="request/brotherhood/listRejected.do"><spring:message code="master.page.rejected.request" /></a></li>
					<li><a href="request/brotherhood/listPending.do"><spring:message code="master.page.pending.request" /></a></li>
				</ul>
			</li>
			
		</security:authorize>
		
		<!-- ========================================================================================================= -->
		<!-- ============================================   MEMBER   ================================================= -->
		<!-- ========================================================================================================= -->
				
		<security:authorize access="hasRole('MEMBER')">
		
			
			<%-- PROCESSIONS --%>
			
			<li><a href="procession/member/list.do"><spring:message	code="master.page.procession.member.list" /></a>
			
			<%-- REQUESTS --%>
						
			<li><a href="request/member/list.do"><spring:message code="master.page.request.member.list" /></a></li>	
			
			<%-- BROTHERHOODS --%>
			
			<li><a class="fNiv"><spring:message	code="master.page.brotherhood" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="brotherhood/list.do"><spring:message code="master.page.brotherhood.list" /></a></li>
					<li><a href="brotherhood/allBrotherhoodsFree.do"><spring:message code="master.page.brotherhood.allFree" /></a></li>
					<li><a href="brotherhood/brotherhoodsHasBelonged.do"><spring:message code="master.page.brotherhood.hasBelonged" /></a></li>
				</ul>
			</li>
			
			<%-- FINDER --%>
			
			<li><a href="finder/member/edit.do"><spring:message code="master.page.finder.member.edit" /></a></li>	
			
			
		</security:authorize>
		
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
			<li><a class="fNiv" href="procession/list.do"><spring:message code="master.page.all.processions" /></a></li>
			<li><a class="fNiv" href="brotherhood/create.do"><spring:message code="master.page.brotherhood.register" /></a></li>
			<li><a class="fNiv" href="member/create.do"><spring:message code="master.page.member.register" /></a></li>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
			<li>
				<a class="fNiv"> 
					<spring:message code="master.page.profile" /> 
			        (<security:authentication property="principal.username" />)
				</a>
				<ul>
					<li class="arrow"></li>
					<security:authorize access="hasRole('ADMIN')">
					<li><a href="administrator/edit.do"><spring:message code="master.page.member.edit" /></a></li>
					<li><a href="administrator/display.do"><spring:message code="master.page.member.display" /></a></li>
					</security:authorize>
					<li><a href="socialProfile/list.do"><spring:message code="master.page.actor.socialProfiles" /></a></li>
					<security:authorize access="hasRole('MEMBER')">
					<li><a href="member/edit.do"><spring:message code="master.page.member.edit" /></a></li>
					<li><a href="member/display.do"><spring:message code="master.page.member.display" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('BROTHERHOOD')">
					<li><a href="brotherhood/edit.do"><spring:message code="master.page.brotherhood.edit" /></a></li>
					<li><a href="brotherhood/display.do"><spring:message code="master.page.brotherhood.display" /></a></li>
					</security:authorize>					
					<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
				</ul>
			</li>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

