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
					<li><a href="ban/administrator/list.do"><spring:message	code="master.page.ban" /></a></li>
					<li><a href="sponsorship/administrator/launchDeactivate.do"><spring:message	code="master.page.administrator.deactivate" /></a></li>	
				</ul>
			</li>
			
			<li><a href="position/administrator/list.do"><spring:message	code="master.page.position" /></a>
				
			</li>
			
			<li><a href="area/administrator/list.do"><spring:message	code="master.page.area" /></a>
				
			</li>
			
			<li><a href="dashboard/administrator/statistics.do"><spring:message	code="master.page.dashboard" /></a>
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

		<%-- PARADES --%>
			<li><a class="fNiv"><spring:message	code="master.page.parades" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="parade/brotherhood/list.do"><spring:message code="master.page.all.parade" /></a></li>
					<li><a href="parade/brotherhood/create.do"><spring:message code="master.page.parade.create" /></a></li>
					<li><a href="parade/brotherhood/listDefault.do"><spring:message code="master.page.parade.default" /></a></li>
					<li><a href="parade/brotherhood/listAccepted.do"><spring:message code="master.page.parade.accepted" /></a></li>
					<li><a href="parade/brotherhood/listRejected.do"><spring:message code="master.page.parade.rejected" /></a></li>
					<li><a href="parade/brotherhood/listSubmitted.do"><spring:message code="master.page.parade.submitted" /></a></li>
				</ul>
			</li>
			
		<%-- MEMBERS --%>
			
			<li><a href="member/list.do"><spring:message code="master.page.member.list" /></a></li>
			
		<%-- HISTORY --%>
		
			<li><a href="history/list.do"><spring:message code="master.page.history" /></a></li>
		
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
		
			
			<%-- PARADES --%>
			
			<li><a href="parade/member/list.do"><spring:message	code="master.page.parade.member.list" /></a>
			
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
		
		<!-- ========================================================================================================= -->
		<!-- ========================================= CHAPTER =================================================== -->
		<!-- ========================================================================================================= -->	
	
		<security:authorize access="hasRole('CHAPTER')">
		
		
		<%-- PARADES --%>
		
		<li><a class="fNiv"><spring:message	code="master.page.parades.chapter.list" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="parade/chapter/listAccepted.do"><spring:message code="master.page.parade.accepted" /></a></li>
					<li><a href="parade/chapter/listRejected.do"><spring:message code="master.page.parade.rejected" /></a></li>
					<li><a href="parade/chapter/listSubmitted.do"><spring:message code="master.page.parade.submitted" /></a></li>
				</ul>
			</li>
		
		</security:authorize>
		
		<!-- ========================================================================================================= -->
		<!-- ========================================= SPONSOR =================================================== -->
		<!-- ========================================================================================================= -->	
	
		<security:authorize access="hasRole('SPONSOR')">
		
		<%-- PARADES --%>
		
		<li><a href="parade/sponsor/list.do"><spring:message code="master.page.parade.sponsor" /></a></li>
		
		<%-- SPONSORSHIP --%>
		
		<li><a href="sponsorship/sponsor/list.do"><spring:message code="master.page.sponsor.sponsorship" /></a></li>
		
		</security:authorize>
		
		<!-- ========================================================================================================= -->
		<!-- ========================================== ANONYMOUS ==================================================== -->
		<!-- ========================================================================================================= -->	
	
		
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
			<li><a class="fNiv" href="brotherhood/listAll.do"><spring:message code="master.page.all.brotherhood" /></a></li>
			<li><a class="fNiv" href="brotherhood/create.do"><spring:message code="master.page.brotherhood.register" /></a></li>
			<li><a class="fNiv" href="member/create.do"><spring:message code="master.page.member.register" /></a></li>
			<li><a class="fNiv" href="sponsor/create.do"><spring:message code="master.page.sponsor.register" /></a></li>
			<li><a class="fNiv" href="chapter/create.do"><spring:message code="master.page.chapter.register" /></a></li>
		</security:authorize>
		
		
		<!-- ========================================================================================================= -->
		<!-- ======================================== AUTHENTICATED ================================================== -->
		<!-- ========================================================================================================= -->	
	
		
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
					<security:authorize access="hasRole('SPONSOR')">
					<li><a href="sponsor/edit.do"><spring:message code="master.page.sponsor.edit" /></a></li>
					<li><a href="sponsor/display.do"><spring:message code="master.page.sponsor.display" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('CHAPTER')">
					<li><a href="chapter/edit.do"><spring:message code="master.page.chapter.edit" /></a></li>
					<li><a href="chapter/display.do"><spring:message code="master.page.chapter.display" /></a></li>
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

