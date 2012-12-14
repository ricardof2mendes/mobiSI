<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='home.title' /></c:set>

<t:main title="${title}">
	<c:choose>
		<c:when test="${actionBean.fieldErrors}">
			<stripes:errors/>
		</c:when>
		<c:otherwise>
			<div class="globalErrors">
				<stripes:errors/>
			</div>
		</c:otherwise>
	</c:choose>
	<article>
		<section>
			<nav class="simpleList">
				<ul>
					<li class="bookForNow">
						<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.HomeActionBean" event="bookCarForNow">
							<fmt:message key="book.now.title"/>
						</stripes:link>
					</li>
					<li class="findForLater">
						<stripes:link id="findForLater" beanclass="com.criticalsoftware.mobics.presentation.action.HomeActionBean" event="findCarForLater">
							<fmt:message key="find.later.title"/>
						</stripes:link>
					</li>
					<li class="bookAdvance">
						<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.HomeActionBean" event="bookInAdvance">
							<fmt:message key="book.advance.title"/>
						</stripes:link>
					</li>
				</ul>
			</nav>
			
			<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" 
						  id="nearestCar" name="nearestCar" event="nearestCarBook" class="linkBtn bigBtn green" addSourcePage="true">
				<span id="nearestCar"><fmt:message key="nearest.car.button"/></span>
			</stripes:link>			
		</section>
	</article>
	<jsp:include page="/WEB-INF/common/geolocationErrorAlert.jsp"/>
</t:main>
