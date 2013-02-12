<%@tag description="Main page template" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<%@attribute name="title" required="true" %>
<c:set var="version" scope="page">
	<fmt:message key="application.version"/>
</c:set>

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

        <!-- CSS -->
        <link rel="stylesheet" href="${contextPath}/css/style.map.min.css?version=${version}" />
    </head>
    
    <body>

        <t:header title="${title}"/>
        
        <jsp:doBody/>

        <!-- JS -->
        <script type="text/javascript" src="${contextPath}/js/zepto.js"></script>
        <script type="text/javascript" src="${contextPath}/js/OpenLayers.mobile.js"></script>
        <script type="text/javascript" src="${contextPath}/js/script.map.min.jsp?version=${version}"></script>
    </body>
</html>
