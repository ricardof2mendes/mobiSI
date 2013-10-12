<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='trip.detail.last.trip.title' /></c:set>

<t:main title="${title}">
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
	
	<article>
		
		<c:choose>
			<c:when test="${actionBean.last != null}">
				<section>
					<h2><fmt:message key="trip.detail.booking.info"/></h2>
					<nav class="panel">
						<ul>
							<li class="detail white">
								<span><fmt:message key="trip.detail.booking.type"/></span>
								<span>
									<fmt:message key="BookingType.${actionBean.last.bookingType}"/>
								</span>
							</li>
							<li class="detail white">
								<span><fmt:message key="trip.detail.booking.number"/></span>
								<span>
									${actionBean.last.bookingNumber}
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
									<mobi:formatMobics value="${actionBean.last.totalCost}" type="currencySymbol" />
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
									<fmt:formatDate value="${actionBean.last.startDate.time}" 
								    				pattern="${applicationScope.configuration.dateTimePattern}"/>
								</span>
							</li>
							<li class="detail white">
								<span><fmt:message key="trip.detail.end.date"/></span>
								<span>
									<fmt:formatDate value="${actionBean.last.endDate.time}" 
													pattern="${applicationScope.configuration.dateTimePattern}"/>
								</span>
							</li>
							<li class="detail white">
								<span><fmt:message key="trip.detail.time.in.use"/></span>
								<span>
									<mobi:formatMobics value="${actionBean.last.timeInUse}" type="time"/>
								</span>
							</li>
							<li class="detail white">
								<span><fmt:message key="trip.detail.time.locked"/></span>
								<span>
									<mobi:formatMobics value="${actionBean.last.timeBooked}" type="time"/>
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
										<c:when test="${actionBean.last.bookingType == 'IMMEDIATE'}">
											${actionBean.startLocation}	
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${actionBean.last.startPark != null}">
													${actionBean.last.startPark}
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
										<c:when test="${actionBean.last.bookingType == 'IMMEDIATE'}">
											${actionBean.endLocation}	
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${actionBean.last.endPark != null}">
													${actionBean.last.endPark}
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
									<mobi:formatMobics value="${actionBean.last.distance}" type="distance" />
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
									<stripes:param name="licensePlate" value="${actionBean.last.carLicensePlate}"/>
												
									<span><fmt:message key="trip.detail.license.plate"/></span>
									<span>
										${actionBean.last.carLicensePlate}
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
									<mobi:formatMobics value="${actionBean.last.car.priceInUse}" type="currencyHour" />
								</span>
							</li>
							<li class="detail white">
								<span>
									<fmt:message key="trip.detail.price.locked"/>
								</span>
								<span>
									<mobi:formatMobics value="${actionBean.last.car.priceReserved}" type="currencyHour" />
								</span>
							</li>
							<li class="detail white">
								<span>
									<fmt:message key="trip.detail.included.distance"/>
								</span>
								<span>
									<mobi:formatMobics value="${actionBean.last.car.distanceThreshold}" type="distance" />
								</span>
							</li>
							<li class="detail white">
								<span>
									<fmt:message key="trip.detail.cost.per.extra"/>
								</span>
								<span>
									<mobi:formatMobics value="${actionBean.last.car.costPerExtraKm}" type="currencySymbol" />
								</span>
							</li>
							
						</ul>
					</nav>
				</section>
			</c:when>
			<c:otherwise>
				<section id="noresults">
					<label><fmt:message key="recent.history.no.activities.found"/></label>
				</section>
			</c:otherwise>
		</c:choose>
		
	</article>
	
</t:main>