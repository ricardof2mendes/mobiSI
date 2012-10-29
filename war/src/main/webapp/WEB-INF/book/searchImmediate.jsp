<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page">
	<fmt:message key='list.search.title' />
</c:set>

<t:main title="${title}">
	<article>
		<section>
			<nav class="orderCriteria">
				<ul>
					<li>
						<stripes:link class="${actionBean.orderBy == 'DISTANCE' ? 'selected' : '' }"
									  beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" event="searchCars">
							<stripes:param name="price" value="${actionBean.price}"/>
							<stripes:param name="distance" value="${actionBean.distance}"/>
							<stripes:param name="clazz" value="${actionBean.clazz}"/>
							<stripes:param name="fuel" value="${actionBean.fuel}"/>
							<stripes:param name="latitude" value="${actionBean.latitude}"/>
							<stripes:param name="longitude" value="${actionBean.longitude}"/>
							<stripes:param name="orderBy" value="DISTANCE"/>
							
							<fmt:message key="book.now.search.distance"/>
						</stripes:link>
					</li>
					<li>
						<stripes:link class="${actionBean.orderBy == 'PRICE' ? 'selected' : '' }"
									  beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" event="searchCars">
							<stripes:param name="price" value="${actionBean.price}"/>
							<stripes:param name="distance" value="${actionBean.distance}"/>
							<stripes:param name="clazz" value="${actionBean.clazz}"/>
							<stripes:param name="fuel" value="${actionBean.fuel}"/>
							<stripes:param name="latitude" value="${actionBean.latitude}"/>
							<stripes:param name="longitude" value="${actionBean.longitude}"/>
							<stripes:param name="orderBy" value="PRICE"/>
							
							<fmt:message key="book.now.search.price"/>
						</stripes:link>
					</li>
					<li>
						<stripes:link class="${actionBean.orderBy == 'CLASS' ? 'selected' : '' }"
									  beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" event="searchCars">
							<stripes:param name="price" value="${actionBean.price}"/>
							<stripes:param name="distance" value="${actionBean.distance}"/>
							<stripes:param name="clazz" value="${actionBean.clazz}"/>
							<stripes:param name="fuel" value="${actionBean.fuel}"/>
							<stripes:param name="latitude" value="${actionBean.latitude}"/>
							<stripes:param name="longitude" value="${actionBean.longitude}"/>
							<stripes:param name="orderBy" value="CLASS"/>
							
							<fmt:message key="book.now.search.class"/>
						</stripes:link>
					</li>
				</ul>
			</nav>
		</section>
		<div class="clear"></div>
		<c:import url="./carListImmediate.jsp"></c:import>
	</article>
</t:main>