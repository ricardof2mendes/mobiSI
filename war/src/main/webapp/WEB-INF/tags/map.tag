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

        <title>${title}</title>

        <!-- Link Tags -->
        <link rel="icon" href="${contextPath}/favicon.ico" sizes="16x16 32x32" />

        <!-- TODO CSS -->
        <link rel="stylesheet" href="${contextPath}/css/normalize.css" />
        <link rel="stylesheet" href="${contextPath}/css/default.css" />
        <link rel="stylesheet" href="${contextPath}/css/default.map.css" />
    </head>
    
    <body>

        <t:header title="${title}"/>
        
        <jsp:doBody/>

        <!-- TODO JS -->
        <script>
        	var contextPath = '${contextPath}';
        </script>
        <script src="${contextPath}/js/zepto.js"></script>
        <script src="${contextPath}/js/zepto-gfx.js"></script>

        <script src="${contextPath}/js/OpenLayers.mobile.debug.js"></script>
		<script src="${contextPath}/js/OpenLayers.mobics.extension.js"></script>
        <script src="${contextPath}/js/mobics.map.js"></script>
        <script src="${contextPath}/js/default.map.js"></script>
    </body>
</html>
