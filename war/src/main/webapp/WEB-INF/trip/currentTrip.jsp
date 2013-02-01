<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='current.trip.title' /></c:set>
<c:set var="warnTitle" scope="page">
	<c:choose>
		<c:when test="${actionBean.current.undesirableZoneCost > 0}">
			<fmt:message key="current.trip.end.trip.confirm.h3">
				<fmt:param>${actionBean.current.undesirableZoneCost}</fmt:param>
			</fmt:message>
		</c:when>
		<c:otherwise>
			<fmt:message key="current.trip.end.trip.confirm.h3.no.cost"/>
		</c:otherwise>
	</c:choose>
</c:set>

<t:main title="${title}">
	
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>

	<!-- State variable -->
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
							<li class="detail white">
								<span>
									<fmt:message key="car.details.start.date"/>
								</span>
								<span>
									<fmt:formatDate value="${actionBean.current.startDate.time}" pattern="${applicationScope.configuration.dateTimePattern}"/>
								</span>
							</li>
							<li class="detail white">
								<span>
									<fmt:message key="car.details.end.date"/>
								</span>
								<span>
									<fmt:formatDate value="${actionBean.current.endDate.time}" pattern="${applicationScope.configuration.dateTimePattern}"/>
								</span>
							</li>
						</c:otherwise>
					</c:choose>
				</ul>
			</nav>
		</section>
		
		<!-- Unlock car -->
		<c:if test="${actionBean.current.carState == 'BOOKED' && actionBean.current.state != 'WAIT_OBS_IMMEDIATE' && actionBean.current.state != 'OBS_ERROR'}">	
			<section>
				<stripes:link id="unlock" beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="linkBtn gray" event="unlockCar" addSourcePage="true">
					<stripes:param name="licensePlate">${actionBean.current.licensePlate}</stripes:param>
					<fmt:message key="current.trip.button.unlock.car"/>
				</stripes:link>
			</section>
		</c:if>
		
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
							<fmt:message key="current.trip.price.included"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.current.distanceThreshold}" type="currencyHour" />
						</span>
					</li>
					<li class="detail white">
						<span>
							<fmt:message key="current.trip.price.extra"/>
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
						<c:when test="${actionBean.current.carState == 'BOOKED'}">
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

		<c:if test="${actionBean.current.state != 'WAIT_OBS_IMMEDIATE'}">	
			<section>
				<!-- Edit current trip (advance booking) -->
				<c:if test="${actionBean.current.bookingType == 'ADVANCE'}">		
					<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="linkBtn gray" addSourcePage="true" event="extend">
						<fmt:message key="current.trip.button.edit"/>
					</stripes:link>
				</c:if>
				
				<!-- Lock car End trip with js unwanted zone validation-->
				<c:if test="${actionBean.current.carState == 'IN_USE'}">		
					<stripes:link id="endTrip" beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="linkBtn orangered" event="lockEndTrip" addSourcePage="true">
						<stripes:param name="licensePlate">${actionBean.current.licensePlate}</stripes:param>
						<fmt:message key="current.trip.button.lock.car.end.trip"/>
					</stripes:link>
					<div class="warningMessage">
						<fmt:message key="current.trip.end.message"/>
					</div>
				</c:if>
			</section>
		</c:if>
	</article>
	
	<!-- Zone variable for js -->
	<span id="zone" class="hidden">${actionBean.current.currentZoneType}</span>
	
	<!-- Modal window for confirmation -->
	<div class="confirm2">
		<article>
			<section id="stateError" class="hidden">
				<h2><fmt:message key="current.trip.booking.cancelled"/></h2>
				<h3 id="title1"><fmt:message key="current.trip.booking.cancelled.text"/></h3>
				<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" event="endTrip" class="alertBtn gray" >
					<fmt:message key="current.trip.extend.ok"/>
				</stripes:link>
			</section>
			<section id="unwantedZoneError" class="hidden">
				<h2><fmt:message key="current.trip.end.trip.confirm.h2"/></h2>
				<h3 id="title1">${pageScope.warnTitle}</h3>
				<stripes:link id="lockEndTrip" beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="alertBtn orangered" event="lockEndTrip" addSourcePage="true">
					<stripes:param name="licensePlate">${actionBean.current.licensePlate}</stripes:param>
					<fmt:message key="current.trip.button.lock.car.end.trip"/>
				</stripes:link>
				<stripes:link id="closeConfirm" href="#" class="alertBtn gray" >
					<fmt:message key="current.trip.extend.cancel"/>
				</stripes:link>
			</section>
			<section id="unlocking" class="hidden">
				<h2><fmt:message key="current.trip.unlocking.car"/></h2>
				<h3><fmt:message key="current.trip.validating.seconds"/></h3>
			</section>
			<section id="locking" class="hidden">
				<h2><fmt:message key="current.trip.locking.car"/></h2>
				<h3><fmt:message key="current.trip.validating.seconds"/></h3>
			</section>
		</article>
	</div>
		
</t:main>