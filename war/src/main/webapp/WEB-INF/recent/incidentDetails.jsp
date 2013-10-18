<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key="incident.title.${actionBean.activityType}" /></c:set>

<t:main title="${title}">
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
	
	<article>
		<section>
            <h2><fmt:message key="incident.title.${actionBean.activityType}" /></h2>
			<nav class="panel">
				<ul>
					<li class="detail white">
						<span>
							<fmt:message key="incident.type"/>
						</span>
						<span>
							${actionBean.incident.type}
						</span>
					</li>
					<li class="detail white">
						<span>
							<fmt:message key="incident.number"/>
						</span>
						<span>
							${actionBean.incident.number}
						</span>
					</li>
				</ul>			 
			</nav>
		</section>
        <section>
            <c:choose>
                <c:when test="${actionBean.incident.isNegativeEvent}">
                    <c:set var="title"><fmt:message key='incident.cost' /></c:set>
                    <c:set var="subtitle"><fmt:message key="incident.total.cost"/></c:set>
                </c:when>
                <c:otherwise>
                    <c:set var="title"><fmt:message key='incident.credit' /></c:set>
                    <c:set var="subtitle"><fmt:message key="incident.credit.value"/></c:set>
                </c:otherwise>
            </c:choose>
            <h2>${pageScope.title}</h2>
            <nav class="panel">
                <ul>
                    <li class="detail white">
						<span>
							${pageScope.subtitle}
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.incident.totalCost}" type="currencySymbol"/>
						</span>
                    </li>
                </ul>
            </nav>
		</section>
        <section>
            <h2><fmt:message key='incident.details' /></h2>
            <nav class="panel">
                <ul>
                    <li class="detail white">
						<span>
							<fmt:message key="incident.date"/>
						</span>
						<span>
							<fmt:formatDate value="${actionBean.incident.dateOfEvent.time}" pattern="${applicationScope.configuration.dateTimePattern}"/>
						</span>
                    </li>
                    <li class="detail white">
						<span>
							<fmt:message key="incident.liability.party"/>
						</span>
						<span>
							<fmt:message key="application.value.${actionBean.incident.liableParty}"/>
						</span>
                    </li>
                    <li class="detail white">
						<span>
							<fmt:message key="incident.description"/>
						</span>
						<span>
                                ${actionBean.incident.description}
                        </span>
                    </li>
                </ul>
            </nav>
        </section>
        <c:if test="${!empty actionBean.incident.car}">
            <section>
                <h2><fmt:message key='incident.booked.car' /></h2>
                <nav class="panel">
                    <ul>
                        <li class="link white">
                            <c:choose>
                                <c:when test="${actionBean.incident.car.carType == 'NORMAL'}">
                                    <c:set var="bookingTypeBean" value="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="bookingTypeBean" value="com.criticalsoftware.mobics.presentation.action.booking.AdvanceBookingActionBean"/>
                                </c:otherwise>
                            </c:choose>

                            <stripes:link id="carDetails" beanclass="${pageScope.bookingTypeBean}" event="carDetails" addSourcePage="true" >
                                <stripes:param name="licensePlate" value="${actionBean.incident.car.licensePlate}"/>
                                <span><fmt:message key="trip.detail.license.plate"/></span>
                                <span>${actionBean.incident.car.licensePlate}</span>
                            </stripes:link>
                        </li>
                    </ul>
                </nav>
            </section>
        </c:if>
	</article>
</t:main>