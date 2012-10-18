<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='book.title.${param.title}' /></c:set>

<t:main title="${title}">
	<stripes:errors/>
	<article>
		<section>
			<nav class="bookList">
				<ul>
					<li>
						<stripes:link href="#">
							<div>
								<img src="${contextPath}/booking/ImmediateBooking.action?getCarImage=&licensePlate=${actionBean.car.licensePlate}" />
							</div>
							<div>
								<div>
									<span>${actionBean.car.licensePlate} </span>
									<span>${actionBean.car.carBrandName}&nbsp;${actionBean.car.carModelName}</span>
									<span>${actionBean.car.fuelType}&nbsp;(${actionBean.car.range})</span>
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
			<stripes:link id="showPin" beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" class="greenBtn" event="showPin" addSourcePage="true">
				<stripes:param name="licensePlate" value="${actionBean.car.licensePlate}"/>
				<fmt:message key="book.now.submit"/>
			</stripes:link>
			<div class="warningMessage">
				<fmt:message key="booking.charge.message"/>
			</div>
		</section>
	</article>	
</t:main>