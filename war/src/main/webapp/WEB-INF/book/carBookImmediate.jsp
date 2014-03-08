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
								<img  class="carImage" src="${contextPath}/booking/ImmediateBooking.action?getCarImage=&licensePlate=${actionBean.car.licensePlate}&width=58&height=58" />
							</div>
							<div>
								<div>
									<span>${actionBean.car.licensePlate} </span>
									<span>${actionBean.car.carBrandName}&nbsp;${actionBean.car.carModelName}</span>
									<span><fmt:message key="FuelType.${actionBean.car.fuelType.name}"/>&nbsp;

                                    <c:choose>
                                        <c:when test="${actionBean.car.fuelLevel != null}">(${actionBean.car.fuelLevel}%)</c:when>
                                        <c:otherwise>(<fmt:message key="application.value.not.available"/>)</c:otherwise>
                                    </c:choose>
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
							<fmt:message key="trip.detail.price.booked.per.minute"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.car.priceBookedPerMinute}" type="currencySymbol" />
						</span>
                    </li>
                    <li class="detail">
						<span>
							<fmt:message key="trip.detail.cost.per.extra"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.car.costPerExtraKm}" type="currencySymbol" />
						</span>
                    </li>
                    <li class="detail">
                        <div class="left">
                            <span>
                                <fmt:message key="trip.detail.max.cost"/>
                            </span>
                        </div>
                        <div class="right">
                            <span>
                                <fmt:message key="trip.detail.max.cost.one.hour"/> -
                                <mobi:formatMobics value="${actionBean.car.maxCostPerHour}" type="currencySymbol" /> -
                                <mobi:formatMobics value="${actionBean.car.distanceThreshold}" type="distance" />
                            </span>
							<span>
							    ${actionBean.car.configurableTime}h -
                                <mobi:formatMobics value="${actionBean.car.maxCostPerConfigurableHour}" type="currencySymbol" /> -
                                <mobi:formatMobics value="${actionBean.car.includedDistancePerConfigurableHour}" type="distance" />
                            </span>
							<span>
                                <fmt:message key="trip.detail.max.cost.max.hour"/> -
                                <mobi:formatMobics value="${actionBean.car.maxCostPerDay}" type="currencySymbol" /> -
                                <mobi:formatMobics value="${actionBean.car.includedDistancePerDay}" type="distance" />
                            </span>
                        </div>
                        <div class="clear"></div>
                    </li>
				</ul>
			</nav>
        </section>
    </article>
    <div class="warningMessage">
        <fmt:message key="car.details.price.message"/>
        <stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" event="price">
            <strong><fmt:message key="car.details.price.link.message"/></strong>
        </stripes:link>
    </div>
	<article>
        <section>
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

    <c:if test="${!empty actionBean.car.addOns}">
        <article>
            <section>
                <h2><fmt:message key="booking.addons.message" /></h2>
                <nav class="simpleList">
                    <ul>
                        <c:forEach items="${actionBean.car.addOns}" var="addon">
                            <li class="cDetail addonDetail">
                                <stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.addons.AddOnsActionBean" event="detail" addSourcePage="true">
                                    <stripes:param name="code">${addon.code}</stripes:param>
                                    <div>
                                        <span>${addon.name}</span>
                                        <span><fmt:formatNumber value="${addon.discount}" pattern="${applicationScope.configuration.meterPattern}"/>&nbsp;%</span>
                                    </div>
                                </stripes:link>
                            </li>
                        </c:forEach>
                    </ul>
                </nav>
            </section>
        </article>
    </c:if>

    <c:if test="${!empty actionBean.car.promotions}">
        <article>
            <section>
                <h2><fmt:message key="booking.promotions.message" /></h2>
                <nav class="simpleList">
                    <ul>
                        <c:forEach items="${actionBean.car.promotions}" var="promo">
                            <li class="cDetail promotionDetail">
                                <stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.promotions.PromotionsActionBean" event="detail" addSourcePage="true">
                                    <stripes:param name="code">${promo.code}</stripes:param>
                                    <div>
                                        <span>${promo.name}</span>
                                        <span>
                                            <fmt:formatNumber value="${promo.discount}" pattern="${applicationScope.configuration.meterPattern}"/>&nbsp;%
                                        </span>
                                    </div>
                                </stripes:link>
                            </li>
                        </c:forEach>
                    </ul>
                </nav>
            </section>
        </article>
    </c:if>

    <c:if test="${!empty actionBean.car.addOns || !empty actionBean.car.promotions}">
        <article>
            <section>
                <div class="warningMessage">
                    <fmt:message key="booking.addons.promotions.message"/>
                </div>
            </section>
        </article>
    </c:if>

	<jsp:include page="/WEB-INF/common/geolocationErrorAlert.jsp"/>
</t:main>