<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page">
	<fmt:message key='book.now.search.results' />
</c:set>

<t:map title="${title}">
	
	<jsp:include page="/WEB-INF/common/legend.jsp"></jsp:include>
	
	<span id="paramPrice" class="hidden">${param.price}</span>
	<span id="paramDist" class="hidden">${param.distance}</span>
	<span id="paramClazz" class="hidden">${param.clazz}</span>
	<span id="paramFuel" class="hidden">${param.fuel}</span>
	<span id="paramOrder" class="hidden">${param.orderBy}</span>
	<span id="paramLat" class="hidden">${param.latitude}</span>
	<span id="paramLong" class="hidden">${param.longitude}</span>
	<article>
		<section>
			<div id="container" class="white">
				<div id="map"><!-- Map will be included here--></div>
			</div>
		</section>
	</article>
	<article>
		<section id="whiteBar">
			<nav class="threeColumnList hidden">
				<ul>
					<li class="noBorder">
						<stripes:link href="${contextPath}/booking/ImmediateBooking.action?licensePlateBook=">
						<stripes:param name="latitude">${param.latitude}</stripes:param>
						<stripes:param name="longitude">${param.longitude}</stripes:param>
							<div>
								<img class="carImage" src="" />
							</div>
							<div>
								<div>
								
									<span id="licensePlate"></span>
									<span id="carBrandName"></span>
									<span id="fuelType"></span>
								</div>
								<div>
									<span id="priceInUse"></span>
									<span id="distance"></span>
                                    <span>
                                        <span id="add"></span>
                                        <span id="promo"></span>
                                    </span>
								</div>
							</div>
						</stripes:link>
					</li>
				</ul>
			</nav>
		</section>
	</article>
	
	<!-- Modal window for no results -->
	<div class="confirm2">
		<article>
			<section id="noResults" class="hidden">
				<h2><fmt:message key="book.now.no.results.found.header"/></h2>
				<h3 id="title1"><fmt:message key="book.now.no.results.found"/></h3>
				<h3 id="title1"><fmt:message key="book.now.no.results.msg1"/></h3>
				<stripes:form beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" method="post">
					<stripes:hidden id="negatedTripLatitude" name="negatedTripLatitude" value="${param.latitude}"/>
					<stripes:hidden id="negatedTripLongitude" name="negatedTripLongitude" value="${param.longitude}"/>
					<stripes:submit name="createNegatedTrip" class="submitBtn orange">
						<fmt:message key="book.now.no.results.report.negated.trip"/>						
					</stripes:submit>	
				</stripes:form>
				<stripes:link id="closeConfirm" href="#" class="alertBtn gray" >
					<fmt:message key="book.now.no.results.cancel"/>
				</stripes:link>
			</section>
		</article>
	</div>
</t:map>

