<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='book.title.${param.title}' /></c:set>

<t:main title="${title}">
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
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
								<img  class="carImage" src="${contextPath}/booking/ImmediateBooking.action?getCarImage=&licensePlate=${actionBean.car.licensePlate}" />
							</div>
							<div>
								<div>
									<span>${actionBean.car.licensePlate} </span>
									<span>${actionBean.car.carBrandName}&nbsp;${actionBean.car.carModelName}</span>
									<span><fmt:message key="FuelType.${actionBean.car.fuelType.name}"/>&nbsp;
									<!-- RANGE removed
										<c:choose>
											<c:when test="${actionBean.car.range != null}">(${actionBean.car.range})</c:when>
											<c:otherwise>(<fmt:message key="application.value.not.available"/>)</c:otherwise>
										</c:choose> -->
									</span>
								</div>
							</div>
						</stripes:link>
					</li>
					<c:if test="${actionBean.car.state == 'AVAILABLE' }">
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
					</c:if>	
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
				</ul>			 
			</nav>
			
			<c:if test="${actionBean.car.state == 'AVAILABLE' }">
				<stripes:link id="showPin" beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" class="linkBtn green" event="showPin" addSourcePage="true">
					<stripes:param name="licensePlate" value="${actionBean.car.licensePlate}"/>
					<stripes:param name="latitude" value="${actionBean.latitude}"/>
					<stripes:param name="longitude" value="${actionBean.longitude}"/>
					<fmt:message key="book.now.submit"/>
				</stripes:link>
				<div class="warningMessage">
					<fmt:message key="booking.charge.message"/>
				</div>
			</c:if>
		</section>
	</article>	
</t:main>