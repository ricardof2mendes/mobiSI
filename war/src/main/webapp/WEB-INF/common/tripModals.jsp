<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
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

<!-- Modal window for confirmation -->
<div class="confirm2">
	<article>
           <section id="justConfirmLockEndTrip" class="hidden">
            <h2><fmt:message key="current.trip.end.trip.just.confirm.h2"/></h2>
			<br />
              	<stripes:link id="lockEndTrip" beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="alertBtn orangered" event="lockEndTrip" addSourcePage="true">
                 	<stripes:param name="licensePlate">${actionBean.current.licensePlate}</stripes:param>
            	<c:choose>
					<c:when test="${actionBean.current.neverStarted}">	
						<fmt:message key="current.trip.button.end.trip"/>
					</c:when>
					<c:otherwise>
						<fmt:message key="current.trip.button.lock.car.end.trip"/>
					</c:otherwise>
				</c:choose>
            	</stripes:link>
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
               <stripes:link id="lockEndTripUnwanted" beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="alertBtn orangered" event="lockEndTrip" addSourcePage="true">
                  	<stripes:param name="licensePlate">${actionBean.current.licensePlate}</stripes:param>
					<c:choose>
						<c:when test="${actionBean.current.neverStarted}">	
							<fmt:message key="current.trip.button.end.trip"/>
						</c:when>
						<c:otherwise>
							<fmt:message key="current.trip.button.lock.car.end.trip"/>
						</c:otherwise>
					</c:choose>
             		</stripes:link>
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
			<c:choose>
				<c:when test="${actionBean.current.neverStarted}">	
					<h2><fmt:message key="current.trip.locking.car"/>&nbsp;&nbsp;&nbsp;<img src="${contextPath}/img/indicator.gif"/></h2>
				</c:when>
				<c:otherwise>
					<h2><fmt:message key="current.trip.locking.car.started"/>&nbsp;&nbsp;&nbsp;<img src="${contextPath}/img/indicator.gif"/></h2>
				</c:otherwise>
			</c:choose>
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