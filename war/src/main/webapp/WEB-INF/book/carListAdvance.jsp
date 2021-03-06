<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page">
	<fmt:message key='book.advance.title' />
</c:set>

<t:main title="${title}">
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
	
	<article class="bodyWhite">
		<c:choose>
			<c:when test="${actionBean.cars != null}">
				<section>
					<nav class="threeColumnList">
						<ul>
							<c:forEach items="${actionBean.cars}" var="car">
								<li>
									<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.AdvanceBookingActionBean" event="licensePlateBookAdvance">
										<stripes:param name="licensePlate" value="${car.licensePlate}"/>
										<stripes:param name="startDate"><fmt:formatDate value="${actionBean.startDate}" pattern="${applicationScope.configuration.dateTimePattern}"/></stripes:param>
										<stripes:param name="endDate"><fmt:formatDate value="${actionBean.endDate}" pattern="${applicationScope.configuration.dateTimePattern}"/></stripes:param>
										<div>
											<img class="carImage" src="${contextPath}/booking/AdvanceBooking.action?getCarImage=&licensePlate=${car.licensePlate}&width=58&height=58" />
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
													<mobi:formatMobics value="${car.priceBookedPerMinute}" type="currencyHour" />
												</span>
												<span><!--Not showing distance--></span>
                                                <span>
                                                    <span class="${!empty car.addOns ? 'showAddOn' : ''}"></span>
                                                    <span class="${!empty car.promotions ? 'showPromotion' : ''}"></span>
                                                </span>
											</div>
										</div>
									</stripes:link>
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
	</article>
</t:main>