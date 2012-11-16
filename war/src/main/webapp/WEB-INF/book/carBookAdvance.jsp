<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='book.advance.for.later.title' /></c:set>

<t:main title="${title}">
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
	<article>
		<section>
			<nav class="panel">
				<ul>
					<li class="image">
						<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.AdvanceBookingActionBean" event="carDetails" addSourcePage="true">
							<stripes:param name="licensePlate" value="${actionBean.car.licensePlate}"/>
							<div>
								<img src="${contextPath}/booking/AdvanceBooking.action?getCarImage=&licensePlate=${actionBean.car.licensePlate}" />
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
										</c:choose>
										 -->
									</span>
								</div>
							</div>
						</stripes:link>
					</li>
					<!-- you should allways see the park, not the car -->
					<c:choose>
						<c:when test="${actionBean.car.zones != null}">
							<li class="link">
								<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.AdvanceBookingActionBean" event="parkLocation" addSourcePage="true">
									<stripes:param name="licensePlate">${actionBean.car.licensePlate}</stripes:param>
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
					<li class="detail">
						<span>
							<fmt:message key="car.details.price.use"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.car.priceInUse}" type="currencyHour"/>
						</span>
					</li>
					<li class="detail">
						<span>
							<fmt:message key="car.details.price.locked"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.car.priceReserved}" type="currencyHour"/>
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
							<fmt:formatDate value="${actionBean.startDate}" pattern="${applicationScope.configuration.dateTimePattern}"/>
						</span>
					</li>
					<li class="detail">
						<span>
							<fmt:message key="car.details.end.date"/>
						</span>
						<span>
							<fmt:formatDate value="${actionBean.endDate}" pattern="${applicationScope.configuration.dateTimePattern}"/>
						</span>
					</li>
				</ul>
			</nav>
			<div class="cleaner"></div>
			<stripes:link id="showPin" beanclass="com.criticalsoftware.mobics.presentation.action.booking.AdvanceBookingActionBean" class="linkBtn green" event="showPin" addSourcePage="true">
				<stripes:param name="licensePlate" value="${actionBean.car.licensePlate}"/>
				<stripes:param name="startDate">
					<fmt:formatDate value="${actionBean.startDate}" pattern="${applicationScope.configuration.dateTimePattern}"/>
				</stripes:param>
				<stripes:param name="endDate">
					<fmt:formatDate value="${actionBean.endDate}" pattern="${applicationScope.configuration.dateTimePattern}"/>
				</stripes:param>
				<fmt:message key="book.advance.submit"/>
			</stripes:link>
			<div class="warningMessage">
				<fmt:message key="booking.charge.message.advance"/>
			</div>
		</section>
	</article>	
</t:main>