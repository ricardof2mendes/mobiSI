<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='current.trip.title' /></c:set>
<c:set var="warnTitle" scope="page">
	<c:choose>
		<c:when test="${actionBean.trip.undesirableZoneCost > 0}">
			<fmt:message key="current.trip.end.trip.confirm.h3">
				<fmt:param>${actionBean.trip.undesirableZoneCost}</fmt:param>
			</fmt:message>
		</c:when>
		<c:otherwise>
			<fmt:message key="current.trip.end.trip.confirm.h3.no.cost"/>
		</c:otherwise>
	</c:choose>
</c:set>

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
			<section id="stateError" class="hidden">
				<h2><fmt:message key="current.trip.booking.cancelled"/></h2>
				<h3 id="title1"><fmt:message key="current.trip.booking.cancelled.text"/></h3>
				<a id="closeBookingImmediate" href="#" class="alertBtn gray" >
					<fmt:message key="current.trip.extend.ok"/>
				</a>
			</section>
			<section id="unwantedZoneError" class="hidden">
				<h2><fmt:message key="current.trip.end.trip.confirm"/></h2>
				<h3 id="title1">${pageScope.warnTitle}</h3>
				<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="linkBtn orangered" event="endTrip" addSourcePage="true">
					<fmt:message key="current.trip.button.end.trip"/>
				</stripes:link>
				<stripes:link id="closeConfirm" href="#" class="alertBtn gray" >
					<fmt:message key="current.trip.extend.ok"/>
				</stripes:link>
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
		<c:if test="${actionBean.current.carState == 'BOOKED'}">	
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
		
		<section>
			<!-- Edit current trip (advance booking) -->
			<c:if test="${actionBean.current.bookingType == 'ADVANCE'}">		
				<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="linkBtn gray" addSourcePage="true" event="extend">
					<fmt:message key="current.trip.button.edit"/>
				</stripes:link>
			</c:if>
			
			<!-- Lock car -->
			<c:if test="${actionBean.current.carState == 'IN_USE'}">		
				<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="linkBtn gray" event="lockCar" addSourcePage="true">
					<fmt:message key="current.trip.button.lock.car"/>
				</stripes:link>
				<div class="warningMessage">
					<fmt:message key="current.trip.lock.message"/>
				</div>
			</c:if>
			
			<!-- End trip -->
			<c:if test="${actionBean.current.state == 'IN_USE'}">		
				<stripes:link id="endTrip" beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="linkBtn orangered" event="endTrip" addSourcePage="true">
					<fmt:message key="current.trip.button.end.trip"/>
				</stripes:link>
				<div class="warningMessage">
					<fmt:message key="current.trip.end.message"/>
				</div>
			</c:if>
		</section>
	</article>
		
</t:main>