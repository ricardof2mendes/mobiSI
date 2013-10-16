<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='event.title' /></c:set>

<t:main title="${title}">
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
	
	<article>
		<section>
            <h2><fmt:message key='event.title' /></h2>
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
            <h2><fmt:message key='incident.cost' /></h2>
			<nav class="panel">
				<ul>
					<li class="detail white">
						<span>
							<fmt:message key="incident.total.cost"/>
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
	</article>
</t:main>