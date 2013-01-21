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
        <link rel="icon" href="${contextPath}/favicon.ico" sizes="16x16 32x32" />

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
        <script src="${contextPath}/js/zepto-gfx.js"></script>
        
        <!-- Calendar -->
        <script>
        	var CONTEXT_PATH = '${contextPath}';
        	var ZONE_ALL = '<fmt:message key="book.advance.zone.any" />';
        	var DATE_PATTERN = '${applicationScope.configuration.jsDatePattern}';
        	var TIME_PATTERN = '${applicationScope.configuration.jsTimePattern}';
        </script>
        <c:if test="${addCalendar}">
      		<script src="${contextPath}/js/mobiscroll-2.3.1.custom.min.js"></script>      		
        </c:if>
        <script src="${contextPath}/js/default.js"></script>
    </body>
</html>
