<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='recent.title' /></c:set>

<t:main title="${title}">

	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
	
	<article class="bodyWhite">
		<c:choose>
			<c:when test="${actionBean.recents != null}">
				<section>
					<nav class="twoColumnList">
						<ul>
							<c:forEach items="${actionBean.recents}" var="recent">
								<li>
                                    <!--Default values-->
                                    <c:set var="color" value=""/>
                                    <c:set var="display" value=""/>

									<c:choose>
										<c:when test="${recent.activityType == 'TRIP'}">
											<c:set var="eventName" value="tripDetails"/>
										</c:when>
										<c:when test="${recent.activityType == 'ADVANCE_BOOKING'}">
											<c:set var="eventName" value="advanceBookingDetails"/>
											<c:set var="display" value="display:none"/>
                                            <c:set var="color" value="color:red"/>
										</c:when>
                                        <c:when test="${recent.activityType == 'INCIDENT' || recent.activityType == 'EVENT'}">
                                            <c:set var="eventName" value="incidentDetails"/>
                                        </c:when>
                                        <c:when test="${recent.activityType == 'CREDIT'}">
                                            <c:set var="eventName" value="creditDetails"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="eventName" value="interestDetails"/>
                                            <c:set var="display" value="display:none"/>
                                            <c:set var="color" value="color:red"/>
                                        </c:otherwise>
									</c:choose>
									
									<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.recent.RecentActivitiesActionBean" event="${eventName}" addSourcePage="true">	
										<stripes:param name="activityCode" value="${recent.activityCode }"/>
                                        <stripes:param name="activityType" value="${recent.activityType}"/>

										<div>
											<span style="${color}"><fmt:message key="CustomerActivityEnum.${recent.activityType}"/></span>
											<span style="${display}">
                                                <c:choose>
                                                    <c:when test="${recent.isDebit}">-</c:when>
                                                    <c:otherwise>+</c:otherwise>
                                                </c:choose>
												<mobi:formatMobics value="${recent.costWithTax}" type="currencySymbol" />
											</span>
										</div>
										<div>
											<span>
												<mobi:formatMobics value="${recent.activityDate}" type="timeWeek"/>
											</span>
                                            <span>
                                                <span class="${recent.applicableAddon ? 'showAddOn' : ''}"></span>
                                                <span class="${recent.applicablePromotion ? 'showPromotion' : ''}"></span>
                                            </span>
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
					<label><fmt:message key="recent.history.no.activities.found"/></label>
				</section>
			</c:otherwise>
		</c:choose>
	</article>
</t:main>