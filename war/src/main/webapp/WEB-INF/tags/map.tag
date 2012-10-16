<%@tag description="Main page template" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<%@attribute name="title" required="true" %>
<%@attribute name="hideHeader" type="java.lang.Boolean" %>
<%@attribute name="hideFooter" type="java.lang.Boolean" %>

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
    <body onload="Map('${param.licensePlate}');">
        <c:if test="${not hideHeader}">
            <t:header title="${title}"/>
        </c:if>

        <jsp:doBody/>

        <c:if test="${not hideFooter}">
            <t:footer />
        </c:if>

        <!-- TODO JS -->
        <script>var contextPath = '${contextPath}';</script>
        <script src="${contextPath}/js/zepto.js"></script>
        <script src="${contextPath}/js/zepto-gfx.js"></script>
        <script src="${contextPath}/js/default.js"></script> 
        <script src="${contextPath}/js/OpenLayers.mobile.js"></script>
        <script src="${contextPath}/js/map.js"></script>
    </body>
</html>