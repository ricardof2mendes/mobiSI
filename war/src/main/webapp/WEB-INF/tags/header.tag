<%@tag description="Header template" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp" %>
<%@attribute name="title" required="true" %>

<header class="${theme}Theme">
	<a href="#" class="menuBtn"></a>
	<a href="${contextPath}/Logout.action" class="logoutBtn"></a>
	<h1>${title}</h1>
</header>
<div class="bottomShadow"></div>
<nav id="menu" class="hidden">
	<ul>
		<li>
			<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.HomeActionBean">
				<fmt:message key="home.title"/>
			</stripes:link>
		</li>
		<li><a href="#">Trip</a></li>
		<li><a href="#">Recent</a></li>
		<li><a href="#">Messages</a></li>
		<li><a href="#">Contacts</a></li>
		<li><a href="#">Preferences</a></li>
		<li><a href="#">Account</a></li>
	</ul>
</nav>