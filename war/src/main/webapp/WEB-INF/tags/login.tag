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
        <link rel="stylesheet" href="${contextPath}/css/default.css" />
        <link rel="stylesheet" href="${contextPath}/css/normalize.css" />
    </head>
    <body class="${theme}">
        <jsp:doBody/>
    </body>
    
</html>
