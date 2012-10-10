<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='nearest.car.title' /></c:set>

<t:main title="${title}" hideFooter="true">
	<article>
		<section>
			<nav class="bookList">
				<ul>
					<li>
						<stripes:link href="#">
							<div>
								<img src="${contextPath}/book/LicensePlate.action?getCarImage=&licensePlate=${actionBean.car.licensePlate}" />
							</div>
							<div>
								<div>
									<span>${actionBean.car.licensePlate}</span>
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
							<c:choose>
								<c:when test="${actionBean.car.distanceThreshold < 1000}">
									<fmt:message key="car.details.distance.meter">
										<fmt:param>
											<fmt:formatNumber value="${actionBean.car.distanceThreshold}" maxFractionDigits="0"/>
										</fmt:param>
									</fmt:message>
								</c:when>
								<c:otherwise>
									<fmt:message key="car.details.distance.kilometer">
										<fmt:param>
											<fmt:formatNumber value="${actionBean.car.distanceThreshold * 0.001}" maxFractionDigits="1"/>
										</fmt:param>
									</fmt:message>
								</c:otherwise>
							</c:choose>
						</span>
					</li>
					<li class="link">
						<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.book.LicensePlateActionBean" event="carLocation">
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
							<fmt:message key="car.details.price.hour"><fmt:param value="${actionBean.car.priceInUse}"/></fmt:message>
						</span>
					</li>
					<li class="detail">
						<span>
							<fmt:message key="car.details.price.locked"/>
						</span>
						<span>
							<fmt:message key="car.details.price.hour"><fmt:param value="${actionBean.car.priceReserved}"/></fmt:message>
						</span>
					</li>
				</ul>			 
			</nav>
			<div class="cleaner"></div>
			<stripes:link href="#" id="nearestCar" class="greenBtn">
				<stripes:param name="licensePlate">${actionBean.car.licensePlate}</stripes:param>
				<fmt:message key="book.now.submit"/>
			</stripes:link>
			<div class="warningMessage">
				<fmt:message key="booking.charge.message"/>
			</div>
		</section>
	</article>	
</t:main>