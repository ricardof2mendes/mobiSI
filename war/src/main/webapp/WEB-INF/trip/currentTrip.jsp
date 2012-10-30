<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='current.trip.title' /></c:set>

<t:main title="${title}">
	<stripes:messages/>
	
	<article>
		<c:if test="${actionBean.current.state == 'IN_USE'}">
			<section>
				<h2><fmt:message key="current.trip.booked.car"/></h2>
				<nav class="panel">
					<ul>
						<li class="link">
							<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" event="carDetails">
								<stripes:param name="licensePlate" value="${actionBean.current.licensePlate}"/>
								<stripes:param name="latitude" value="${actionBean.current.latitude}"/>
								<stripes:param name="longitude" value="${actionBean.current.longitude}"/>
		
								<span><fmt:message key="current.trip.car"/></span>
								<span>
									${actionBean.current.licensePlate}&nbsp;(${actionBean.current.carBrand}&nbsp;${actionBean.current.carModel})
								</span>
							</stripes:link>
						</li>
						<li class="link">
							<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" event="carLocation">
								<stripes:param name="licensePlate">${actionBean.current.licensePlate}</stripes:param>
								<span><fmt:message key="current.trip.location"/></span>
								<span>
									${actionBean.location}
								</span>
							</stripes:link>
						</li>
					</ul>
				</nav>
			</section>
			
			<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="linkBtn gray" event="unlockCar" addSourcePage="true">
				<fmt:message key="current.trip.button.unlock.car"/>
			</stripes:link>
			
			<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="linkBtn gray" event="signal" addSourcePage="true">
				<fmt:message key="current.trip.button.signal.position"/>
			</stripes:link>
		</c:if>			
		
		<section>
			<h2><fmt:message key="current.trip.details"/></h2>
			<nav class="panel">
				<ul>
					<li class="detail">
						<span><fmt:message key="current.trip.cost"/></span>
						<span>
							<mobi:formatMobics value="${actionBean.current.currentCost}" type="currency" 
											   pattern="${applicationScope.configuration.currencyPattern}"/>
						</span>
					</li>
				</ul>
			</nav>
		</section>
		
		<section>
			<nav class="panel">
				<ul>
					<li class="detail">
						<span>
							<fmt:message key="current.trip.price.use"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.current.basePrice}" type="currency" 
											   pattern="${applicationScope.configuration.currencyPattern}"/>
						</span>
					</li>
					<li class="detail">
						<span>
							<fmt:message key="current.trip.price.locked"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.current.lockedPrice}" type="currency" 
											   pattern="${applicationScope.configuration.currencyPattern}"/>
						</span>
					</li>
					<li class="detail">
						<span>
							<fmt:message key="current.trip.duration"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.current.bookingDuration}" type="time" pattern="0"/>
						</span>
					</li>
					
					<c:choose>
						<c:when test="${actionBean.current.state == 'IN_USE'}">
							<li class="detail">
								<span>
									<fmt:message key="current.trip.distance"/>
								</span>
								<span>
									<mobi:formatMobics value="${actionBean.current.currentDistance}" type="distance" 
											   pattern="${applicationScope.configuration.meterPattern}"
											   pattern2="${applicationScope.configuration.kilometerPattern}"/>
								</span>
							</li>
						</c:when>
						<c:otherwise>
							<li class="link">
								<span>
									<fmt:message key="current.trip.current.zone"/>
								</span>
								<span>
									<fmt:message key="zone.${actionBean.current.currentZoneType}"/>
								</span>
							</li>
						</c:otherwise>
					</c:choose>
				</ul>
			</nav>
		</section>
				
		<c:if test="${actionBean.current.state == 'ON_TRIP'}">		
			<stripes:link id="showPin" beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="linkBtn gray" event="lockCar" addSourcePage="true">
				<fmt:message key="current.trip.button.lock.car"/>
			</stripes:link>
			<div class="warningMessage">
				<fmt:message key="current.trip.lock.message"/>
			</div>
		</c:if>
		
		<stripes:link id="showPin" beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="linkBtn red" event="endTrip" addSourcePage="true">
			<fmt:message key="current.trip.button.end.trip"/>
		</stripes:link>
		<div class="warningMessage">
			<fmt:message key="current.trip.end.message"/>
		</div>
	</article>
	
</t:main>