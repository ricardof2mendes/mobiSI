<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='trip.detail.advance.trip.title' /></c:set>

<t:main title="${title}">
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
	
	<!-- State pooling -->	
	<span id="state" class="hidden">${actionBean.trip.state}</span>
	<span id="activityCode" class="hidden">${actionBean.trip.bookingNumber}</span>
	<span id="extended" class="hidden">${actionBean.extended}</span>

	<article id="statePooling" class="${actionBean.trip.state != 'WAIT_OBS_ADVANCE' ? 'hidden' : ''}">
		<section>
			<h2>
				<fmt:message key="current.trip.validating"/>&nbsp;&nbsp;&nbsp;<img src="${contextPath}/img/indicator.gif"/>
			</h2>
			<div>
				<fmt:message key="current.trip.validating.seconds">
					<fmt:param>
						<mobi:formatMobics type="milliseconds" value="${applicationScope.configuration.statePollingTimeoutMilliseconds}"/>
					</fmt:param>
				</fmt:message>
			</div>
		</section>
	</article>
	
	<!-- Modal window for error on status-->
	<div class="confirm2">
		<article>
			<section>
				<h2><fmt:message key="current.trip.booking.cancelled"/></h2>
				<h3 id="title1"><fmt:message key="current.trip.booking.cancelled.text"/></h3>
				<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.recent.RecentActivitiesActionBean" event="cancelAdvanceBooking" class="alertBtn gray">
					<stripes:param name="activityCode">${actionBean.trip.bookingNumber}</stripes:param>
					<fmt:message key="geolocation.alert.button.ok"/>
				</stripes:link>
			</section>
		</article>
	</div>
	
	<article>
		<section>
			<nav class="panel">
				<ul>
					<li class="image">
						<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.AdvanceBookingActionBean" event="carDetails" addSourcePage="true">
							<stripes:param name="licensePlate" value="${actionBean.trip.car.licensePlate}"/>
							<div>
								<img class="carImage" src="${contextPath}/booking/AdvanceBooking.action?getCarImage=&licensePlate=${actionBean.trip.car.licensePlate}&width=58&height=58" />
							</div>
							<div>
								<div>
									<span>${actionBean.trip.car.licensePlate} </span>
									<span>${actionBean.trip.car.carBrandName}&nbsp;${actionBean.trip.car.carModelName}</span>
									<span><fmt:message key="FuelType.${actionBean.trip.car.fuelType.name}"/>&nbsp;
										<c:choose>
											<c:when test="${actionBean.trip.car.fuelLevel != null}">(${actionBean.trip.car.fuelLevel}%)</c:when>
											<c:otherwise>(<fmt:message key="application.value.not.available"/>)</c:otherwise>
										</c:choose>
									</span>
								</div>
							</div>
						</stripes:link>
					</li>
					<!-- you should allways see the park, not the car -->
					<c:choose>
						<c:when test="${actionBean.trip.car.zones != null}">
							<li class="link">
								<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.AdvanceBookingActionBean" event="parkLocation" addSourcePage="true">
									<stripes:param name="licensePlate">${actionBean.trip.car.licensePlate}</stripes:param>
									<span>
										<fmt:message key="car.details.park"/>
									</span>
									<span>
										${actionBean.trip.car.zones[0].zone} 
									</span>	
								</stripes:link>
							</li>
						</c:when>
						<c:otherwise>
							<li class="detail">
								<span>
									<fmt:message key="car.details.park"/>
								</span>
								<span>
									<fmt:message key="application.value.not.available"/>
								</span>	
							</li>	
						</c:otherwise>
					</c:choose>
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
                        <jsp:include page="/WEB-INF/common/maxValuesTable.jsp">
						  <jsp:param name="maxCostPerHour" value="${actionBean.trip.car.maxCostPerHour}" />
						  <jsp:param name="distanceThreshold" value="${actionBean.trip.car.distanceThreshold}" />
						  <jsp:param name="configurableTime" value="${actionBean.trip.car.configurableTime}" />
						  <jsp:param name="maxCostPerConfigurableHour" value="${actionBean.trip.car.maxCostPerConfigurableHour}" />
						  <jsp:param name="includedDistancePerConfigurableHour" value="${actionBean.trip.car.includedDistancePerConfigurableHour}" />
						  <jsp:param name="maxCostPerDay" value="${actionBean.trip.car.maxCostPerDay}" />
						  <jsp:param name="includedDistancePerDay" value="${actionBean.trip.car.includedDistancePerDay}" />						  
						</jsp:include>
                        <div class="clear"></div>
                    </li>
				</ul>			 
			</nav>
		</section>
        <div class="warningMessage">
            <fmt:message key="car.details.price.message"/>
            <stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" event="basePrice">
                <strong><fmt:message key="car.details.price.link.message"/></strong>
            </stripes:link>
        </div>
		<section>
			<nav class="panel">
				<ul>
					<li class="detail">
						<span>
							<fmt:message key="car.details.start.date"/>
						</span>
						<span>
							<fmt:formatDate value="${actionBean.trip.startDate.time}" pattern="${applicationScope.configuration.dateTimePattern}"/>
						</span>
					</li>
					<li class="detail">
						<span>
							<fmt:message key="car.details.end.date"/>
						</span>
						<span>
							<fmt:formatDate value="${actionBean.trip.endDate.time}" pattern="${applicationScope.configuration.dateTimePattern}"/>
						</span>
					</li>
				</ul>
			</nav>
			<c:if test="${actionBean.trip.state != 'CANCELED' && actionBean.trip.state != 'WAIT_OBS_ADVANCE' && actionBean.trip.state != 'IN_ERROR'}">
				<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.EditAvanceBookingActionBean" class="linkBtn gray" addSourcePage="true">
					<stripes:param name="activityCode" value="${actionBean.trip.bookingNumber}"/>
					<fmt:message key="trip.detail.advance.booking.edit"/>
				</stripes:link>
				<a id="openConfirm" href="#" class="linkBtn orangered">
					<fmt:message key="trip.detail.advance.booking.cancel"/>
				</a>
				<div class="warningMessage">
					<c:choose>
						<c:when test="${actionBean.trip.cancelCost != null && actionBean.trip.cancelCost.unscaledValue() != 0}">
							<fmt:message key="trip.detail.advance.booking.cancel.cost">
								<fmt:param><mobi:formatMobics value="${actionBean.trip.cancelTime}" type="time"/></fmt:param>
								<fmt:param><mobi:formatMobics value="${actionBean.trip.cancelCost}" type="currencySymbol"/></fmt:param>
							</fmt:message>
						</c:when>
						<c:otherwise><fmt:message key="trip.detail.advance.booking.cancel.no.fees"/></c:otherwise>
					</c:choose>
				</div>
			</c:if>
			<c:if test="${actionBean.trip.state == 'CANCELED'}">
			  <div class="warningMessageRed">
			    <fmt:message key="trip.detail.advance.booking.cancel.already"/>
			  </div>
			</c:if>
		</section>
	</article>
	
	<!-- Modal window for confirmation -->
	<div class="confirm">
		<article>
			<section>
				<h2 class="redText increaseText"><fmt:message key="trip.detail.advance.booking.cancel.header"/></h2>
				<h3 id="haveFee" class="hidden">
					<fmt:message key="trip.detail.advance.booking.cancel.cost">
						<fmt:param><mobi:formatMobics value="${actionBean.trip.cancelTime}" type="time"/></fmt:param>
						<fmt:param><mobi:formatMobics value="${actionBean.trip.cancelCost}" type="currencySymbol"/></fmt:param>
					</fmt:message>
				</h3>
				<h3 id="dontHaveFee" class="hidden">
					<fmt:message key="trip.detail.advance.booking.cancel.no.fees"/>
				</h3>
				<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.recent.RecentActivitiesActionBean" class="alertBtn orangered" event="cancelAdvanceBooking" addSourcePage="true">
					<stripes:param name="activityCode" value="${actionBean.trip.bookingNumber}"/>
					<fmt:message key="trip.detail.advance.booking.cancel"/>
				</stripes:link>
				<a id="closeConfirm" href="#" class="alertBtn gray" >
					<fmt:message key="trip.detail.advance.booking.cancel.dont"/>
				</a>
			</section>
		</article>
	</div>	
</t:main>