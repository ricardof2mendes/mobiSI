<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='current.trip.title' /></c:set>

<t:main title="${title}">
	
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
	
	<span id="state" class="hidden">${actionBean.current.state}</span>
		
	<article id="statePooling" class="${actionBean.current.state != 'WAIT_OBS_IMMEDIATE' ? 'hidden' : ''} currentTrip">
		<section>
			<h2>
				<fmt:message key="current.trip.validating"/>
			</h2>
			<div>
				<fmt:message key="current.trip.validating.seconds"/>
			</div>
		</section>
	</article>
	
	<!-- Modal window for confirmation -->
	<div class="confirm2">
		<article>
			<section>
				<h2><fmt:message key="current.trip.booking.cancelled"/></h2>
				<h3 id="title1"><fmt:message key="current.trip.booking.cancelled.text"/></h3>
				<a id="closeBookingImmediate" href="#" class="alertBtn gray" >
					<fmt:message key="geolocation.alert.button.ok"/>
				</a>
			</section>
		</article>
	</div>
	
	<article>
		<section>
			<h2><fmt:message key="current.trip.booked.car"/></h2>
			<nav class="panel">
				<ul>
					<c:choose>
						<c:when test="${actionBean.current.bookingType == 'IMMEDIATE'}">
							<li class="link white">
								<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" event="carDetails" addSourcePage="true" >
									<stripes:param name="licensePlate" value="${actionBean.current.licensePlate}"/>
			
									<span><fmt:message key="current.trip.car"/></span>
									<span>
										${actionBean.current.licensePlate}&nbsp;(${actionBean.current.carBrand}&nbsp;${actionBean.current.carModel})
									</span>
								</stripes:link>
							</li>
							<li class="link white">
								<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" event="carLocation" addSourcePage="true" >
									<stripes:param name="licensePlate">${actionBean.current.licensePlate}</stripes:param>
									<span><fmt:message key="current.trip.location"/></span>
									<span>
										${actionBean.location}
									</span>
								</stripes:link>
							</li>
						</c:when>
						<c:otherwise>
							<li class="link white">
								<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.AdvanceBookingActionBean" event="carDetails">
									<stripes:param name="licensePlate" value="${actionBean.current.licensePlate}"/>
			
									<span><fmt:message key="current.trip.car"/></span>
									<span>
										${actionBean.current.licensePlate}&nbsp;(${actionBean.current.carBrand}&nbsp;${actionBean.current.carModel})
									</span>
								</stripes:link>
							</li>
							<li class="link white">
								<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.AdvanceBookingActionBean" event="parkLocation" addSourcePage="true">
									<stripes:param name="licensePlate">${actionBean.current.licensePlate}</stripes:param>
									<span><fmt:message key="current.trip.location"/></span>
									<span>
										${actionBean.location}
									</span>
								</stripes:link>
							</li>
						</c:otherwise>
					</c:choose>
				</ul>
			</nav>
		</section>
		
		<c:if test="${actionBean.current.state == 'IN_USE'}">	
			<section>
				<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="linkBtn gray" event="unlockCar" addSourcePage="true">
					<stripes:param name="licensePlate">${actionBean.current.licensePlate}</stripes:param>
					<fmt:message key="current.trip.button.unlock.car"/>
				</stripes:link>
			</section>
		</c:if>			
		
		<section>
			<h2><fmt:message key="current.trip.details"/></h2>
			<nav class="panel">
				<ul>
					<li class="detail white">
						<span><fmt:message key="current.trip.cost"/></span>
						<span>
							<mobi:formatMobics value="${actionBean.current.currentCost}" type="currencySymbol" />
						</span>
					</li>
				</ul>
			</nav>
		</section>
		
		<section>
			<nav class="panel">
				<ul>
					<li class="detail white">
						<span>
							<fmt:message key="current.trip.price.use"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.current.basePrice}" type="currencyHour" />
						</span>
					</li>
					<li class="detail white">
						<span>
							<fmt:message key="current.trip.price.locked"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.current.lockedPrice}" type="currencyHour" />
						</span>
					</li>
					<li class="detail white">
						<span>
							<fmt:message key="current.trip.duration"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.current.bookingDuration}" type="time" />
						</span>
					</li>
					
					<c:choose>
						<c:when test="${actionBean.current.state == 'IN_USE'}">
							<li class="detail white">
								<span>
									<fmt:message key="current.trip.distance"/>
								</span>
								<span>
									<mobi:formatMobics value="${actionBean.current.currentDistance}" type="distance" />
								</span>
							</li>
						</c:when>
						<c:otherwise>
							<li class="detail white">
								<span>
									<fmt:message key="current.trip.current.zone"/>
								</span>
								<span>
									<fmt:message key="ZoneType.${actionBean.current.currentZoneType}"/>
								</span>
							</li>
						</c:otherwise>
					</c:choose>
				</ul>
			</nav>
		</section>
		
		
		
		<c:if test="${actionBean.current.state == 'IN_USE' && actionBean.current.bookingType == 'ADVANCED'}">		
			<stripes:link id="showPin" beanclass="com.criticalsoftware.mobics.presentation.action.booking.EditAvanceBookingActionBean" class="linkBtn gray" addSourcePage="true">
				<stripes:param name="activityCode" value="${actionBean.current.bookingCode}"/>
				<fmt:message key="current.trip.button.edit"/>
			</stripes:link>
		</c:if>
				
		<c:if test="${actionBean.current.state == 'ON_TRIP'}">		
			<stripes:link id="showPin" beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="linkBtn gray" event="lockCar" addSourcePage="true">
				<fmt:message key="current.trip.button.lock.car"/>
			</stripes:link>
			<div class="warningMessage">
				<fmt:message key="current.trip.lock.message"/>
			</div>
		</c:if>
		
		<c:if test="${actionBean.current.state == 'IN_USE'}">		
			<stripes:link id="showPin" beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="linkBtn orangered" event="endTrip" addSourcePage="true">
				<fmt:message key="current.trip.button.end.trip"/>
			</stripes:link>
			<div class="warningMessage">
				<fmt:message key="current.trip.end.message"/>
			</div>
		</c:if>
	</article>
		
</t:main>