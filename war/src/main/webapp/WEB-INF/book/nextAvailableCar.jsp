<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='car.next.available.title' /></c:set>

<t:main title="${title}">
	
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>

	<article>
		<section>
			<nav class=panel>
				<ul>
					<li class="image">
                        <stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.AdvanceBookingActionBean" event="carDetails" addSourcePage="true">
                            <stripes:param name="licensePlate" value="${actionBean.next.car.licensePlate}"/>
                            <div>
                                <img class="carImage" src="${contextPath}/booking/AdvanceBooking.action?getCarImage=&licensePlate=${actionBean.next.car.licensePlate}&width=58&height=58" />
                            </div>
                            <div>
                                <div>
                                    <span>${actionBean.next.car.licensePlate}</span>
                                    <span>${actionBean.next.car.carBrandName}&nbsp;${actionBean.next.car.carModelName}</span>
                                    <span><fmt:message key="FuelType.${actionBean.next.car.fuelType.name}"/>&nbsp;
                                        <c:choose>
                                            <c:when test="${actionBean.next.car.fuelLevel != null}">(${actionBean.next.car.fuelLevel}%)</c:when>
                                            <c:otherwise>(<fmt:message key="application.value.not.available"/>)</c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>
                            </div>
                        </stripes:link>
					</li>
                    <c:choose>
                        <c:when test="${actionBean.next.car.zones != null}">
                            <li class="link">
                                <stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.AdvanceBookingActionBean" event="parkLocation" addSourcePage="true">
                                    <stripes:param name="licensePlate">${actionBean.next.car.licensePlate}</stripes:param>
                                    <stripes:param name="startDate"><fmt:formatDate value="${actionBean.startDate}" pattern="${applicationScope.configuration.dateTimePattern}"/></stripes:param>
                                    <stripes:param name="endDate"><fmt:formatDate value="${actionBean.endDate}" pattern="${applicationScope.configuration.dateTimePattern}"/></stripes:param>
									<span>
										<fmt:message key="car.details.park"/>
									</span>
									<span>
                                            ${actionBean.next.car.zones[0].zone}
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
							<fmt:message key="car.details.price.locked"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.next.car.priceReservedPerMinute}" type="currencyHour" />
						</span>
                    </li>                    
                    <li class="detail">
						<span>
							<fmt:message key="car.details.price.use"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.next.car.priceBookedPerMinute}" type="currencyHour" />
						</span>
                    </li>
                </ul>
            </nav>
        </section>

        <section>
            <nav class="panel">
                <ul>
                    <li class="detail">
                        <span><fmt:message key="trip.detail.start.date"/></span>
						<span>
                            <fmt:message key="bonus.account.available.from">
                                <fmt:param>
                                    <fmt:formatDate value="${actionBean.next.starTime.time}"
                                                    pattern="${applicationScope.configuration.dateTimePattern}"/>
                                </fmt:param>
                                <fmt:param>
                                    <fmt:formatDate value="${actionBean.next.endTime.time}"
                                                    pattern="${applicationScope.configuration.dateTimePattern}"/>
                                </fmt:param>
                            </fmt:message>
						</span>
                    </li>
                </ul>
            </nav>
        </section>

	</article>
</t:main>