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
                    <h2><fmt:message key="trip.detail.cost"/></h2>
                    <c:choose>
                        <c:when test="${actionBean.last.costCalculationSuccess}">
                                <nav class="panel">
                                    <ul>
                                        <li class="detail white">
                                            <span><fmt:message key="trip.detail.total.cost"/></span>
                                    <span>
                                        <mobi:formatMobics value="${actionBean.last.totalCostWithTax}" type="currencySymbol" />
                                        <c:if test="${actionBean.last.paidWithBonus}"><sup>*</sup></c:if>
                                    </span>

                                        </li>
                                        <li class="detail white">
                                            <span><fmt:message key="trip.detail.trip.cost"/></span>
                                    <span>
                                        <mobi:formatMobics value="${actionBean.last.tripCostWithTax}" type="currencySymbol" />
                                    </span>
                                        </li>
                                    </ul>
                                </nav>
                                <c:if test="${actionBean.last.paidWithBonus}">
                                    <div class="warningMessage noMarginBottom">
                                        <fmt:message key="trip.detail.payed.with.bonus"/>
                                    </div>
                                </c:if>
                        </c:when>
                        <c:otherwise>
                            <div class="warningMessage">
                                <fmt:message key="trip.detail.cost.calculation.unsuccess"/>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </section>

				<section>
                    <h2><fmt:message key="trip.detail.details"/></h2>
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
								<span><fmt:message key="trip.detail.duration"/></span>
								<span>
									<mobi:formatMobics value="${actionBean.last.duration}" type="time"/>
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
									<mobi:formatMobics value="${actionBean.last.distance}" type="distanceDecimal" />
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
							<fmt:message key="trip.detail.price.booked.per.minute"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.last.car.priceBookedPerMinute}" type="currencySymbol" />
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
                            <li class="detail white">
                                <jsp:include page="/WEB-INF/common/maxValuesTable.jsp">
								  <jsp:param name="maxCostPerHour" value="${actionBean.last.car.maxCostPerHour}" />
								  <jsp:param name="distanceThreshold" value="${actionBean.last.car.distanceThreshold}" />
								  <jsp:param name="configurableTime" value="${actionBean.last.car.configurableTime}" />
								  <jsp:param name="maxCostPerConfigurableHour" value="${actionBean.last.car.maxCostPerConfigurableHour}" />
								  <jsp:param name="includedDistancePerConfigurableHour" value="${actionBean.last.car.includedDistancePerConfigurableHour}" />
								  <jsp:param name="maxCostPerDay" value="${actionBean.last.car.maxCostPerDay}" />
								  <jsp:param name="includedDistancePerDay" value="${actionBean.last.car.includedDistancePerDay}" />						  
								</jsp:include>
                                <div class="clear"></div>
                            </li>
						</ul>
					</nav>
				</section>
				
						
				<!-- botoes -->
				<c:if test="${actionBean.last.canOpenCar}">
					<stripes:link id="unlock" beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="linkBtn green" event="unlockCar" addSourcePage="true">
						<stripes:param name="licensePlate">${actionBean.last.car.licensePlate}</stripes:param>
						<fmt:message key="current.trip.button.unlock.car"/>
					</stripes:link>
					<stripes:link id="lockEndTrip" beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="linkBtn orangered" event="lockEndTrip" addSourcePage="true">
						<stripes:param name="licensePlate">${actionBean.last.car.licensePlate}</stripes:param>
						<fmt:message key="current.trip.button.lock.car"/>
					</stripes:link>
				</c:if>
						

                <div class="warningMessage">
                    <fmt:message key="car.details.price.message"/>
                    <stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" event="basePrice">
                        <strong><fmt:message key="car.details.price.link.message"/></strong>
                    </stripes:link>
                </div>
			</c:when>
			<c:otherwise>
				<section id="noresults">
					<label><fmt:message key="recent.history.no.activities.found"/></label>
				</section>
			</c:otherwise>
		</c:choose>	
	</article>
	
	
	<jsp:include page="/WEB-INF/common/tripModals.jsp"/>
</t:main>