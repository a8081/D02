
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<style type="text/css">
div.gallery {
  margin: 5px;
  border: 1px solid #ccc;
  float: left;
  width: 180px;
}

div.gallery:hover {
  border: 1px solid #777;
}

div.gallery img {
  width: 100%;
  height: auto;
}

div.desc {
  padding: 15px;
  text-align: center;
}
div.galleryContainer{
	display: inline-block;
}
</style>

<h2>
<acme:display code="float.title" value="${f.title}" />
</h2>
<div class="galleryContainer">
	<jstl:forEach items="${f.pictures}" var="picture" varStatus="loop">
				<div class="gallery">
				  <a target="_blank" href="${picture}">
				    <img src="${picture}" alt="${picture}" width="600" height="400">
				  </a>
				</div>
	</jstl:forEach>
</div>
<br>

<acme:display code="float.description" value="${f.description}" />



