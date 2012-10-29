<%@tag description="Main page template" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<%@attribute name="title" required="true" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <!-- Meta Tags -->
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

        <title>${title}</title>

        <!-- Link Tags -->
        <link rel="icon" href="${contextPath}/favicon.ico" sizes="16x16 32x32" />

        <!-- TODO CSS -->
        <link rel="stylesheet" href="${contextPath}/css/normalize.css" />
        <link rel="stylesheet" href="${contextPath}/css/default.css" />
        <link rel="stylesheet" href="${contextPath}/css/map.css" />
    </head>
    
    <c:set var="script" value=""/>
    <c:choose>
    	<c:when test="${param.carLocation == '' && param.parkLocation == null}">
	    	<c:set var="script" value="MapACar('${param.licensePlate}');"/>
	    	<c:set var="height" value="298px"/>
    	</c:when>
    	<c:when test="${param.parkLocation == '' && param.carLocation == null}">
    		<c:set var="script" value="MapAPark('${param.zone}');"/>
    		<c:set var="height" value="160px"/>
    	</c:when>
    	<c:otherwise></c:otherwise>
    </c:choose>
    <body onload="${script}">

        <t:header title="${title}" showLegend="true"/>
        
        <jsp:doBody/>

        <!-- TODO JS -->
        <script>
        	var contextPath = '${contextPath}';
        	var height = '${height}';
        </script>
        <script src="${contextPath}/js/zepto.js"></script>
        <script src="${contextPath}/js/zepto-gfx.js"></script>
        <script src="${contextPath}/js/OpenLayers.mobile.js"></script>
        <script src="${contextPath}/js/map.js"></script>
    </body>
</html>
