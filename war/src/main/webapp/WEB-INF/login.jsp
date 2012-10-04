<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='login.title' /></c:set>

<t:login title="${title}">
	<article>
		<section id="logo">
		
		</section>
		<section id="login">
			<stripes:form class="loginForm" beanclass="com.criticalsoftware.mobics.presentation.action.LoginActionBean" method="post">
			    <stripes:errors/>
				<div>
					<input type="text" id="username" name="username" placeholder="<fmt:message key="login.j_username" />"/>
					<input type="password" id="password" name="password" placeholder="<fmt:message key="login.j_password" />"/>
				</div>
				<stripes:submit id="authenticate" name="authenticate" class="loginBtn"><fmt:message key="login.submit"/></stripes:submit>
			</stripes:form>
		</section>
	</article>
	<footer class="clear">
		<fmt:message key="login.powered"/>
	</footer>
</t:login>
