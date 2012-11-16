<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>

<c:choose>
	<c:when test="${actionBean.fieldErrors}">
		<stripes:errors/>
	</c:when>
	<c:otherwise>
		<div class="globalErrors">
			<stripes:errors/>
		</div>
	</c:otherwise>
</c:choose>

<stripes:messages/>