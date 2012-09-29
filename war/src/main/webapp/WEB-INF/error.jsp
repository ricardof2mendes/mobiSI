<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='error.title' /></c:set>

<t:main title="${title}">
    <div class="errors">
        <c:choose>
            <c:when test="${not empty param.httpError}">
                <fmt:message key="error.http.${param.httpError}" />
            </c:when>
            <c:when test="${not empty requestScope.error}">
                ${requestScope.error}
            </c:when>
            <c:when test="${not empty actionBean.context.validationErrors}">
                <stripes:errors />
            </c:when>
            <c:otherwise>
                <fmt:message key="error.internal" />
            </c:otherwise>
        </c:choose>
    </div>
</t:main>
