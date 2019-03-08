<%--
 * error.jsp
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

<p>Oops! An error has occurred</p> 
<img src="https://i.dailymail.co.uk/i/pix/2013/11/08/article-0-193F290800000578-749_634x398.jpg" alt="ERROR IMAGE" width="30%" height="30%"/>
<p><a href="<spring:url value='/' />">Return to the welcome page</a><p>
<p><jstl:out value="errorMessage"/>
