<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='login.title' /></c:set>
<c:set var="placeholderLogin" scope="page"><fmt:message key="login.j_username" /></c:set>
<c:set var="placeholderPass" scope="page"><fmt:message key="login.j_password" /></c:set>

<t:login title="${title}">
	<section id="logo">
		<div>
			<img class="splashImage" src="${contextPath}/Login.action?getSplashScreenImage=&CC=${param.CC}" />
		</div>
	</section>
	<section id="loginErrors">
		<div class="globalErrors">
			<stripes:errors/>
		</div>
	</section>
	<section id="login">
		<stripes:form class="loginForm" beanclass="com.criticalsoftware.mobics.presentation.action.LoginActionBean" method="post">
			<div>
				<stripes:hidden id="retina" name="retina"/>
				<stripes:text type="email" id="username" name="username" placeholder="${placeholderLogin}"/>
				<stripes:password id="password" name="password" placeholder="${placeholderPass}"/>
			</div>
			<stripes:submit id="authenticate" name="authenticate" class="submitBtn loginBtn"><fmt:message key="login.submit"/></stripes:submit>
		</stripes:form>
	</section>
	<section id="footer"></section>   
</t:login>
