<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='nearest.car.title' /></c:set>

<t:main title="${title}" hideFooter="true">

	<article>
		<section>
			<nav>
			${actionBean.car.licensePlate}
			<br/>
			${actionBean.car.carBrandName}&nbsp;${actionBean.car.carModelName}
			<br/>
			${actionBean.car.fuelType}&nbsp;(${actionBean.car.range})<br/>
			<br/>
			<fmt:message key="car.details.distance"/> ${actionBean.car.distance},&nbsp;TODO
			<br/>
			
			<fmt:message key="car.details.location"/>
			<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.HomeActionBean" event="bookInAdvance">
							${actionBean.location}
						</stripes:link>
						<br/>
			<fmt:message key="car.details.price.use"/> <fmt:message key="car.details.price.hour"><fmt:param value="${actionBean.car.priceInUse}"/></fmt:message>
			
			<br/>
			<fmt:message key="car.details.price.locked"/> <fmt:message key="car.details.price.hour"><fmt:param value="${actionBean.car.priceReserved}"/></fmt:message>
			</nav>
		</section>
	</article>

	
	<fmt:message key="nearest.car.book.message"/>
</t:main>