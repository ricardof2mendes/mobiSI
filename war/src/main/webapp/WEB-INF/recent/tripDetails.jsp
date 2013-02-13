<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='trip.detail.title' /></c:set>

<t:main title="${title}">
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
	
	<article>
	
		<section>
			<h2><fmt:message key="trip.detail.booking.info"/></h2>
			<nav class="panel">
				<ul>
					<li class="detail white">
						<span><fmt:message key="trip.detail.booking.type"/></span>
						<span>
							<fmt:message key="BookingType.${actionBean.trip.bookingType}"/>
						</span>
					</li>
					<li class="detail white">
						<span><fmt:message key="trip.detail.booking.number"/></span>
						<span>
							${actionBean.trip.bookingNumber}
						</span>
					</li>
				</ul>
			</nav>
		</section>
		
		<section>
			<h2><fmt:message key="trip.detail.details"/></h2>
			<nav class="panel">
				<ul>
					<li class="detail white">
						<span><fmt:message key="trip.detail.total.cost"/></span>
						<span>
							<mobi:formatMobics value="${actionBean.trip.cost}" type="currencySymbol" />
						</span>
					</li>
				</ul>
			</nav>
		</section>
		
		<section>
			<nav class="panel">
				<ul>
					<li class="detail white">
						<span><fmt:message key="trip.detail.start.date"/></span>
						<span>
							<fmt:formatDate value="${actionBean.trip.startDate.time}" 
						    				pattern="${applicationScope.configuration.dateTimePattern}"/>
						</span>
					</li>
					<li class="detail white">
						<span><fmt:message key="trip.detail.end.date"/></span>
						<span>
							<fmt:formatDate value="${actionBean.trip.endDate.time}" 
											pattern="${applicationScope.configuration.dateTimePattern}"/>
						</span>
					</li>
					<li class="detail white">
						<span><fmt:message key="trip.detail.time.in.use"/></span>
						<span>
							<mobi:formatMobics value="${actionBean.trip.timeInUse}" type="time"/>
						</span>
					</li>
					<li class="detail white">
						<span><fmt:message key="trip.detail.time.locked"/></span>
						<span>
							<mobi:formatMobics value="${actionBean.trip.timeBooked}" type="time"/>
						</span>
					</li>
				</ul>
			</nav>
		</section>
		
		<section>
			<nav class="panel">
				<ul>
					<li class="detail white">
						<span><fmt:message key="trip.detail.start.location"/></span>
						<span>
							<c:choose>
								<c:when test="${actionBean.trip.bookingType == 'IMMEDIATE'}">
									${actionBean.startLocation}	
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${actionBean.trip.startPark != null}">
											${actionBean.trip.startPark}
										</c:when>
										<c:otherwise>
											<fmt:message key="application.value.not.available"/>
										</c:otherwise>
									</c:choose> 
								</c:otherwise>
							</c:choose>
						</span>
					</li>
					<li class="detail white">
						<span><fmt:message key="trip.detail.end.location"/></span>
						<span>
							<c:choose>
								<c:when test="${actionBean.trip.bookingType == 'IMMEDIATE'}">
									${actionBean.endLocation}	
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${actionBean.trip.endPark != null}">
											${actionBean.trip.endPark}
										</c:when>
										<c:otherwise>
											<fmt:message key="application.value.not.available"/>
										</c:otherwise>
									</c:choose> 
								</c:otherwise>
							</c:choose>
						</span>
					</li>
					<li class="detail white">
						<span><fmt:message key="trip.detail.distance"/></span>
						<span>
							<mobi:formatMobics value="${actionBean.trip.distance}" type="distance" />
						</span>
					</li>
				</ul>
			</nav>
		</section>
		
		<section>
			<h2><fmt:message key="trip.detail.car.details"/></h2>
			<nav class="panel">
				<ul>
					<li class="link white">
						<stripes:link id="carDetails" beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" event="carDetails" addSourcePage="true" >
							<stripes:param name="licensePlate" value="${actionBean.trip.carLicensePlate}"/>
	
							<span><fmt:message key="trip.detail.license.plate"/></span>
							<span>
								${actionBean.trip.carLicensePlate}
							</span>
						</stripes:link>
					</li>
				</ul>
			</nav>
		</section>
		
		<section>
			<nav class="panel">
				<ul>
					<li class="detail white">
						<span>
							<fmt:message key="trip.detail.price.in.use"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.trip.car.priceInUse}" type="currencyHour" />
						</span>
					</li>
					<li class="detail white">
						<span>
							<fmt:message key="trip.detail.price.locked"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.trip.car.priceReserved}" type="currencyHour" />
						</span>
					</li>
					<li class="detail white">
						<span>
							<fmt:message key="trip.detail.included.distance"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.trip.car.distanceThreshold}" type="distance" />
						</span>
					</li>
					<li class="detail white">
						<span>
							<fmt:message key="trip.detail.cost.per.extra"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.trip.car.costPerExtraKm}" type="currencySymbol" />
						</span>
					</li>
				</ul>
			</nav>
		</section>
	</article>
	
</t:main>