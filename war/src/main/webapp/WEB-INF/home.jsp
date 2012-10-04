<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='home.title' /></c:set>

<t:main title="${title}" hideFooter="true">
	<article>
		<section>
			<nav class="simpleList">
				<ul>
					<li>
						<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.HomeActionBean" event="bookCarForNow">
							<fmt:message key="book.now.title"/>
						</stripes:link>
					</li>
					<li>
						<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.HomeActionBean" event="findCarForLater">
							<fmt:message key="find.later.title"/>
						</stripes:link>
					</li>
					<li>
						<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.HomeActionBean" event="bookInAdvance">
							<fmt:message key="book.advance.title"/>
						</stripes:link>
					</li>
				</ul>
			</nav>
			
			<stripes:form id="geolocation" beanclass="com.criticalsoftware.mobics.presentation.action.book.NearestCarActionBean" method="GET" >
				<stripes:hidden id="latitude" name="latitude"/>
				<stripes:hidden id="longitude" name="longitude"/>
			</stripes:form>
			
			<stripes:link href="#" id="nearestCar" class="greenBtn">
				<fmt:message key="nearest.car.button"/>
			</stripes:link>			
		</section>
	</article>
</t:main>
