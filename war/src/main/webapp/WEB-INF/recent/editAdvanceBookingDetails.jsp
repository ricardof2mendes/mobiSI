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
							<span class="customComboBox">
							
							<c:set var="begin" scope="page">
								<fmt:formatDate value="${actionBean.trip.endDate.time}" pattern="${applicationScope.configuration.dateTimePattern}"/>
							</c:set>
							<c:set var="end" scope="page">
								<fmt:formatDate value="${actionBean.extendBookingDate}" pattern="${applicationScope.configuration.dateTimePattern}"/>
							</c:set>
							
							<stripes:text id="limited" name="endDate" class="editable"
									value="${actionBean.trip.endDate.time}" 
									placeholder="${placeholder}" formatPattern="${applicationScope.configuration.dateTimePattern}"
									data-begin="${pageScope.begin}" 
									data-limit="${pageScope.end}"/>
									
							</span>
						</li>
					</ul>
				</nav>
			</section>
			<section class="submit">
				<div class="warningMessage">
					<c:choose>
						<c:when test="${not empty actionBean.extendBookingDate}">
							<fmt:message key="trip.detail.advance.booking.extend.date">
								<fmt:param>
									<fmt:formatDate value="${actionBean.extendBookingDate}" pattern="${applicationScope.configuration.dateTimePattern}"/>
								</fmt:param>
							</fmt:message>
						</c:when>
						<c:otherwise>
							<fmt:message key="trip.detail.advance.booking.extend.date.free"/>
						</c:otherwise>
					</c:choose>			
				</div>
				<stripes:submit name="saveAdvanceBooking"  class="submitBtn gray"><fmt:message key="trip.detail.advance.booking.save"/></stripes:submit>
			</section>
		</article>	
	</stripes:form>
</t:main>