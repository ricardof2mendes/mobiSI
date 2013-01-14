<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page">
	<fmt:message key='location.title' />
</c:set>

<t:map title="${title}">

	<jsp:include page="/WEB-INF/common/legend.jsp"></jsp:include>
	
	<c:choose>
    	<c:when test="${param.carLocation == ''}">
	        <span id="paramLicensePlate" class="hidden">${param.licensePlate}</span>
    	</c:when>
    	<c:when test="${param.parkLocation == ''}">
    		<span id="paramZone" class="hidden">${param.zone}</span>
    	</c:when>
    </c:choose>
    
	<article>
		<section>
			<div id="container">	
				<div id="map"><!-- Map will be included here--></div>
			</div>		
		</section>
	</article>
		
</t:map>

