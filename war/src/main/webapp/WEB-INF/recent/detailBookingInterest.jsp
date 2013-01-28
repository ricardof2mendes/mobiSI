<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='interest.detail.title' /></c:set>

<t:main title="${title}">
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
	<article>
		<section>
			<h2><fmt:message key="interest.details.notify.title"/></h2>
			<nav class="panel">
				<ul>
					<li class="detail white">
						<span>
							<fmt:message key="interest.details.date.time"/>
						</span>
						<span>
							<fmt:formatDate value="${actionBean.booking.pickupDate.time}" pattern="${applicationScope.configuration.dateTimePattern}"/>
						</span>
					</li>
				</ul>
			</nav>
		</section>
	
		<section>
			<nav class="panel">
				<ul>
					<li class="detail white">
						<span>
							<fmt:message key="interest.details.location"/>
						</span>
						<span>
							${actionBean.booking.locationName}
						</span>
					</li>
					<li class="detail white">
						<span>
							<fmt:message key="interest.details.max.distance"/>
						</span>
						<span>
							<c:choose>
								<c:when test="${actionBean.booking.radius != applicationScope.configuration.anyDistance}">
									<mobi:formatMobics value="${actionBean.booking.radius}" type="distance"/>
								</c:when>
								<c:otherwise>
									<fmt:message key="interest.details.any.radius"/>
								</c:otherwise>
							</c:choose>
						</span>
					</li>
				</ul>			 
			</nav>
		</section>
		<section>
			<nav class="panel">
				<ul>
					<li class="detail white">
						<span>
							<fmt:message key="interest.details.car.class"/>
						</span>
						<span>
							<c:choose>
								<c:when test="${empty actionBean.booking.carClass}"><fmt:message key="CarClazz.NOT_SPECIFIED"/></c:when>
								<c:otherwise>${actionBean.booking.carClass}</c:otherwise>
							</c:choose>
						</span>
					</li>
					<li class="detail white">
						<span>
							<fmt:message key="interest.details.from.my.car.club"/>
						</span>
						<span>
							<fmt:message key="application.value.${actionBean.booking.carClubCarsOnly}"/>
						</span>
					</li>
				</ul>
			</nav>
		</section>
		<section>
			<h2><fmt:message key="interest.details.notifications"/></h2>
			<nav class="panel">
				<ul>
					<li class="detail white">
						<span>
							<fmt:message key="interest.details.start.sending"/>
						</span>
						<span>
							<c:choose>
								<c:when test="${actionBean.booking.notificationStartTime < 0}">
									<mobi:formatMobics value="${-(actionBean.booking.notificationStartTime)}" type="time"/>&nbsp;<fmt:message key="interest.details.before"/>
								</c:when>
								<c:otherwise>
									<fmt:message key="find.car.later.on.time"/>
								</c:otherwise>
							</c:choose>
						</span>
					</li>
					<li class="detail white">
						<span>
							<fmt:message key="interest.details.stop.sending"/>
						</span>
						<span>
							<c:choose>
								<c:when test="${actionBean.booking.notificationStopTime > 0}">
									<mobi:formatMobics value="${actionBean.booking.notificationStopTime}" type="time"/>&nbsp;<fmt:message key="interest.details.after"/>
								</c:when>
								<c:otherwise>
									<fmt:message key="find.car.later.on.time"/>
								</c:otherwise>
							</c:choose>
						</span>
					</li>
					<li class="detail white">
						<span>
							<fmt:message key="interest.details.max.number"/>
						</span>
						<span>
							${actionBean.booking.numOfNotifications}
						</span>
					</li>
				</ul>
			</nav>
			<c:if test="${actionBean.booking.isEditable}">
				<div class="cleaner"></div>
				<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.EditBookingInterestActionBean" class="linkBtn gray" addSourcePage="true">
					<stripes:param name="activityCode" value="${actionBean.booking.code}"/>
					<fmt:message key="interest.details.edit.request"/>
				</stripes:link>
			</c:if>
			<c:if test="${actionBean.booking.isDeleteable}">
				<a id="openConfirm" href="#"  class="linkBtn orangered">
					<fmt:message key="interest.details.delete.request"/>
				</a>
			</c:if>
		</section>
	</article>
	
	<!-- Modal window for confirmation -->
	<div class="confirm">
		<article>
			<section>
				<h2><fmt:message key="interest.details.cancel.header"/></h2>
				<h3><fmt:message key="interest.details.cancel.header2"/></h3>
				<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.recent.RecentActivitiesActionBean" class="linkBtn orangered" event="cancelInterest" addSourcePage="true">
					<stripes:param name="activityCode" value="${actionBean.booking.code}"/>
					<fmt:message key="interest.details.delete.request"/>
				</stripes:link>
				<a id="closeConfirm" href="#" class="linkBtn gray" >
					<fmt:message key="interest.details.delete.request.cancel"/>
				</a>
			</section>
		</article>
	</div>
</t:main>