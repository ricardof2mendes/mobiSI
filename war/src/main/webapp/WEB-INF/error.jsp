<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='error.title' /></c:set>

<t:main title="${title}">
	<div class="newerrors">
		<label><fmt:message key="error.message.header"/></label><br/>
        <label>
            <c:choose>
                <c:when test="${actionBean.context.user.carClub ne null}">
                    <fmt:message key="error.message">
                        <fmt:param>${actionBean.context.user.carClub.carClubContactPhone}</fmt:param>
                        <fmt:param>${actionBean.context.user.carClub.carClubContactEmail}</fmt:param>
                    </fmt:message>
                </c:when>
                <c:when test="${actionBean.context.carClub ne null}">
                    <fmt:message key="error.message">
                        <fmt:param>${actionBean.context.carClub.phone}</fmt:param>
                        <fmt:param>${actionBean.context.carClub.email}</fmt:param>
                    </fmt:message>
                </c:when>
                <c:otherwise>
                    <fmt:message key="error.message.generic"/>
                </c:otherwise>
            </c:choose>
        </label>
	</div>
</t:main>
