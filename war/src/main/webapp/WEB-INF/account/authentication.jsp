<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key="account.authentication.security.title" /></c:set>

<t:main title="${title}">
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
	<article>
		<section>
			<nav>
				<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.account.EditEmailLoginActionBean" class="linkBtn gray">
					<fmt:message key="account.change.email"/>
				</stripes:link>
				<c:if test="${not actionBean.message}">
					<div class="warningMessage">
						<fmt:message key="account.email.pending"/>
					</div>	
				</c:if>
				<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.account.EditPasswordActionBean" class="linkBtn gray">
					<fmt:message key="account.change.password"/>
				</stripes:link>	
				<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.account.EditPinActionBean" class="linkBtn gray">
					<fmt:message key="account.change.pin"/>
				</stripes:link>
			</nav>
		</section>
	</article>
</t:main>