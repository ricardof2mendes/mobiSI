<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='book.title.${param.title}' /></c:set>

<t:main title="${title}">
	<c:choose>
		<c:when test="${actionBean.fieldErrors}">
			<stripes:errors/>
		</c:when>
		<c:otherwise>
			<div class="globalErrors">
				<stripes:errors/>
			</div>
		</c:otherwise>
	</c:choose>
	<article>
		<section>
			<nav class="panel">
				<ul>
					<li class="image">
						<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" event="carDetails" addSourcePage="true">
							<stripes:param name="licensePlate" value="${actionBean.car.licensePlate}"/>
							<stripes:param name="latitude" value="${actionBean.latitude}"/>
							<stripes:param name="longitude" value="${actionBean.longitude}"/>
							<div>
								<img src="${contextPath}/booking/ImmediateBooking.action?getCarImage=&licensePlate=${actionBean.car.licensePlate}" />
							</div>
							<div>
								<div>
									<span>${actionBean.car.licensePlate} </span>
									<span>${actionBean.car.carBrandName}&nbsp;${actionBean.car.carModelName}</span>
									<span><fmt:message key="FuelType.${actionBean.car.fuelType}"/>&nbsp;
										<c:choose>
											<c:when test="${actionBean.car.range != null}">(${actionBean.car.range})</c:when>
											<c:otherwise>(<fmt:message key="application.value.not.available"/>)</c:otherwise>
										</c:choose>
									</span>
								</div>
							</div>
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
					<li class="link">
						<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" event="carLocation" addSourcePage="true">
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
				</ul>			 
			</nav>
			<div class="cleaner"></div>
			<stripes:link id="showPin" beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" class="linkBtn green" event="showPin" addSourcePage="true">
				<stripes:param name="licensePlate" value="${actionBean.car.licensePlate}"/>
				<stripes:param name="latitude" value="${actionBean.latitude}"/>
				<stripes:param name="longitude" value="${actionBean.longitude}"/>
				<fmt:message key="book.now.submit"/>
			</stripes:link>
			<div class="warningMessage">
				<fmt:message key="booking.charge.message"/>
			</div>
		</section>
	</article>	
</t:main>