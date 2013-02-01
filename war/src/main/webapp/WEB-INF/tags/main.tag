<%@tag description="Main page template" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<%@attribute name="title" required="true" %>
<%@attribute name="addCalendar" type="java.lang.Boolean" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <!-- Meta Tags -->
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; minimum-scale=1.0; user-scalable=0;" />
        <meta name="handheldfriendly" content="true"/>
        <meta name="apple-mobile-web-app-capable" content="yes">
        <meta name="format-detection" content="telephone=no" />

        <title>${title}</title>

        <!-- Link Tags -->
        <link rel="icon" sizes="16x16" href="${contextPath}/favicon16.png" />
        <link rel="icon" sizes="32x32" href="${contextPath}/favicon32.png" />
        
		<!-- For non-Retina iPhone, iPod Touch, and Android 2.1+ devices: -->
		<link rel="apple-touch-icon-precomposed" href="${contextPath}/favicon16.png">
        <link rel="apple-touch-icon-precomposed" sizes="32x32" href="${contextPath}/favicon32.png" />
		<!-- For first- and second-generation iPad: -->
		<link rel="apple-touch-icon-precomposed" sizes="57x57" href="${contextPath}/favicon57.png">
        <!-- For iPhone with high-resolution Retina display: -->
		<link rel="apple-touch-icon-precomposed" sizes="114x114" href="${contextPath}/favicon114.png">
		        
        <!-- TODO CSS -->
        <link rel="stylesheet" href="${contextPath}/css/normalize.css" />
        <link rel="stylesheet" href="${contextPath}/css/default.css" />
        
        <!-- Calendar -->
        <c:if test="${addCalendar}">
	        <link rel="stylesheet" href="${contextPath}/css/mobiscroll.core-2.0.3.css" />
        </c:if>
    </head>
    <body>
		<div class="modal"><!-- Place at bottom of page --></div>
        
        <t:header title="${title}"/>

        <jsp:doBody/>

        <!-- TODO JS -->
        <script src="${contextPath}/js/zepto.js"></script>
        <script>
        	var CONTEXT_PATH = '${contextPath}';
        	var ZONE_ALL = '<fmt:message key="book.advance.zone.any" />';
        	// date patterns for calendar
        	var DATE_PATTERN = '${applicationScope.configuration.jsDatePattern}';
        	var TIME_PATTERN = '${applicationScope.configuration.jsTimePattern}';
        	// booking status for ajax pooling
        	var WAITING = 'WAIT_OBS_';
     		var ERROR = 'OBS_ERROR';
     		var IN_USE = 'IN_USE';
     		// unwanted zone
     		var UNWANTED_ZONE = 'UNWANTED';
        </script>
        
        
        <!-- Calendar -->
        <c:if test="${addCalendar}">
      		<script src="${contextPath}/js/mobiscroll-2.3.1.custom.min.js"></script>      		
        </c:if>
        <script src="${contextPath}/js/default.js"></script>
    </body>
</html>
