<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>



<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ page import="java.util.*" %>

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.6.0/Chart.min.js"></script>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
</head>


<security:authorize access="hasRole('ADMIN')">
 
 <!-- TABLE --> 
 <table >
 <spring:message code="dashboard.brotherhoods" var="brotherhoodsH" />
 <caption><jstl:out value="${brotherhoodsH}" /></caption>

    <tr >
    <td><spring:message code="average.members.brotherhood" var="averageH" /><jstl:out value="${averageH}" /></td>
    <td>${averageMembers}</td>
    </tr>
    <tr >
    <td><spring:message code="min.members.brotherhood" var="minH" /><jstl:out value="${minH}" /></td>
    <td>${minMembers}</td>
    </tr>
    <tr >
    <td><spring:message code="max.members.brotherhood" var="maxH" /><jstl:out value="${maxH}" /></td>
    <td>${maxMembers}</td>
    </tr>
    <tr > 
    <td><spring:message code="desviation.members.brotherhood" var="desH" /><jstl:out value="${desH}" /></td>
    <td>${desviationMembers}</td>
    </tr>
    <tr >
    <td><spring:message code="largest.brotherhood" var="largestH" /><jstl:out value="${largestH}" /></td>
    <td>${largest}</td>
    </tr>
    <tr >
    <td><spring:message code="smallest.brotherhood" var="smallestH" /><jstl:out value="${smallestH}" /></td>
    <td>${smallest}</td>
    </tr>
  </table>
  
  
  <table>
  <spring:message code="dashboard.processions" var="processionsH" />
 <caption><jstl:out value="${processionsH}" /></caption>
    <tr >
    <td><spring:message code="processions.soon" var="soonH" /><jstl:out value="${soonH}" /></td>
    <td>${soon}</td>
    </tr>
    <tr >
    <td><spring:message code="requests.approved" var="reqaH" /><jstl:out value="${reqaH}" /></td>
    <td>${requestsApproved}</td>
    </tr>
     <tr >
    <td><spring:message code="requests.pending" var="reqpH" /><jstl:out value="${reqpH}" /></td>
    <td>${requestsPending}</td>
    </tr>
     <tr>
    <td><spring:message code="requests.rejected" var="reqrH" /><jstl:out value="${reqrH}" /></td>
    <td>${requestsRejected}</td>
    </tr>
     <tr>
    <td><spring:message code="members.percent" var="memberspH" /><jstl:out value="${memberspH}" /></td>
    <td>${membersPercent}</td>
    </tr>
    </table>
    
    <table>
    <spring:message code="dashboard.areas" var="areasH" />
 <caption><jstl:out value="${areasH}" /></caption>
     <tr >
    <td><spring:message code="average.brotherhood.area" var="averageBH" /><jstl:out value="${averageBH}" /></td>
    <td>${averageBrotherhoods}</td>
    </tr>
    <tr >
    <td><spring:message code="min.brotherhood.area" var="minBH" /><jstl:out value="${minBH}" /></td>
    <td>${minBrotherhoods}</td>
    </tr>
    <tr >
    <td><spring:message code="max.brotherhood.area" var="maxBH" /><jstl:out value="${maxBH}" /></td>
    <td>${maxBrotherhoods}</td>
    </tr>
    <tr >
    <td><spring:message code="desviation.brotherhood.area" var="desBH" /><jstl:out value="${desBH}" /></td>
    <td>${desviationBrotherhoods}</td>
    </tr>
    <td><spring:message code="ratio.brotherhood.area" var="ratioBH" /><jstl:out value="${ratioBH}" /></td>
    <td>${ratioBrotherhoods}</td>
    </tr>
    <tr >
    <td><spring:message code="ratio.no.coordinated.area" var="maxBH" /><jstl:out value="${maxBH}" /></td>
    <td>${ratioNoCoordinatedArea}</td>
    </tr>
    </table>
    
    <table>
    <spring:message code="dashboard.finders" var="findersH" />
 <caption><jstl:out value="${findersH}" /></caption>
    <tr >
    <td><spring:message code="average.results" var="averageRH" /><jstl:out value="${averageRH}" /></td>
    <td>${averageResults}</td>
    </tr>
    <tr >
    <td><spring:message code="min.results" var="minRH" /><jstl:out value="${minRH}" /></td>
    <td>${minResults}</td>
    </tr>
    <tr >
    <td><spring:message code="max.results" var="maxRH" /><jstl:out value="${maxRH}" /></td>
    <td>${maxResults}</td>
    </tr>
    <tr >
    <td><spring:message code="desviation.results" var="desRH" /><jstl:out value="${desRH}" /></td>
    <td>${desviationResults}</td>
    </tr>
    <tr >
    <td><spring:message code="ratio.finders" var="ratioFH" /><jstl:out value="${ratioFH}" /></td>
    <td>${ratioFinders}</td>
    </tr>
    
    <table>
    <spring:message code="dashboard.procession" var="findersH" />
 <caption><jstl:out value="${findersH}" /></caption>
 	<jstl:forEach items="${parades }" var="parade">
    <tr >
    <td><jstl:out value="${parade.title}" /></td>
    <td><spring:url value="dashboard/administrator/statistics.do" var="displayURL">
				<spring:param name="id" value="${parade.id }"/> <!-- es el par�metro que va a acompa�ar a la url -->
			</spring:url>
			<a href="${displayURL}"><spring:message code="dashboard.calculate"/></a></td>
    </tr>
    </jstl:forEach>
    <tr >
    <td><spring:message code="requests.approved" var="reqaH" /><jstl:out value="${reqaH}" /></td>
    <td>${requestsParadeApproved}</td>
    </tr>
     <tr >
    <td><spring:message code="requests.pending" var="reqpH" /><jstl:out value="${reqpH}" /></td>
    <td>${requestsParadePending}</td>
    </tr>
     <tr>
    <td><spring:message code="requests.rejected" var="reqrH" /><jstl:out value="${reqrH}" /></td>
    <td>${requestsParadeRejected}</td>
    </tr>
    	
       
    </table>
    
    <table>
    <spring:message code="dashboard.histories" var="historiesH" />
 <caption><jstl:out value="${historiesH}" /></caption>
    <tr >
    <td><spring:message code="average.histories" var="averageHH" /><jstl:out value="${averageHH}" /></td>
    <td>${avgStatisticsRecord}</td>
    </tr>
    <tr >
    <td><spring:message code="min.histories" var="minHH" /><jstl:out value="${minHH}" /></td>
    <td>${minAvgStatisticsRecord}</td>
    </tr>
    <tr >
    <td><spring:message code="max.histories" var="maxHH" /><jstl:out value="${maxHH}" /></td>
    <td>${maxAvgStatisticsRecord}</td>
    </tr>
    <tr >
    <td><spring:message code="desviation.histories" var="desHH" /><jstl:out value="${desHH}" /></td>
    <td>${stddevAvgStatisticsRecord}</td>
    </tr>
    <tr >
    <td><spring:message code="largest.brotherhood.history" var="largestHH" /><jstl:out value="${largestHH}" /></td>
    <td>${largestBrotherhoodPerHistory}</td>
    </tr>
    <tr >
    <td><spring:message code="brotherhood.history" var="brotherhoodHH" /><jstl:out value="${brotherhoodHH}" /></td>
    <td>${brotherhoodPerHistory}</td>
    </tr>
    
    <table>
    
        <table>
    <spring:message code="dashboard.chapters" var="chaptersH" />
 <caption><jstl:out value="${chaptersH}" /></caption>
    <tr >
    <td><spring:message code="average.chapter" var="averageCH" /><jstl:out value="${averageCH}" /></td>
    <td>${avgStatisticsParadesByChapter}</td>
    </tr>
    <tr >
    <td><spring:message code="min.chapter" var="minCH" /><jstl:out value="${minCH}" /></td>
    <td>${minStatisticsParadesByChapter}</td>
    </tr>
    <tr >
    <td><spring:message code="max.chapter" var="maxCH" /><jstl:out value="${maxCH}" /></td>
    <td>${maxStatisticsParadesByChapter}</td>
    </tr>
    <tr >
    <td><spring:message code="deviation.chapter" var="desCH" /><jstl:out value="${desCH}" /></td>
    <td>${stddevStatisticsParadesByChapter}</td>
    </tr>
    <tr >
    <td><spring:message code="ratio.chapter" var="ratioCH" /><jstl:out value="${ratioCH}" /></td>
    <td>${chapterTenPercent}</td>
    </tr>
    <table>
    
    <table>
    <spring:message code="dashboard.parades" var="paradesH" />
 <caption><jstl:out value="${paradesH}" /></caption>
    <tr >
    <td><spring:message code="ratio.parade" var="ratioPH" /><jstl:out value="${ratioPH}" /></td>
    <td>${ratioDraftFinalParade}</td>
    </tr>
    <tr >
    <td><spring:message code="submitted.parade" var="submittedPH" /><jstl:out value="${submittedPH}" /></td>
    <td>${submittedParadeRatio}</td>
    </tr>
    <tr >
    <td><spring:message code="accepted.parade" var="acceptedPH" /><jstl:out value="${acceptedPH}" /></td>
    <td>${acceptedParadeRatio}</td>
    </tr>
    <tr >
    <td><spring:message code="rejected.parade" var="rejectedPH" /><jstl:out value="${rejectedPH}" /></td>
    <td>${rejectedParadeRatio}</td>
    </tr>
    <table>
    
    
    <table>
    <div class="container">
    <canvas id="myChart" ></canvas>
  </div>
  
  <spring:message code="dashboard.positions.frequency" var="titulo"></spring:message>
  
  <script>
  	
  
    let myChart = document.getElementById('myChart').getContext('2d');

    // Global Options
    Chart.defaults.global.defaultFontFamily = 'Lato';
    Chart.defaults.global.defaultFontSize = 18;
    Chart.defaults.global.defaultFontColor = '#777';
    

    let massPopChart = new Chart(myChart, {
      type:'bar', // bar, horizontalBar, pie, line, doughnut, radar, polarArea
      data:{
        labels:[
<jstl:forEach items="${positions2}" var="position">
<%="\"" %><jstl:out value="${position}" /><%="\"" %>,
</jstl:forEach>
		                   
		                   ],
        datasets:[{
          label:'Frequency',
          data:[
		                   <jstl:forEach items="${frequencies2}" var="frequency">
<jstl:out value="${frequency}" />,
</jstl:forEach>,0
		                   
		                   ],
          backgroundColor:'green',
          /*backgroundColor:[
            'rgba(255, 99, 132, 0.6)',
            'rgba(54, 162, 235, 0.6)',
            'rgba(255, 206, 86, 0.6)',
            'rgba(75, 192, 192, 0.6)',
            'rgba(153, 102, 255, 0.6)',
            'rgba(255, 159, 64, 0.6)',
            'rgba(255, 99, 132, 0.6)'
          ],*/
          width:50,
          height:50,
          borderWidth:1,
          borderColor:'#777',
          hoverBorderWidth:3,
          hoverBorderColor:'#000'
        }]
      },
      options:{
        title:{
          display:true,
          text:<%="\'" %><jstl:out value="${titulo}" /><%="\'" %>,
          fontSize:25
        },
        legend:{
          display:true,
          position:'right',
          labels:{
            fontColor:'#000'
          }
        },
        layout:{
          padding:{
            left:50,
            right:0,
            bottom:0,
            top:0
          }
        },
        tooltips:{
          enabled:true
        }
      }
    });
  </script>
    
    
    
    
    </table>
    


</table>

</security:authorize>
