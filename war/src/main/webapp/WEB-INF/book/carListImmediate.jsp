<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>

<c:choose>
	<c:when test="${actionBean.cars != null}">
		<section>
			<nav class="threeColumnList">
				<ul>
					<c:forEach items="${actionBean.cars}" var="car">
						<li>
							<c:choose>
								<c:when test="${car.state == 'AVAILABLE'}">
									<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" event="licensePlateBook">
										<stripes:param name="licensePlate" value="${car.licensePlate}"/>
										<stripes:param name="latitude" value="${actionBean.latitude}"/>
										<stripes:param name="longitude" value="${actionBean.longitude}"/>
										<div>
											<img class="carImage" src="${contextPath}/booking/ImmediateBooking.action?getCarImage=&licensePlate=${car.licensePlate}&width=58&height=58" />
										</div>
										<div>
											<div>
												<span>${car.licensePlate}</span>
												<span>${car.carBrandName}&nbsp;${car.carModelName}</span>
												<span><fmt:message key="FuelType.${car.fuelType.name}"/>&nbsp;
													<c:choose>
														<c:when test="${car.fuelLevel != null}">(${car.fuelLevel}%)</c:when>
														<c:otherwise>(<fmt:message key="application.value.not.available"/>)</c:otherwise>
													</c:choose>
												</span>
											</div>
											<div>
												<span>
													<mobi:formatMobics value="${car.priceBookedPerMinute}" type="currencyHour" />
												</span>
												<span>
													<c:choose>
														<c:when test="${! empty car.distance}">
															<mobi:formatMobics value="${car.distance}" type="distanceDecimal" />									
														</c:when>
														<c:otherwise>
															<fmt:message key="application.value.not.available"/>
														</c:otherwise>
													</c:choose>
												</span>
                                                <span>
                                                    <span class="${!empty car.addOns ? 'showAddOn' : ''}"></span>
                                                    <span class="${!empty car.promotions ? 'showPromotion' : ''}"></span>
                                                </span>
											</div>
										</div>
									</stripes:link>
								</c:when>
								<c:otherwise>
									<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" event="carDetails">
										<stripes:param name="licensePlate" value="${car.licensePlate}"/>
										<stripes:param name="latitude" value="${actionBean.latitude}"/>
										<stripes:param name="longitude" value="${actionBean.longitude}"/>
										<div>
											<img class="carImage" src="${contextPath}/booking/ImmediateBooking.action?getCarImage=&licensePlate=${car.licensePlate}&width=58&height=58" />
										</div>
										<div>
											<div>
												<span>${car.licensePlate}</span>
												<span>${car.carBrandName}&nbsp;${car.carModelName}</span>
												<span><fmt:message key="FuelType.${car.fuelType.name}"/>&nbsp;
													<c:choose>
														<c:when test="${actionBean.car.fuelLevel != null}">(${actionBean.car.fuelLevel}%)</c:when>
														<c:otherwise>(<fmt:message key="application.value.not.available"/>)</c:otherwise>
													</c:choose>
												</span>
											</div>
											<div>
												<span class="unavailable">
													<fmt:message key="car.details.unavaliable"/>
												</span>
											</div>
										</div>
									</stripes:link>
								</c:otherwise>
							</c:choose>	
						</li>
					</c:forEach>
				</ul>	
			</nav>
		</section>
	</c:when>
    <c:otherwise>
        <section id="noresults">
            <label><fmt:message key="book.now.no.results.found.header"/></label><br/>
            <label><fmt:message key="book.now.no.results.found"/></label>
        </section>
    </c:otherwise>
</c:choose>
