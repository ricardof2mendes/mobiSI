<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='recent.title' /></c:set>

<t:main title="${title}">
	<stripes:messages/>
	
	<article>
		<c:choose>
			<c:when test="${actionBean.recents != null}">
				<section>
					<nav class="twoColumnList">
						<ul>
							<c:forEach items="${actionBean.recents}" var="recent">
								<li>
									<c:choose>
										<c:when test="${recent.activityType == 'TRIP'}">
											<c:set var="eventName" value="tripDetails"/>
											<c:set var="color" value=""/>
											<c:set var="display" value=""/>
										</c:when>
										<c:when test="${recent.activityType == 'ADVANCE_BOOKING'}">
											<c:set var="eventName" value="bookingDetails"/>
											<c:set var="color" value="color:red"/>
											<c:set var="display" value="display:none"/>
										</c:when>
										<c:otherwise>
											<c:set var="eventName" value="interestDetails"/>
											<c:set var="color" value="color:red"/>
											<c:set var="display" value="display:none"/>
										</c:otherwise>
									</c:choose>
									
									<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.recent.RecentActivitiesActionBean" event="${eventName}" addSourcePage="true">	
										<stripes:param name="activityCode" value="${recent.activityCode }"/>
										<div>
											<span style="${color}"><fmt:message key="CustomerActivityEnum.${recent.activityType}"/></span>
											<span style="${display}">
												<fmt:message key="recent.cost"/>:&nbsp;
												<mobi:formatMobics value="${recent.cost}" type="currencySymbol" />
											</span>
										</div>
										<div>
											<span>
												<mobi:formatMobics value="${recent.activityDate}" type="timeWeek"/>
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