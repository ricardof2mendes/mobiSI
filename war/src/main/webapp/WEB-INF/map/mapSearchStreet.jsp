<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='search.location.title' /></c:set>

<c:set var="placeholder" scope="page">
	<fmt:message key="interest.details.type.here"/>
</c:set>
	<c:choose>
		<c:when test="${param.edit}">
			<c:set var="beanclass" scope="page">
				<c:out value="com.criticalsoftware.mobics.presentation.action.booking.EditBookingInterestActionBean"/>
			</c:set>
			<c:set var="method" scope="page">
				<c:out value="returnToEdit"/>
			</c:set>
		</c:when>
		<c:otherwise>
			<c:set var="beanclass" scope="page">
				<c:out value="com.criticalsoftware.mobics.presentation.action.booking.CreateBookingInterestActionBean"/>
			</c:set>
			<c:set var="method" scope="page">
				<c:out value="main"/>
			</c:set>
		</c:otherwise>
	</c:choose>

<t:map title="${title}">

	<jsp:include page="/WEB-INF/common/legend.jsp"></jsp:include>
	<span id="paramAddr" class="hidden">${actionBean.query}</span>
	<span id="paramDist" class="hidden">${actionBean.distance}</span>
	<span id="paramLat" class="hidden">${actionBean.latitude}</span>
	<span id="paramLon" class="hidden">${actionBean.longitude}</span>
	<span id="paramCLM" class="hidden"><fmt:message key="interest.details.current.location"/></span>
	
	<article id="streetsearch" class="simpleArticle">
		<section>
			<stripes:form id="streetSearchForm" beanclass="com.criticalsoftware.mobics.presentation.action.booking.BookingInterestActionBean" method="get">
				<div>
					<div class="right">
						<ul>
							<li>
								<stripes:link id="resultlist"  beanclass="com.criticalsoftware.mobics.presentation.action.booking.BookingInterestActionBean" >
									<fmt:message key="interest.details.list"/>
								</stripes:link>
								<stripes:link id="resultmap" class="hidden" beanclass="com.criticalsoftware.mobics.presentation.action.booking.BookingInterestActionBean" >
									<fmt:message key="interest.details.map"/>
								</stripes:link>
							</li>
						</ul>
					</div>
					<div class="left">
						<stripes:text name="query" id="query" placeholder="${placeholder}" autocomplete="off" />
						<div></div>
					</div>
				</div>
			</stripes:form>			
		</section>
	</article>
	<div class="streetBottomShadow"></div>
	<article>
		<section>
			<div id="container" class="street">	
				<div id="map"></div>
			</div>
		</section>	
	</article>
	<article>
		<section id="whiteBar" class="addressBar">
			<nav class="hidden">
				<div class="ellipsis">
					<span id="choosenAddress"></span>
				</div>
				<div>
					<!-- Submit button -->
					<div>
						<stripes:form beanclass="${pageScope.beanclass}" method="get">
							<stripes:hidden id="latitude" name="latitude"/>
							<stripes:hidden id="longitude" name="longitude"/>
							<stripes:hidden name="startDate"/>
							<stripes:hidden name="distance"/>
							<stripes:hidden name="carClazz"/>
							<stripes:hidden name="fromMyCarClub"/>
							<stripes:hidden name="startSending"/>
							<stripes:hidden name="stopSending"/>
							<stripes:hidden name="maxMessages"/>
							<stripes:hidden id="address" name="address"/>
							<c:if test="${not empty param.activityCode}">
								<stripes:hidden name="activityCode" value="${param.activityCode}"/>
							</c:if>
							
							<stripes:submit name="${pageScope.method}" class="submitBtn green">
								<fmt:message key="interest.button.ok"/>
							</stripes:submit>
						</stripes:form>
					</div>
					<!-- Cancel button -->
					<div>
						<stripes:link beanclass="${pageScope.beanclass}" class="linkBtn gray" event="${pageScope.method}">
							<stripes:param name="latitude" value="${actionBean.latitude}"/>
							<stripes:param name="longitude" value="${actionBean.longitude}"/>
							<stripes:param name="startDate"><fmt:formatDate value="${actionBean.startDate}" pattern="${applicationScope.configuration.dateTimePattern}"/></stripes:param>
							<stripes:param name="distance" value="${actionBean.distance}"/>
							<stripes:param name="carClazz" value="${actionBean.carClazz}"/>
							<stripes:param name="fromMyCarClub" value="${actionBean.fromMyCarClub}"/>
							<stripes:param name="startSending" value="${actionBean.startSending}"/>
							<stripes:param name="stopSending" value="${actionBean.stopSending}"/>
							<stripes:param name="maxMessages" value="${actionBean.maxMessages}"/>
							<stripes:param name="address" value="${actionBean.address}"/>
							<c:if test="${not empty param.activityCode}">
								<stripes:param name="activityCode" value="${param.activityCode}"/>
							</c:if>
							<fmt:message key="interest.button.cancel"/>
						</stripes:link>
					</div>
				</div>
			</nav>
		</section>	
	</article>
	<article id="addressList" class="hidden">
		<section>
			<nav class="oneColumnList">
				<!-- Shared link to be used in by places in list display-->
				<stripes:link id="linkToBeUsedInList" class="hidden" beanclass="${pageScope.beanclass}" event="${pageScope.method}">
					<stripes:param name="startDate"><fmt:formatDate value="${actionBean.startDate}" pattern="${applicationScope.configuration.dateTimePattern}"/></stripes:param>
					<stripes:param name="distance" value="${actionBean.distance}"/>
					<stripes:param name="carClazz" value="${actionBean.carClazz}"/>
					<stripes:param name="fromMyCarClub" value="${actionBean.fromMyCarClub}"/>
					<stripes:param name="startSending" value="${actionBean.startSending}"/>
					<stripes:param name="stopSending" value="${actionBean.stopSending}"/>
					<stripes:param name="maxMessages" value="${actionBean.maxMessages}"/>
					<c:if test="${not empty param.activityCode}">
						<stripes:param name="activityCode" value="${param.activityCode}"/>
					</c:if>
				</stripes:link>
				<ul id="results">
					
				</ul>
			</nav>
		</section>
	</article>
	
	<!-- Modal window for no results -->
	<div class="confirm2">
		<article>
			<section id="noResults" class="hidden">
				<h2><fmt:message key="find.car.later.no.results.found.header"/></h2>
				<h3 id="title1"><fmt:message key="find.car.later.no.results.found"/></h3>
				<stripes:link id="closeConfirm" href="#" class="alertBtn gray" >
					<fmt:message key="find.car.later.no.results.ok"/>
				</stripes:link>
			</section>
		</article>
	</div>
</t:map>

