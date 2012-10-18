<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>

<c:choose>
	<c:when test="${actionBean.cars != null}">
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
											<img src="${contextPath}/booking/ImmediateBooking.action?getCarImage=&licensePlate=${car.licensePlate}&${car.licensePlate}.png" />
										</div>
										<div>
											<div>
												<span>${car.licensePlate}</span>
												<span>${car.carBrandName}&nbsp;${car.carModelName}</span>
												<span>${car.fuelType}&nbsp;(${car.range})</span>
											</div>
											<div>
												<span>
													<mobi:formatMobics value="${car.priceInUse}" type="currency" 
																	   pattern="${applicationScope.configuration.currencyPattern}"/>
												</span>
												<span>
													<mobi:formatMobics value="${car.distance}" type="distance" 
																	   pattern="${applicationScope.configuration.meterPattern}" 
																	   pattern2="${applicationScope.configuration.kilometerPattern}" />
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
	</c:when>
<c:otherwise>
	<section id="noresults">
		<label><fmt:message key="car.no.results.found"/></label>
	</section>
</c:otherwise>
</c:choose>
