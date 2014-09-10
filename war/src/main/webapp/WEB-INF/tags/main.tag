<%@tag description="Main page template" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<%@attribute name="title" required="true" %>
<%@attribute name="addCalendar" type="java.lang.Boolean" %>
<c:set var="version" scope="page">
	<fmt:message key="application.version"/>
</c:set>
<c:set var="state" scope="page" value="${applicationScope.configuration.applicationState}"/>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <!-- Meta Tags -->
        <meta charset="UTF-8" />
        <meta name="version" content="${project.parent.version}" />
        <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0" />
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
		        
        <!-- CSS -->
        <c:choose>
	        <c:when test="${addCalendar}">
	        	<c:choose>
	        		<c:when test="${pageScope.state == 'PRODUCTION'}">
				        <link rel="stylesheet" href="${contextPath}/css/style.calendar.min.css?version=${version}" />
	        		</c:when>
	        		<c:when test="${pageScope.state == 'DEVELOPMENT'}">
				        <link rel="stylesheet" href="${contextPath}/css/style.calendar.css?version=${version}" />
	        		</c:when>
	        		<c:otherwise>
	        			<link rel="stylesheet" href="${contextPath}/css/normalize.css" />
	         			<link rel="stylesheet" href="${contextPath}/css/default.css" />
	         			<link rel="stylesheet" href="${contextPath}/css/mobiscroll.custom.css" />
	        		</c:otherwise>
	        	</c:choose>
        	</c:when>
        	<c:otherwise>
        		<c:choose>
	        		<c:when test="${pageScope.state == 'PRODUCTION'}">
		        		<link rel="stylesheet" href="${contextPath}/css/style.min.css?version=${version}" />		
	        		</c:when>
	        		<c:when test="${pageScope.state == 'DEVELOPMENT'}">
		        		<link rel="stylesheet" href="${contextPath}/css/style.css?version=${version}" />		
	        		</c:when>
	        		<c:otherwise>
	        			<link rel="stylesheet" href="${contextPath}/css/normalize.css" />
	         			<link rel="stylesheet" href="${contextPath}/css/default.css" />
	        		</c:otherwise>
	        	</c:choose>
        	</c:otherwise>
        </c:choose>
    </head>

    <body >
		<div class="modal"><!-- Place at bottom of page --></div>
        
        <t:header title="${title}"/>

        <jsp:doBody/>

        <!--JS -->
        <c:choose>
	        <c:when test="${addCalendar}">
     	        <c:choose>
	        		<c:when test="${pageScope.state == 'PRODUCTION'}">
				        <script type="text/javascript" src="${contextPath}/js/script.calendar.min.jsp?version=${version}"></script>
	        		</c:when>
	        		<c:when test="${pageScope.state == 'DEVELOPMENT'}">
				        <script type="text/javascript" src="${contextPath}/js/script.calendar.jsp?version=${version}"></script>
	        		</c:when>
	        		<c:otherwise>
				        <script type="text/javascript" src="${contextPath}/js/zepto.js"></script>
				        <script type="text/javascript" src="${contextPath}/js/mobiscroll.datetime-2.4.1.min.js"></script>
				        <script type="text/javascript" src="${contextPath}/js/moment.js"></script>
				        
				        <script type="text/javascript" >
				     		// *****************************************************************
			        		// * Any variable declared here must be replicated in variables.js *
			        		// *****************************************************************
			        	
				        	// context path
					        var CONTEXT_PATH = '${pageContext.request.contextPath}';
					        // pooling interval
					        var POOLING_INTERVAL = '${applicationScope.configuration.pollingIntervalMilliseconds}';
					        var UNLOCK_TIMEOUT_INTERVAL = '${applicationScope.configuration.unlockPollingTimeoutMilliseconds}';
					        var LOCK_TIMEOUT_INTERVAL = '${applicationScope.configuration.lockEndPollingTimeoutMilliseconds}';
					        // localizable strings
					        var ZONE_ALL_LABEL = '<fmt:message key="book.advance.zone.any" />';
					        var GEOLOCATION_NOT_SUPPORTED_LABEL = '<fmt:message key="geolocation.alert.msg.not.supported"/>';
					        var OK_LABEL = '<fmt:message key="calendar.ok"/>';
					        var MINUTES_LABEL = '<fmt:message key="calendar.minutes"/>';
					        // date patterns for calendar
					        var DATE_TIME_PATTERN = '${applicationScope.configuration.jsDateTimePattern}';
					        var DATE_PATTERN = '${applicationScope.configuration.jsDatePattern}';
					        var TIME_PATTERN = '${applicationScope.configuration.jsTimePattern}';
				        </script>
				        
				        <script type="text/javascript" src="${contextPath}/js/default.js"></script>
	        		</c:otherwise>
	        	</c:choose>
        	</c:when>
        	<c:otherwise>
        		<c:choose>
	        		<c:when test="${pageScope.state == 'PRODUCTION'}">
				        <script type="text/javascript" src="${contextPath}/js/script.min.jsp?version=${version}"></script>
	        		</c:when>
	        		<c:when test="${pageScope.state == 'DEVELOPMENT'}">
	        			<script type="text/javascript" src="${contextPath}/js/script.jsp?version=${version}"></script>
	        		</c:when>
	        		<c:otherwise>
				        <script type="text/javascript" src="${contextPath}/js/zepto.js"></script>
				        <script type="text/javascript" >
				        	// *****************************************************************
				        	// * Any variable declared here must be replicated in variables.js *
				        	// *****************************************************************
				        	
					     	// context path
					        var CONTEXT_PATH = '${pageContext.request.contextPath}';
					        // pooling interval
					        var POOLING_INTERVAL = '${applicationScope.configuration.pollingIntervalMilliseconds}';
					        var UNLOCK_TIMEOUT_INTERVAL = '${applicationScope.configuration.unlockPollingTimeoutMilliseconds}';
					        var LOCK_TIMEOUT_INTERVAL = '${applicationScope.configuration.lockEndPollingTimeoutMilliseconds}';
					        // localizable strings
					        var ZONE_ALL_LABEL = '<fmt:message key="book.advance.zone.any" />';
					        var GEOLOCATION_NOT_SUPPORTED_LABEL = '<fmt:message key="geolocation.alert.msg.not.supported"/>';
					        var OK_LABEL = '<fmt:message key="calendar.ok"/>';
					        var MINUTES_LABEL = '<fmt:message key="calendar.minutes"/>';
					        // date patterns for calendar
					        var DATE_TIME_PATTERN = '${applicationScope.configuration.jsDateTimePattern}';
					        var DATE_PATTERN = '${applicationScope.configuration.jsDatePattern}';
					        var TIME_PATTERN = '${applicationScope.configuration.jsTimePattern}';
				        </script>
				        
				        <script type="text/javascript" src="${contextPath}/js/default.js"></script>
	        		</c:otherwise>
	        	</c:choose>
        	</c:otherwise>
        </c:choose>
    </body>
</html>
