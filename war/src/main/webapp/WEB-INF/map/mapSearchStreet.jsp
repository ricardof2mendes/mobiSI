<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='search.location.title' /></c:set>

<c:set var="placeholder" scope="page">
	<fmt:message key="interest.details.type.here"/>
</c:set>

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
					<div>
						<stripes:form beanclass="com.criticalsoftware.mobics.presentation.action.booking.BookingInterestActionBean" method="get">
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
							
							<stripes:submit name="main" class="submitBtn green">
								<fmt:message key="interest.button.ok"/>
							</stripes:submit>
						</stripes:form>
					</div>
					<div>
						<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.BookingInterestActionBean" class="linkBtn gray">
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
				<stripes:link id="linkToBeUsedInList" class="hidden" beanclass="com.criticalsoftware.mobics.presentation.action.booking.BookingInterestActionBean">
					<stripes:param name="startDate"><fmt:formatDate value="${actionBean.startDate}" pattern="${applicationScope.configuration.dateTimePattern}"/></stripes:param>
					<stripes:param name="distance" value="${actionBean.distance}"/>
					<stripes:param name="carClazz" value="${actionBean.carClazz}"/>
					<stripes:param name="fromMyCarClub" value="${actionBean.fromMyCarClub}"/>
					<stripes:param name="startSending" value="${actionBean.startSending}"/>
					<stripes:param name="stopSending" value="${actionBean.stopSending}"/>
					<stripes:param name="maxMessages" value="${actionBean.maxMessages}"/>
				</stripes:link>
				<ul id="results">
					
				</ul>
			</nav>
		</section>
	</article>
</t:map>

