<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='current.trip.title' /></c:set>
<c:set var="warnTitle" scope="page">
	<c:choose>
		<c:when test="${actionBean.current.undesirableZoneCost > 0}">
			<fmt:message key="current.trip.end.trip.confirm.h3">
			  <fmt:param><mobi:formatMobics value="${actionBean.current.undesirableZoneCost}" type="currencySymbol"/></fmt:param>
			</fmt:message>
		</c:when>
		<c:otherwise>
			<fmt:message key="current.trip.end.trip.confirm.h3.no.cost"/>
		</c:otherwise>
	</c:choose>
</c:set>

<t:main title="${title}">
	
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>

	<!-- State variable -->
	<span id="state" class="hidden">${actionBean.current.state}</span>
	
	<article id="statePooling" class="${actionBean.current.state != 'WAIT_OBS_IMMEDIATE' ? 'hidden' : ''} currentTrip">
		<section>
			<h2>
				<fmt:message key="current.trip.validating"/>&nbsp;&nbsp;&nbsp;<img src="${contextPath}/img/indicator.gif"/>
			</h2>
			<div>
				<fmt:message key="current.trip.validating.seconds">
					<fmt:param>
						<mobi:formatMobics type="milliseconds" value="${applicationScope.configuration.statePollingTimeoutMilliseconds}"/>
					</fmt:param>
				</fmt:message>
			</div>
		</section>
	</article>

	<article>
		<section>
			<h2><fmt:message key="current.trip.booked.car"/></h2>
			<nav class="panel">
				<ul>
					<c:choose>
						<c:when test="${actionBean.current.bookingType == 'IMMEDIATE'}">
							<li class="link white">
								<stripes:link id="carDetails" beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" event="carDetails" addSourcePage="true" >
									<stripes:param name="licensePlate" value="${actionBean.current.licensePlate}"/>
			
									<span><fmt:message key="current.trip.car"/></span>
									<span class="ellipsis">
										${actionBean.current.licensePlate}&nbsp;(${actionBean.current.carBrand}&nbsp;${actionBean.current.carModel})
									</span>
								</stripes:link>
							</li>
							<li class="link white">
								<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" event="carLocation" addSourcePage="true" >
									<stripes:param name="licensePlate">${actionBean.current.licensePlate}</stripes:param>
									<span><fmt:message key="current.trip.location"/></span>
									<span>
										${actionBean.location}
									</span>
								</stripes:link>
							</li>
						</c:when>
						<c:otherwise>
							<li class="link white">
								<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.AdvanceBookingActionBean" event="carDetails">
									<stripes:param name="licensePlate" value="${actionBean.current.licensePlate}"/>
			
									<span><fmt:message key="current.trip.car"/></span>
									<span class="ellipsis">
										${actionBean.current.licensePlate}&nbsp;(${actionBean.current.carBrand}&nbsp;${actionBean.current.carModel})
									</span>
								</stripes:link>
							</li>
							<li class="link white">
								<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.AdvanceBookingActionBean" event="parkLocation" addSourcePage="true">
									<stripes:param name="licensePlate">${actionBean.current.licensePlate}</stripes:param>
									<span><fmt:message key="current.trip.location"/></span>
									<span>
										${actionBean.location}
									</span>
								</stripes:link>
							</li>
							<li class="detail white">
								<span>
									<fmt:message key="car.details.start.date"/>
								</span>
								<span>
									<fmt:formatDate value="${actionBean.current.startDate.time}" pattern="${applicationScope.configuration.dateTimePattern}"/>
								</span>
							</li>
							<li class="detail white">
								<span>
									<fmt:message key="car.details.end.date"/>
								</span>
								<span>
									<fmt:formatDate value="${actionBean.current.endDate.time}" pattern="${applicationScope.configuration.dateTimePattern}"/>
								</span>
							</li>
						</c:otherwise>
						</c:choose>
						<li class="detail white">
								<span>
									<fmt:message key="trip.detail.car.state"/>
								</span>
								<span>
									<fmt:message key="CAR_STATE-${actionBean.current.carState}"/>
								</span>
							</li>
					
				</ul>
			</nav>
		</section>
		
		<!-- Unlock car -->
		<c:if test="${actionBean.current.carState == 'BOOKED' && actionBean.current.state != 'WAIT_OBS_IMMEDIATE' && actionBean.current.state != 'OBS_ERROR'}">	
			<section>
				<stripes:link id="unlock" beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="linkBtn gray" event="unlockCar" addSourcePage="true">
					<stripes:param name="licensePlate">${actionBean.current.licensePlate}</stripes:param>
					<fmt:message key="current.trip.button.unlock.car"/>
				</stripes:link>
			</section>
		</c:if>
		
		
		
		<c:choose>
		
			<%-- CCOME --%>
       		<c:when test="${actionBean.newDriverVersion}">
				<c:if test="${actionBean.current.state != 'WAIT_OBS_IMMEDIATE'}">	
					<section>
						<!-- Edit current trip (advance booking) -->
						<c:if test="${actionBean.current.bookingType == 'ADVANCE'}">		
							<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="linkBtn gray" addSourcePage="true" event="extend">
								<fmt:message key="current.trip.button.edit"/>
							</stripes:link>
						</c:if>
						
						<!-- Lock car End trip with js unwanted zone validation-->
						<c:choose>
							<c:when test="${actionBean.current.carState == 'IN_USE' || actionBean.current.carState == 'ENGINE_RUNNING'}">	
								<stripes:link id="lockCar" beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="linkBtn orangered" event="lockCar" addSourcePage="true">
									<stripes:param name="licensePlate">${actionBean.current.licensePlate}</stripes:param>
									<fmt:message key="current.trip.button.lock.car"/>
								</stripes:link>
								<stripes:link id="unlock" beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="linkBtn gray" event="unlockCar" addSourcePage="true">
									<stripes:param name="licensePlate">${actionBean.current.licensePlate}</stripes:param>
									<fmt:message key="current.trip.button.unlock.car"/>
								</stripes:link>
							 </c:when>
							 <c:otherwise>
							 	<stripes:link id="endTrip" beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="linkBtn orangered" event="endTrip" addSourcePage="true">
									<stripes:param name="licensePlate">${actionBean.current.licensePlate}</stripes:param>
									<fmt:message key="current.trip.button.end.trip"/>
								</stripes:link>
								<div class="warningMessage">
									<fmt:message key="current.trip.end.message"/>
								</div>
							 </c:otherwise>
						 </c:choose>
					</section>
				</c:if>
			
           </c:when>
           
           <%-- OLD CCOM --%>
           <c:otherwise>
           		<c:if test="${actionBean.current.state != 'WAIT_OBS_IMMEDIATE'}">	
				<section>
					<!-- Edit current trip (advance booking) -->
					<c:if test="${actionBean.current.bookingType == 'ADVANCE'}">		
						<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="linkBtn gray" addSourcePage="true" event="extend">
							<fmt:message key="current.trip.button.edit"/>
						</stripes:link>
					</c:if>
					
					<!-- Lock car End trip with js unwanted zone validation-->
					<c:if test="${actionBean.current.carState == 'IN_USE'}">		
						<stripes:link id="endTrip" beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="linkBtn orangered" event="lockEndTrip" addSourcePage="true">
							<stripes:param name="licensePlate">${actionBean.current.licensePlate}</stripes:param>
							<fmt:message key="current.trip.button.lock.car.end.trip"/>
						</stripes:link>
						<div class="warningMessage">
							<fmt:message key="current.trip.end.message"/>
						</div>
					</c:if>
				</section>
				</c:if>
           </c:otherwise>
		</c:choose>          
		
		
		
		
		
		<section>
            <h2><fmt:message key="current.trip.details"/></h2>
			<nav class="panel">
				<ul>
					<li class="detail white">
						<span>
							<fmt:message key="current.trip.duration"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.current.bookingDuration}" type="time" />
						</span>
					</li>
					
					<c:choose>
						<c:when test="${actionBean.current.carState == 'BOOKED'}">
							<li class="detail white">
								<span>
									<fmt:message key="current.trip.distance"/>
								</span>
								<span>
									<mobi:formatMobics value="${actionBean.current.currentDistance}" type="distanceDecimal" />
								</span>
							</li>
						</c:when>
						<c:otherwise>
							<li class="detail white">
								<span>
									<fmt:message key="current.trip.current.zone"/>
								</span>
								<span>
									<fmt:message key="ZoneType.${actionBean.current.currentZoneType}"/>
								</span>
							</li>
						</c:otherwise>
					</c:choose>


                    <li class="detail white">
						<span>
							<fmt:message key="trip.detail.price.booked.per.minute"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.current.carDTO.priceBookedPerMinute}" type="currencySymbol" />
						</span>
                    </li>
                    <li class="detail white">
						<span>
							<fmt:message key="trip.detail.cost.per.extra"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.current.carDTO.costPerExtraKm}" type="currencySymbol" />
						</span>
                    </li>
                    <li class="detail white">
                        <jsp:include page="/WEB-INF/common/maxValuesTable.jsp">
						  <jsp:param name="maxCostPerHour" value="${actionBean.current.carDTO.maxCostPerHour}" />
						  <jsp:param name="distanceThreshold" value="${actionBean.current.carDTO.distanceThreshold}" />
						  <jsp:param name="configurableTime" value="${actionBean.current.carDTO.configurableTime}" />
						  <jsp:param name="maxCostPerConfigurableHour" value="${actionBean.current.carDTO.maxCostPerConfigurableHour}" />
						  <jsp:param name="includedDistancePerConfigurableHour" value="${actionBean.current.carDTO.includedDistancePerConfigurableHour}" />
						  <jsp:param name="maxCostPerDay" value="${actionBean.current.carDTO.maxCostPerDay}" />
						  <jsp:param name="includedDistancePerDay" value="${actionBean.current.carDTO.includedDistancePerDay}" />						  
						</jsp:include>
                        <div class="clear"></div>
                    </li>
                </ul>
            </nav>
        </section>

        <div class="warningMessage">
            <fmt:message key="car.details.price.message"/>
            <stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" event="basePrice">
                <strong><fmt:message key="car.details.price.link.message"/></strong>
            </stripes:link>
        </div>


		
	</article>
	
	<!-- Zone variable for js -->
	<span id="zone" class="hidden">${actionBean.current.currentZoneType}</span>
	
	<!-- Modal window for confirmation -->
	<div class="confirm2">
		<article>
            <section id="justConfirmLockEndTrip" class="hidden">
            	<c:choose>
	            	<c:when test="${actionBean.newDriverVersion}">
	                	<h2><fmt:message key="current.trip.end.trip.just.confirm.h2.v2"/></h2>
	                </c:when>
	                <c:otherwise>
	                	<h2><fmt:message key="current.trip.end.trip.just.confirm.h2"/></h2>
	                </c:otherwise>
  				</c:choose>    
  				      
                <h3><fmt:message key="current.trip.end.message"/></h3>
                
                <c:choose>
	            	<c:when test="${actionBean.newDriverVersion}">
	                	<stripes:link id="endTripWithPooling" beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="alertBtn orangered" event="endTripWithPooling" addSourcePage="true">
                    	<stripes:param name="licensePlate">${actionBean.current.licensePlate}</stripes:param>
			            <fmt:message key="current.trip.button.end.trip"/>
               			</stripes:link>
	                </c:when>
	                <c:otherwise>
	                	<stripes:link id="lockEndTrip" beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="alertBtn orangered" event="lockEndTrip" addSourcePage="true">
                    	<stripes:param name="licensePlate">${actionBean.current.licensePlate}</stripes:param>
			            <fmt:message key="current.trip.button.lock.car.end.trip"/>
               		</stripes:link>
	                </c:otherwise>
	  			</c:choose>
	                   	
                <stripes:link id="closeConfirm" href="#" class="alertBtn gray" >
                    <fmt:message key="current.trip.extend.cancel"/>
                </stripes:link>
            </section>
			<section id="stateError" class="hidden">
				<h2><fmt:message key="current.trip.booking.cancelled"/></h2>
				<h3 id="title1"><fmt:message key="current.trip.booking.cancelled.text"/></h3>
				<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" event="endTrip" class="alertBtn gray" >
					<fmt:message key="current.trip.extend.ok"/>
				</stripes:link>
			</section>
			<section id="unwantedZoneError" class="hidden">
				<h2><fmt:message key="current.trip.end.trip.confirm.h2"/></h2>
				<h3 id="title1">${pageScope.warnTitle}</h3>
				
				
				<c:choose>
	            	<c:when test="${actionBean.newDriverVersion}">
	                	<stripes:link id="endTripWithPooling" beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="alertBtn orangered" event="endTripWithPooling" addSourcePage="true">
                    	<stripes:param name="licensePlate">${actionBean.current.licensePlate}</stripes:param>
			            <fmt:message key="current.trip.button.end.trip"/>
               			</stripes:link>
	                </c:when>
	                <c:otherwise>
	                	<stripes:link id="lockEndTrip" beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="alertBtn orangered" event="lockEndTrip" addSourcePage="true">
                    	<stripes:param name="licensePlate">${actionBean.current.licensePlate}</stripes:param>
			            <fmt:message key="current.trip.button.lock.car.end.trip"/>
               		</stripes:link>
	                </c:otherwise>
	  			</c:choose>
				
				
				<stripes:link id="closeConfirm" href="#" class="alertBtn gray" >
					<fmt:message key="current.trip.extend.cancel"/>
				</stripes:link>
			</section>
			<section id="forbiddenZoneError" class="hidden">
                <h2><fmt:message key="current.trip.end.trip.error.forbidden.h2"/></h2>
                <h3><fmt:message key="current.trip.end.trip.error.forbidden.h3"/></h3>
                <stripes:link id="closeConfirm" href="#" class="alertBtn gray" >
                  <fmt:message key="current.trip.end.trip.error.forbidden.close"/>
                </stripes:link>
			</section>
			<section id="unlocking" class="hidden">
				<h2><fmt:message key="current.trip.unlocking.car"/>&nbsp;&nbsp;&nbsp;<img src="${contextPath}/img/indicator.gif"/></h2>
				<h3>
					<fmt:message key="current.trip.validating.seconds">
						<fmt:param>
							<mobi:formatMobics type="milliseconds" value="${applicationScope.configuration.unlockPollingTimeoutMilliseconds}"/>
						</fmt:param>
					</fmt:message>
				</h3>
			</section>
			<section id="locking" class="hidden">
				<h2><fmt:message key="current.trip.locking.car"/>&nbsp;&nbsp;&nbsp;<img src="${contextPath}/img/indicator.gif"/></h2>
				<h3>
					<fmt:message key="current.trip.validating.seconds">
						<fmt:param>
							<mobi:formatMobics type="milliseconds" value="${applicationScope.configuration.lockEndPollingTimeoutMilliseconds}"/>
						</fmt:param>
					</fmt:message>
				</h3>
			</section>
		</article>
	</div>
		
</t:main>