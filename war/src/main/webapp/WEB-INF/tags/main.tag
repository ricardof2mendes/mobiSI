<%@tag description="Main page template" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<%@attribute name="title" required="true" %>
<%@attribute name="addCalendar" type="java.lang.Boolean" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <!-- Meta Tags -->
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
        <meta name="apple-mobile-web-app-capable" content="yes">

        <title>${title}</title>

        <!-- Link Tags -->
        <link rel="icon" href="${contextPath}/favicon.ico" sizes="16x16 32x32" />

        <!-- TODO CSS -->
        <link rel="stylesheet" href="${contextPath}/css/normalize.css" />
        <link rel="stylesheet" href="${contextPath}/css/default.css" />
        
        <!-- Calendar -->
        <c:if test="${addCalendar}">
	        <link rel="stylesheet" href="${contextPath}/css/mobiscroll.core-2.0.3.css" />
	        <link rel="stylesheet" href="${contextPath}/css/mobiscroll.ios-2.0.2.css" />
        </c:if>
    </head>
    <body>

        <t:header title="${title}" showLegend="false"/>

        <jsp:doBody/>

		<div class="modal"><!-- Place at bottom of page --></div>

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
      		<script src="${contextPath}/js/mobiscroll.zepto-2.0.3.js"></script>
      		<script src="${contextPath}/js/mobiscroll.core-2.0.3.js"></script>
      		<script src="${contextPath}/js/mobiscroll.datetime-2.0.3.js"></script>      		
        </c:if>
        <script src="${contextPath}/js/default.js"></script>
    </body>
</html>
