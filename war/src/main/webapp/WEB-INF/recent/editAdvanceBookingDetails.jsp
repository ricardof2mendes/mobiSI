<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='trip.detail.advance.trip.extend.title' /></c:set>
<c:set var="placeholder" scope="page"><fmt:message key="book.advance.date.placeholder"/></c:set>

<t:main title="${title}" addCalendar="true">
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
	<stripes:form beanclass="com.criticalsoftware.mobics.presentation.action.booking.EditAvanceBookingActionBean" method="post">
		<stripes:hidden name="activityCode" value="${actionBean.trip.bookingNumber}"/>
		<article>
			<section>
				<nav class="panel">
					<ul>
						<li class="image">
							<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.EditAvanceBookingActionBean" event="carDetails" addSourcePage="true">
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
									<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.EditAvanceBookingActionBean" event="parkLocation" addSourcePage="true">
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
							<span class="customComboBox">
								<stripes:text id="endDate" name="endDate" class="editable" value="${actionBean.trip.endDate.time}"
									   placeholder="${placeholder}" formatPattern="${applicationScope.configuration.dateTimePattern}"/>
							</span>
						</li>
					</ul>
				</nav>
			</section>
			<section class="submit">
				<div class="warningMessage">			
					<fmt:formatDate value="${actionBean.extendBookingDate}" pattern="${applicationScope.configuration.dateTimePattern}"/>
				</div>
				<stripes:submit name="saveAdvanceBooking"  class="submitBtn gray"><fmt:message key="trip.detail.advance.booking.save"/></stripes:submit>
				<div class="warningMessage">
					<fmt:message key="trip.detail.advance.booking.edit.cost">
						<fmt:param><mobi:formatMobics value="${actionBean.trip.extendCost}" type="currencySymbol"/></fmt:param>
					</fmt:message>
				</div>
			</section>
		</article>	
	</stripes:form>
</t:main>