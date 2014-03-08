<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='car.details.title' /></c:set>

<t:main title="${title}">

	<jsp:include page="/WEB-INF/common/message_error.jsp"/>

	<article>
		<section>
			<nav class=panel>
				<ul>
					<li class="imageNoLink">
						<div>
							<img class="carImage" src="${contextPath}/booking/ImmediateBooking.action?getCarImage=&licensePlate=${actionBean.car.licensePlate}&width=58&height=58" />
						</div>
						<div>
							<div>
								<span>${actionBean.car.licensePlate}</span>
								<span>${actionBean.car.carBrandName}&nbsp;${actionBean.car.carModelName}</span>
								<span><fmt:message key="FuelType.${actionBean.car.fuelType.name}"/>&nbsp;
									<c:choose>
										<c:when test="${actionBean.car.fuelLevel != null}">(${actionBean.car.fuelLevel}%)</c:when>
										<c:otherwise>(<fmt:message key="application.value.not.available"/>)</c:otherwise>
									</c:choose>
								</span>
							</div>
						</div>
					</li>
				</ul>	
			</nav>
		</section>
		
		<section>
			<nav class="panel">
				<ul>	
					<li class="detail">
						<span>
							<fmt:message key="car.details.license.plate"/>
						</span>
						<span>
							${actionBean.car.licensePlate}
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
							<fmt:message key="car.details.brand"/>
						</span>
						<span>
							${actionBean.car.carBrandName}
						</span>
					</li>
					<li class="detail">
						<span>
							<fmt:message key="car.details.model"/>
						</span>
						<span>
							${actionBean.car.carModelName}
						</span>
					</li>
					<li class="detail">
						<span>
							<fmt:message key="car.details.class"/>
						</span>
						<span>
							${actionBean.car.category}
						</span>
					</li>
					<li class="detail">
						<span>
							<fmt:message key="car.details.fuel"/>
						</span>
						<span>
							<fmt:message key="FuelType.${actionBean.car.fuelType.name}"/>
						</span>
					</li>
					<li class="detail">
						<span>
							<fmt:message key="car.details.fuelLevel"/>
						</span>
						<span>
							<c:choose>
								<c:when test="${actionBean.car.fuelLevel != null}">${actionBean.car.fuelLevel} %</c:when>
								<c:otherwise><fmt:message key="application.value.not.available"/></c:otherwise>
							</c:choose>
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
							<fmt:message key="trip.detail.price.booked.per.minute"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.car.priceBookedPerMinute}" type="currencySymbol" />
						</span>
                    </li>
                    <li class="detail">
						<span>
							<fmt:message key="trip.detail.cost.per.extra"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.car.costPerExtraKm}" type="currencySymbol" />
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
                                <mobi:formatMobics value="${actionBean.car.maxCostPerHour}" type="currencySymbol" /> -
                                <mobi:formatMobics value="${actionBean.car.distanceThreshold}" type="distance" />
                            </span>
							<span>
							    ${actionBean.trip.car.configurableTime}h -
                                <mobi:formatMobics value="${actionBean.car.maxCostPerConfigurableHour}" type="currencySymbol" /> -
                                <mobi:formatMobics value="${actionBean.car.includedDistancePerConfigurableHour}" type="distance" />
                            </span>
							<span>
                                <fmt:message key="trip.detail.max.cost.max.hour"/> -
                                <mobi:formatMobics value="${actionBean.car.maxCostPerDay}" type="currencySymbol" /> -
                                <mobi:formatMobics value="${actionBean.car.includedDistancePerDay}" type="distance" />
                            </span>
                        </div>
                        <div class="clear"></div>
                    </li>
                </ul>
			</nav>
		</section>
		
		<c:if test="${actionBean.car.state == 'AVAILABLE' }">
			<section>
				<nav class="panel">
					<ul>
						<li class="link">
							<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" event="carLocation">
								<stripes:param name="licensePlate">${actionBean.car.licensePlate}</stripes:param>
								<span>
									<fmt:message key="car.details.location"/>
								</span>
								<span>
									${actionBean.location}
								</span>	
							</stripes:link>
						</li>
						<li class="detail">
							<span>
								<fmt:message key="car.details.distance"/> 
							</span>
							<span>
								<c:choose>
									<c:when test="${! empty actionBean.car.distance}">
										<mobi:formatMobics value="${actionBean.car.distance}" type="distance" />									
									</c:when>
									<c:otherwise>
										<fmt:message key="application.value.not.available"/>
									</c:otherwise>
								</c:choose>
							</span>
						</li>
					</ul>
				</nav>
			</section>
		</c:if>
		
		<section>
			<nav class="panel">
				<ul>	
					<li class="detail">
						<span>
							<fmt:message key="car.details.owner"/>
						</span>
						<span>
							${actionBean.car.carClubName}
						</span>
					</li>
				</ul>			 
			</nav>
		</section>
		
	</article>	
</t:main>