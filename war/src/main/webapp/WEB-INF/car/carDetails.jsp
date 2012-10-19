<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='car.details.title' /></c:set>

<t:main title="${title}">
	<article id="carDetails">
		<section>
			<nav class=detailHeader>
				<ul>
					<li>
						<div>
							<img src="${contextPath}/booking/ImmediateBooking.action?getCarImage=&licensePlate=${actionBean.car.licensePlate}" />
						</div>
						<div>
							<div>
								<span>${actionBean.car.licensePlate}</span>
								<span>${actionBean.car.carBrandName}&nbsp;${actionBean.car.carModelName}</span>
								<span><fmt:message key="FuelType.${actionBean.car.fuelType}"/>&nbsp;(${actionBean.car.range})</span>
							</div>
						</div>
					</li>
				</ul>	
			</nav>
		</section>
		
		<section>
			<nav class="detailBody">
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
			<nav class="detailBody">
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
							<fmt:message key="FuelType.${actionBean.car.fuelType}"/>
						</span>
					</li>
					<li class="detail">
						<span>
							<fmt:message key="car.details.range"/>
						</span>
						<span>
							${actionBean.car.range}
						</span>
					</li>
				</ul>			 
			</nav>
		</section>
		
		<section>
			<nav class="detailBody">
				<ul>	
					<li class="detail">
						<span>
							<fmt:message key="car.details.price.use"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.car.priceInUse}" type="currency" 
											   pattern="${applicationScope.configuration.currencyPattern}"/>
						</span>
					</li>
					<li class="detail">
						<span>
							<fmt:message key="car.details.price.locked"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.car.priceReserved}" type="currency" 
											   pattern="${applicationScope.configuration.currencyPattern}"/>
						</span>
					</li>
					<li class="detail">
						<span>
							<fmt:message key="car.details.price.included"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.car.distanceThreshold}" type="distance" 
											   pattern="${applicationScope.configuration.meterPattern}"
											   pattern2="${applicationScope.configuration.kilometerPattern}"/>
						</span>
					</li>
					<li class="detail">
						<span>
							<fmt:message key="car.details.price.extra"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.car.costPerExtraKm}" type="currency" 
											   pattern="${applicationScope.configuration.currencyPattern}"/>
						</span>
					</li>
				</ul>			 
			</nav>
		</section>
		
		<c:if test="${actionBean.car.state == 'AVAILABLE' }">
			<section>
				<nav class="detailBody">
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
								<mobi:formatMobics value="${actionBean.car.distance}" type="distance" 
												   pattern="${applicationScope.configuration.meterPattern}" 
												   pattern2="${applicationScope.configuration.kilometerPattern}" />
							</span>
						</li>
					</ul>
				</nav>
			</section>
		</c:if>
		
		<section>
			<nav class="detailBody">
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