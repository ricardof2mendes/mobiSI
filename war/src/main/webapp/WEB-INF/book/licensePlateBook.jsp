<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key="license.plate.title" /></c:set>

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
									<span>${actionBean.car.carBrandName}&nbsp;${car.carModelName}</span>
									<span>${actionBean.car.fuelType}&nbsp;(${car.distance})</span>
								</div>
							</div>
						</stripes:link>
					</li>
					<li class="detail">
						<span>
							<fmt:message key="car.details.distance"/> 
						</span>
						<span>
							${actionBean.car.distance},&nbsp;TODO
						</span>
					</li>
					<li class="detail">
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