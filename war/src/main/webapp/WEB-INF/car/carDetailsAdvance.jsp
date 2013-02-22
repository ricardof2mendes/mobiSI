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
								<!-- RANGE removed
									<c:choose>
										<c:when test="${actionBean.car.range != null}">(${actionBean.car.range})</c:when>
										<c:otherwise>(<fmt:message key="application.value.not.available"/>)</c:otherwise>
									</c:choose>-->
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
					<!-- RANGE removed
					<li class="detail">
						<span>
							<fmt:message key="car.details.range"/>
						</span>
						<span>
							<c:choose>
								<c:when test="${actionBean.car.range != null}">${actionBean.car.range}</c:when>
								<c:otherwise><fmt:message key="application.value.not.available"/></c:otherwise>
							</c:choose>
						</span>
					</li>-->
				</ul>			 
			</nav>
		</section>
		
		<section>
			<nav class="panel">
				<ul>	
					<li class="detail">
						<span>
							<fmt:message key="car.details.price.use"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.car.priceInUse}" type="currencyHour" />
						</span>
					</li>
					<li class="detail">
						<span>
							<fmt:message key="car.details.price.locked"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.car.priceReserved}" type="currencyHour" />
						</span>
					</li>
					<li class="detail">
						<span>
							<fmt:message key="car.details.price.included"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.car.distanceThreshold}" type="distance" />
						</span>
					</li>
					<li class="detail">
						<span>
							<fmt:message key="car.details.price.extra"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.car.costPerExtraKm}" type="currencySymbol" />
						</span>
					</li>
				</ul>			 
			</nav>
		</section>
		
		<section>
			<nav class="panel">
				<ul>
					<c:choose>
						<c:when test="${actionBean.car.zones != null}">
							<li class="link">
								<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.AdvanceBookingActionBean" event="parkLocation" addSourcePage="true">
									<stripes:param name="licensePlate">${actionBean.car.licensePlate}</stripes:param>
									<stripes:param name="startDate"><fmt:formatDate value="${actionBean.startDate}" pattern="${applicationScope.configuration.dateTimePattern}"/></stripes:param>
									<stripes:param name="endDate"><fmt:formatDate value="${actionBean.endDate}" pattern="${applicationScope.configuration.dateTimePattern}"/></stripes:param>
									<span>
										<fmt:message key="car.details.park"/>
									</span>
									<span>
										${actionBean.car.zones[0].zone} 
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
				</ul>
			</nav>
		</section>
		
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