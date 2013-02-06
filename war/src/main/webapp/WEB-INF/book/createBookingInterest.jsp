<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='find.car.later.title' /></c:set>
<c:set var="dateplaceholder" scope="page"><fmt:message key="interest.details.datetime.placeholder"/></c:set>

<c:set var="currentLocationMessage" scope="page">
	<fmt:message key="interest.details.current.location"/>
</c:set>
<c:set var="sendToMapAddress" scope="page">
	<c:choose>
		<c:when test="${currentLocationMessage != actionBean.address}">${actionBean.address}</c:when>
		<c:otherwise></c:otherwise>
	</c:choose>
</c:set>

<t:main title="${title}" addCalendar="true">

	<jsp:include page="/WEB-INF/common/message_error.jsp"/>

	<stripes:form id="interestBook" beanclass="com.criticalsoftware.mobics.presentation.action.booking.CreateBookingInterestActionBean" method="post">
		<stripes:hidden id="latitude" name="latitude"/>
		<stripes:hidden id="longitude" name="longitude"/>
		<stripes:hidden id="address" name="address"/>
		
		<article>
			<section>
				<h2><fmt:message key="find.car.later.notify"/></h2>
				<nav class="panel">
					<ul>
						<li class="detail white">
							<span><fmt:message key="find.car.later.datetime"/></span>
							<span class="customComboBox">
								<stripes:text id="startDate" name="startDate" value="${actionBean.startDate}" class="editable" placeholder="${dateplaceholder}" formatPattern="${applicationScope.configuration.dateTimePattern}"/>
							</span>
						</li>
					</ul>
				</nav>
			</section>
			<section>
				<nav class="panel">
					<ul>
						<li class="link white">
							<stripes:link id="locationLink" beanclass="com.criticalsoftware.mobics.presentation.action.booking.BookingInterestActionBean" event="searchLocation" addSourcePage="true">
								<stripes:param name="query">${sendToMapAddress}</stripes:param>
								<span><fmt:message key="find.car.later.location"/></span>
								<span id="addressSpan">
									<c:choose>
										<c:when test="${not empty actionBean.address}">${actionBean.address}</c:when>
										<c:when test="${not empty actionBean.latitude}">${currentLocationMessage}</c:when>
										<c:otherwise><fmt:message key="find.car.later.please.choose"/></c:otherwise>
									</c:choose>
								</span>
							</stripes:link>
						</li>
						<li class="detail white">
							<span><fmt:message key="find.car.later.max.distance"/></span>
							<span class="customComboBox">
								<stripes:select id="distance" name="distance">
									<stripes:option value="${applicationScope.configuration.anyDistance}">
										<fmt:message key="book.now.search.distance.any"/>
									</stripes:option>
									<stripes:option value="500" >
										<fmt:message key="book.now.search.distance.label.meters"><fmt:param value="500"/></fmt:message>
									</stripes:option>
									<stripes:option value="1000" >
										<fmt:message key="book.now.search.distance.label"><fmt:param value="1"/></fmt:message>
									</stripes:option>
									<stripes:option value="2000" >
										<fmt:message key="book.now.search.distance.label"><fmt:param value="2"/></fmt:message>
									</stripes:option>
									<stripes:option value="3000" >
										<fmt:message key="book.now.search.distance.label"><fmt:param value="3"/></fmt:message>
									</stripes:option>
								</stripes:select>
							</span>
						</li>
					</ul>
				</nav>
			</section>
			<section>
				<nav class="panel">
					<ul>
						<li class="detail white">
							<span><fmt:message key="find.car.later.car.clazz"/></span>
							<span class="customComboBox">
								<stripes:select id="carClazz" name="carClazz">
									<stripes:option value=""><fmt:message key="CarClazz.NOT_SPECIFIED"/></stripes:option>
									<c:forEach items="${actionBean.carClasses}" var="class">
										<stripes:option value="${class.code}">${class.description}</stripes:option>
									</c:forEach>
								</stripes:select>																
							</span>
						</li>
						<c:if test="${not actionBean.context.user.carClub.isStandalone}">
							<li class="detail white">
								<span></span>
								<span class="customComboBox">
									<stripes:checkbox id="fromMyCarClub" name="fromMyCarClub" />&nbsp;<fmt:message key="find.car.later.from.my.club"/>
								</span>
							</li>
						</c:if>
					</ul>
				</nav>
			</section>
			<section>
				<h2><fmt:message key="find.car.later.notifications"/></h2>
				<nav class="panel">
					<ul>
						<li class="detail white">
							<span><fmt:message key="find.car.later.start.sending"/></span>
							<span class="customComboBox">
								<stripes:select id="startSending" name="startSending">
									<stripes:option value="-7200">
										<fmt:message key="find.car.later.start.sending.hours"><fmt:param value="2"/></fmt:message>
									</stripes:option>
									<stripes:option value="-3600">
										<fmt:message key="find.car.later.start.sending.hour"><fmt:param value="1"/></fmt:message>
									</stripes:option>
									<stripes:option value="-1800">
										<fmt:message key="find.car.later.start.sending.minutes"><fmt:param value="30"/></fmt:message>
									</stripes:option>
									<stripes:option value="-900">
										<fmt:message key="find.car.later.start.sending.minutes"><fmt:param value="15"/></fmt:message>
									</stripes:option>
									<stripes:option value="-300">
										<fmt:message key="find.car.later.start.sending.minutes"><fmt:param value="5"/></fmt:message>
									</stripes:option>
									<stripes:option value="0">
										<fmt:message key="find.car.later.on.time"/>
									</stripes:option>
								</stripes:select>		
							</span>
						</li>
						<li class="detail white">
							<span><fmt:message key="find.car.later.stop.sending"/></span>
							<span class="customComboBox">
								<stripes:select id="stopSending" name="stopSending">
									<stripes:option value="0">
										<fmt:message key="find.car.later.on.time"/>
									</stripes:option>
									<stripes:option value="300">
										<fmt:message key="find.car.later.end.sending.minutes"><fmt:param value="5"/></fmt:message>
									</stripes:option>
									<stripes:option value="600">
										<fmt:message key="find.car.later.end.sending.minutes"><fmt:param value="10"/></fmt:message>
									</stripes:option>
									<stripes:option value="900">
										<fmt:message key="find.car.later.end.sending.minutes"><fmt:param value="15"/></fmt:message>
									</stripes:option>
									<stripes:option value="1800">
										<fmt:message key="find.car.later.end.sending.minutes"><fmt:param value="30"/></fmt:message>
									</stripes:option>
									<stripes:option value="3600">
										<fmt:message key="find.car.later.end.sending.hours"><fmt:param value="1"/></fmt:message>
									</stripes:option>
								</stripes:select>
							</span>
						</li>
						<li class="detail white">
							<span><fmt:message key="find.car.later.max.messages"/></span>
							<span class="customComboBox">
								<stripes:text type="number" id="maxMessages" name="maxMessages" 
									   placeholder="${placeholder}"/>
							</span>
						</li>
					</ul>
				</nav>
			</section>
			
			<section class="submit">
				<stripes:submit name="createBookingInterest" class="submitBtn gray">
					<fmt:message key="find.car.later.save.button"/>
				</stripes:submit>
			</section>
		</article>
	</stripes:form> 

</t:main>

