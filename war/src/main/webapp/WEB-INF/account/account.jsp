<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key="account.title" /></c:set>

<t:main title="${title}">
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
	<article>
		<section>
			<h2><fmt:message key="account.settings"/></h2>
			<nav class="simpleList">
				<ul>
					<li>
						<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.account.EditInformationContactActionBean">
							<fmt:message key="account.information.contact.title"/>
						</stripes:link>
					</li>
					<li>
						<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.account.AccountActionBean" event="authentication">
							<fmt:message key="account.authentication.security.title"/>
						</stripes:link>
					</li>
				</ul>
			</nav>
			
			<h2><fmt:message key="account.session"/></h2>
			<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.LogoutActionBean" class="linkBtn red">
				<fmt:message key="main.logout"/>
			</stripes:link>	
			
			<div class="warningMessage">
				<fmt:message key="account.login.message">
					<fmt:param>${sessionScope.user.username}</fmt:param>
				</fmt:message>
			</div>
		</section>
	</article>
</t:main>