<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='error.title' /></c:set>

<t:main title="${title}">
	<div class="newerrors">
		<label><fmt:message key="error.message.header"/></label><br/>
		<label>
			<fmt:message key="error.message">
				<fmt:param>${actionBean.context.user.carClub.carClubContactPhone}</fmt:param>
				<fmt:param>${actionBean.context.user.carClub.carClubContactEmail}</fmt:param>
			</fmt:message>
		</label>
	</div>
</t:main>
