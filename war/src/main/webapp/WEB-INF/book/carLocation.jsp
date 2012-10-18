<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page">
	<fmt:message key='location.title' />
</c:set>

<t:map title="${title}">
	
	<div id="map" class="olMap"></div>
		
</t:map>

