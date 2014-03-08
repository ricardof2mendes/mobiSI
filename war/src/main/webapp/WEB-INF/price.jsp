<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='base.plan' /></c:set>

<t:main title="${title}">
    <div class="price">
        <p><fmt:message key="base.plan.first.paragraph"/></p>
        <p><fmt:message key="base.plan.second.paragraph"/></p>
        <p><fmt:message key="base.plan.third.paragraph"/></p>
        <p><fmt:message key="base.plan.fourth.paragraph"/></p>
        <p><fmt:message key="base.plan.fifth.paragraph"/></p>
    </div>
</t:main>

