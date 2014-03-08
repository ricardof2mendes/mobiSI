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

        <c:choose>
            <c:when test="${actionBean.trip.costCalculationSuccess}">
                <section>
                    <h2><fmt:message key="trip.detail.details"/></h2>
                    <nav class="panel">
                        <ul>
                            <li class="detail white">
                                <span><fmt:message key="trip.detail.total.cost"/></span>
                                <span>
                                    <mobi:formatMobics value="${actionBean.trip.totalCostWithTax}" type="currencySymbol" />
                                    <c:if test="${actionBean.trip.paidWithBonus}"><sup>*</sup></c:if>
                                </span>
                            </li>
                        </ul>
                    </nav>
                    <c:if test="${actionBean.trip.paidWithBonus}">
                        <div class="warningMessage">
                            <fmt:message key="trip.detail.payed.with.bonus"/>
                        </div>
                    </c:if>
                </section>

                <section>
                    <nav class="panel">
                        <ul>
                            <li class="detail white">
                                <span><fmt:message key="trip.detail.trip.cost"/></span>
                                <span>
                                    <mobi:formatMobics value="${actionBean.trip.tripCostWithTax}" type="currencySymbol" />
                                </span>
                            </li>
                        </ul>
                    </nav>
                </section>
            </c:when>
            <c:otherwise>
                <section>
                    <h2><fmt:message key="trip.detail.details"/></h2>
                    <div class="warningMessage">
                        <fmt:message key="trip.detail.cost.calculation.unsuccess"/>
                    </div>
                </section>
            </c:otherwise>
        </c:choose>


        <c:if test="${!empty actionBean.trip.addonCode}">
            <section>
                <nav class="panel">
                    <ul>
                        <li class="detail white">
                            <stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.addons.AddOnsActionBean" event="main" addSourcePage="true" >
                                <stripes:param name="code" value="${actionBean.trip.addonCode}"/>
                                <span><fmt:message key="trip.detail.addon"/></span>
                                <span>${actionBean.trip.addonName}</span>
                            </stripes:link>
                        </li>
                        <li class="detail white">
                            <span><fmt:message key="trip.detail.discount"/></span>
                            <span><mobi:formatMobics value="${actionBean.trip.addOnDiscountGeneratedWithTax}" type="currencySymbol" />&nbsp;(<mobi:formatMobics value="${actionBean.trip.percentageDiscount}" type="percentage" />)</span>
                        </li>
                    </ul>
                </nav>
            </section>
        </c:if>

        <c:if test="${!empty actionBean.trip.promotionCode}">
            <section>
                <nav class="panel">
                    <ul>
                        <li class="detail white">
                            <stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.promotions.PromotionsActionBean" event="detail" addSourcePage="true" >
                                <stripes:param name="code" value="${actionBean.trip.promotionCode}"/>
                                <span><fmt:message key="trip.detail.promotion"/></span>
                                <span>${actionBean.trip.promotionName}</span>
                            </stripes:link>
                        </li>
                        <li class="detail white">
                            <span><fmt:message key="trip.detail.bonus"/></span>
                            <span><mobi:formatMobics value="${actionBean.trip.promotionBonusGeneratedWithTax}" type="currencySymbol" /></span>
                        </li>
                    </ul>
                </nav>
            </section>
        </c:if>

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
                        <c:choose>
                          <c:when test="${actionBean.trip.bookingType == 'IMMEDIATE'}">
                            <c:set var="bookingTypeBean" value="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean"/>
                          </c:when>
                          <c:otherwise>
                            <c:set var="bookingTypeBean" value="com.criticalsoftware.mobics.presentation.action.booking.AdvanceBookingActionBean"/>
                          </c:otherwise>
                        </c:choose>

                        <stripes:link id="carDetails" beanclass="${pageScope.bookingTypeBean}" event="carDetails" addSourcePage="true" >
                          <stripes:param name="licensePlate" value="${actionBean.trip.carLicensePlate}"/>

                          <span><fmt:message key="trip.detail.license.plate"/></span>
                          <span>${actionBean.trip.carLicensePlate}</span>
                        </stripes:link>
					</li>
				</ul>
			</nav>
		</section>
		
		<section>
			<nav class="panel">
				<ul>
					<li class="detail">
						<span>
							<fmt:message key="trip.detail.price.booked.per.minute"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.trip.car.priceBookedPerMinute}" type="currencySymbol" />
						</span>
					</li>
					<li class="detail">
						<span>
							<fmt:message key="trip.detail.cost.per.extra"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.trip.car.costPerExtraKm}" type="currencySymbol" />
						</span>
					</li>
                    <li class="detail">
                        <div class="left">
                            <span>
                                <fmt:message key="trip.detail.max.cost"/>
                            </span>
                        </div>
						<div class="right">
                            <span>
                                <fmt:message key="trip.detail.max.cost.one.hour"/> -
                                <mobi:formatMobics value="${actionBean.trip.car.maxCostPerHour}" type="currencySymbol" /> -
                                <mobi:formatMobics value="${actionBean.trip.car.distanceThreshold}" type="distance" />
                            </span>
							<span>
							    ${actionBean.trip.car.configurableTime}h -
                                <mobi:formatMobics value="${actionBean.trip.car.maxCostPerConfigurableHour}" type="currencySymbol" /> -
                                <mobi:formatMobics value="${actionBean.trip.car.includedDistancePerConfigurableHour}" type="distance" />
                            </span>
							<span>
                                <fmt:message key="trip.detail.max.cost.max.hour"/> -
                                <mobi:formatMobics value="${actionBean.trip.car.maxCostPerDay}" type="currencySymbol" /> -
                                <mobi:formatMobics value="${actionBean.trip.car.includedDistancePerDay}" type="distance" />
                            </span>
						</div>
                        <div class="clear"></div>
                    </li>
				</ul>
			</nav>
		</section>
	</article>
    <div class="warningMessage">
        <fmt:message key="car.details.price.message"/>
        <stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" event="price">
            <strong><fmt:message key="car.details.price.link.message"/></strong>
        </stripes:link>
    </div>
	
</t:main>