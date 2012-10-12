<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>

<c:if test="${actionBean.cars != null}">
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
										<img src="${contextPath}/booking/ImmediateBooking.action?getCarImage=&licensePlate=${car.licensePlate}" />
									</div>
									<div>
										<div>
											<span>${car.licensePlate}</span>
											<span>${car.carBrandName}&nbsp;${car.carModelName}</span>
											<span>${car.fuelType}&nbsp;(${car.range})</span>
										</div>
										<div>
											<span>
											
												<fmt:message key="car.details.price.hour">
													<fmt:param value="${car.priceInUse}"/>
												</fmt:message>
											</span>
											<span>
												<c:choose>
													<c:when test="${car.distance < 1000}">
														<fmt:message key="car.details.distance.meter">
															<fmt:param>
																<fmt:formatNumber value="${car.distance}" maxFractionDigits="0"/>
															</fmt:param>
														</fmt:message>
													</c:when>
													<c:otherwise>
														<fmt:message key="car.details.distance.kilometer">
															<fmt:param>
																<fmt:formatNumber value="${car.distance * 0.001}" maxFractionDigits="1"/>
															</fmt:param>
														</fmt:message>
													</c:otherwise>
												</c:choose>
											</span>
										</div>
									</div>
								</stripes:link>
							</c:when>
							<c:otherwise>
								<stripes:link href="#">
									<div>
										<img src="${contextPath}/booking/ImmediateBooking.action?getCarImage=&licensePlate=${car.licensePlate}" />
									</div>
									<div>
										<div>
											<span>${car.licensePlate}</span>
											<span>${car.carBrandName}&nbsp;${car.carModelName}</span>
											<span>${car.fuelType}&nbsp;(${car.range})</span>
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
</c:if>
