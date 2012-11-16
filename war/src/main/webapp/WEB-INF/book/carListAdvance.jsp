<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page">
	<fmt:message key='book.advance.title' />
</c:set>

<t:main title="${title}">
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
	
	<c:choose>
		<c:when test="${actionBean.cars != null}">
			<section>
				<nav class="threeColumnList">
					<ul>
						<c:forEach items="${actionBean.cars}" var="car">
							<li>
								<c:choose>
									<c:when test="${car.state == 'AVAILABLE'}">
										<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.AdvanceBookingActionBean" event="licensePlateBookAdvance">
											<stripes:param name="licensePlate" value="${car.licensePlate}"/>
											<stripes:param name="startDate"><fmt:formatDate value="${actionBean.startDate}" pattern="${applicationScope.configuration.dateTimePattern}"/></stripes:param>
											<stripes:param name="endDate"><fmt:formatDate value="${actionBean.endDate}" pattern="${applicationScope.configuration.dateTimePattern}"/></stripes:param>
											<div>
												<img src="${contextPath}/booking/AdvanceBooking.action?getCarImage=&licensePlate=${car.licensePlate}#${car.licensePlate}.png" />
											</div>
											<div>
												<div>
													<span>${car.licensePlate}</span>
													<span>${car.carBrandName}&nbsp;${car.carModelName}</span>
													<span>
														<c:if test="${car.zones != null}">
															${car.zones[0].zone} 
														</c:if>
													</span>
												</div>
												<div>
													<span>
														<mobi:formatMobics value="${car.priceInUse}" type="currencyHour" />
													</span>
													<span></span>
												</div>
											</div>
										</stripes:link>
									</c:when>
									<c:otherwise>
										<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.AdvanceBookingActionBean" event="carDetails">
											<stripes:param name="licensePlate" value="${car.licensePlate}"/>
											<div>
												<img src="${contextPath}/booking/ImmediateBooking.action?getCarImage=&licensePlate=${car.licensePlate}&${car.licensePlate}.png" />
											</div>
											<div>
												<div>
													<span>${car.licensePlate}</span>
													<span>${car.carBrandName}&nbsp;${car.carModelName}</span>
													<span>
														<c:if test="${car.zones != null}">
															${car.zones[0].zone} 
														</c:if>
													</span>
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
				<label><fmt:message key="book.advance.no.results.found.header"/></label><br/>
				<label><fmt:message key="book.advance.no.results.found"/></label>
			</section>
		</c:otherwise>
	</c:choose>
</t:main>