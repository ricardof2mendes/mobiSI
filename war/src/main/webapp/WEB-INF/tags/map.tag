<%@tag description="Main page template" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<%@attribute name="title" required="true" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <!-- Meta Tags -->
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; minimum-scale=1.0; user-scalable=0;" />
        <meta name="handheldfriendly" content="true"/>
        <meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="defaul|black|black-translucent">

        <title>${title}</title>

        <!-- Link Tags -->
        <link rel="icon" href="${contextPath}/favicon.ico" sizes="16x16 32x32" />

        <!-- TODO CSS -->
        <link rel="stylesheet" href="${contextPath}/css/normalize.css" />
        <link rel="stylesheet" href="${contextPath}/css/default.css" />
        <link rel="stylesheet" href="${contextPath}/css/map.css" />
    </head>
    
    <c:choose>
    	<c:when test="${param.carLocation == ''}">
	    	<c:set var="script" value="MapACar('${param.licensePlate}');"/>
	    	<c:set var="height" value="298px"/>
    	</c:when>
    	<c:when test="${param.parkLocation == ''}">
    		<c:set var="script" value="MapAPark('${param.zone}');"/>
    		<c:set var="height" value="160px"/>
    	</c:when>
    	<c:when test="${param.searchImmediateInMap == ''}">
    		<c:set var="script" value="MapASearch('${param.price}', '${param.distance}', '${param.clazz}', '${param.fuel}', '${param.orderBy}', '${param.latitude}', '${param.longitude}');"/>
    		<c:set var="height" value="160px"/>
    	</c:when>
    	<c:otherwise></c:otherwise>
    </c:choose>
    
    <body onload="${script}">

        <t:header title="${title}"/>
        
        <jsp:doBody/>

        <!-- TODO JS -->
        <script>
        	var contextPath = '${contextPath}';
        	var height = '${height}';
        </script>
        <script src="${contextPath}/js/zepto.js"></script>
        <script src="${contextPath}/js/zepto-gfx.js"></script>

        <script src="${contextPath}/js/OpenLayers.mobile.js"></script>
		<script src="${contextPath}/js/Button.js"></script>
        
        <script src="${contextPath}/js/map.js"></script>
    </body>
</html>
