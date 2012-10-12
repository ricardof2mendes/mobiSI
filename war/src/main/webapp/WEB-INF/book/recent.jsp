<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='recent.book.title' /></c:set>

<t:main title="${title}" hideFooter="true">
	<stripes:messages/>
</t:main>