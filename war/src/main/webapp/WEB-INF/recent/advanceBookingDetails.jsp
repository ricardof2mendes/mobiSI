<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='trip.detail.advance.trip.title' /></c:set>

<t:main title="${title}">
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
	<article>
		<section>
			<nav class="panel">
				<ul>
					<li class="image">
						<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.AdvanceBookingActionBean" event="carDetails" addSourcePage="true">
							<stripes:param name="licensePlate" value="${actionBean.trip.car.licensePlate}"/>
							<div>
								<img src="${contextPath}/booking/AdvanceBooking.action?getCarImage=&licensePlate=${actionBean.trip.car.licensePlate}" />
							</div>
							<div>
								<div>
									<span>${actionBean.trip.car.licensePlate} </span>
									<span>${actionBean.trip.car.carBrandName}&nbsp;${actionBean.trip.car.carModelName}</span>
									<span><fmt:message key="FuelType.${actionBean.trip.car.fuelType.name}"/>&nbsp;
									<!-- RANGE removed
										<c:choose>
											<c:when test="${actionBean.trip.car.range != null}">(${actionBean.trip.car.range})</c:when>
											<c:otherwise>(<fmt:message key="application.value.not.available"/>)</c:otherwise>
										</c:choose>
										 -->
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
							<fmt:message key="car.details.price.use"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.trip.car.priceInUse}" type="currencyHour"/>
						</span>
					</li>
					<li class="detail">
						<span>
							<fmt:message key="car.details.price.locked"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.trip.car.priceReserved}" type="currencyHour"/>
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
			<div class="cleaner"></div>
			<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.EditAvanceBookingActionBean" class="linkBtn gray" addSourcePage="true">
				<stripes:param name="activityCode" value="${actionBean.trip.bookingNumber}"/>
				<fmt:message key="trip.detail.advance.booking.edit"/>
			</stripes:link>
			<a id="openConfirm" href="#" class="linkBtn orangered">
				<fmt:message key="trip.detail.advance.booking.cancel"/>
			</a>
			<div class="warningMessage">
				<c:choose>
					<c:when test="${actionBean.trip.cancelCost != null}">
						<fmt:message key="trip.detail.advance.booking.cancel.cost">
							<fmt:param><mobi:formatMobics value="${actionBean.time}" type="time"/></fmt:param>
							<fmt:param><mobi:formatMobics value="${actionBean.trip.cancelCost}" type="currencySymbol"/></fmt:param>
						</fmt:message>
					</c:when>
					<c:otherwise><fmt:message key="trip.detail.advance.booking.cancel.no.fees"/></c:otherwise>
				</c:choose>
			</div>
		</section>
	</article>
	
	<!-- Modal window for confirmation -->
	<div class="confirm">
		<article>
			<section>
				<h2><fmt:message key="trip.detail.advance.booking.cancel.header"/></h2>
				<h3>
				<c:choose>
					<c:when test="${actionBean.trip.cancelCost != null}">
						<fmt:message key="trip.detail.advance.booking.cancel.cost">
							<fmt:param><mobi:formatMobics value="${actionBean.time}" type="time"/></fmt:param>
							<fmt:param><mobi:formatMobics value="${actionBean.trip.cancelCost}" type="currencySymbol"/></fmt:param>
						</fmt:message>
					</c:when>
					<c:otherwise><fmt:message key="trip.detail.advance.booking.cancel.no.fees"/></c:otherwise>
				</c:choose>
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