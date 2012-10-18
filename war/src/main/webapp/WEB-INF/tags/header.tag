<%@tag description="Header template" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp" %>
<%@attribute name="title" required="true" %>
<%@attribute name="showLegend" type="java.lang.Boolean" %>

<header class="${theme}">
	<a href="#" class="menuBtn"></a>	
	<h1>${title}</h1>
</header>

<c:if test="${showLegend}">
	<a href="#" class="legendBtn">
		<fmt:message key="location.legend"/>
	</a>
</c:if>

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
		<li>
			<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.account.AccountActionBean">
				<fmt:message key="account.title"/>
			</stripes:link>
		</li>
	</ul>
</nav>
<article id="legend" class=" hidden">
	<section>
		<nav class="simpleList">
			<ul>
				<li>
					<span><fmt:message key="location.legend.my.location"/></span>
				</li>
				<li>
					<span><fmt:message key="location.legend.car.location"/></span>
				</li>
				<li>
					<span><fmt:message key="location.legend.regular.zone"/></span>
				</li>
				<li>
					<span><fmt:message key="location.legend.undesire.zone"/></span>
				</li>
			</ul>
		</nav>
	</section>
</article>