<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page">
	<fmt:message key='find.car.later.title' />
</c:set>

<t:main title="${title}" addCalendar="true">

	<c:choose>
		<c:when test="${actionBean.fieldErrors}">
			<stripes:errors/>
		</c:when>
		<c:otherwise>
			<div class="globalErrors">
				<stripes:errors/>
			</div>
		</c:otherwise>
	</c:choose>

	<stripes:form beanclass="com.criticalsoftware.mobics.presentation.action.booking.FindCarForLaterActionBean" method="get">
		<article>
			<section>
				<h2><fmt:message key="find.car.later.notify"/></h2>
				<nav class="simpleList">
					<ul>
						<li class="filter">
							<span><fmt:message key="find.car.later.datetime"/></span>
							<span class="customComboBox">
								<input type="text" id="startDate" name="startDate" 
									   placeholder="<fmt:message key="find.car.later.datetime.placeholder"/>" 
									   value="<fmt:formatDate value="${actionBean.startDate}" pattern="${applicationScope.configuration.dateTimePattern}"/>"/>
							</span>
						</li>
					</ul>
				</nav>
			</section>
			<section>
				<nav class="simpleList">
					<ul>
						<li class="filter">
							<span><fmt:message key="find.car.later.location"/></span>
							<span class="customComboBox">
								<stripes:hidden id="latitude" name="latitude"/>
								<stripes:hidden id="longitude" name="longitude"/>
								<input type="text" id="mapLocation" name="mapLocation"
									   value="${actionBean.mapLocation}"  
									   placeholder="<fmt:message key="find.car.later.location.placeholder"/>"/>								
							</span>
						</li>
						<li class="filter">
							<span><fmt:message key="find.car.later.max.distance"/></span>
							<span class="customComboBox">
								<stripes:select name="distance">
									<stripes:option value="">
										<fmt:message key="book.now.search.distance.any"/>
									</stripes:option>
									<stripes:option value="500" >
										<fmt:message key="book.now.search.distance.label.meters"><fmt:param value="500"/></fmt:message>
									</stripes:option>
									<stripes:option value="1000" >
										<fmt:message key="book.now.search.distance.label"><fmt:param value="1"/></fmt:message>
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
				<nav class="simpleList">
					<ul>
						<li class="filter">
							<span><fmt:message key="find.car.later.car.clazz"/></span>
							<span class="customComboBox">
								<stripes:select name="carClazz">
									<stripes:options-enumeration enum="com.criticalsoftware.mobics.presentation.util.CarClazz" sort="clazz" />
								</stripes:select>																
							</span>
						</li>
						<li class="filter">
							<span><fmt:message key="find.car.later.from.my.club"/></span>
							<span class="customComboBox">
								<stripes:select name="fromMyCarClub" >
									<stripes:option value="false"><fmt:message key="find.car.later.from.my.car.club.false"/></stripes:option>
									<stripes:option value="true"><fmt:message key="find.car.later.from.my.car.club.true"/></stripes:option>
								</stripes:select>
							</span>
						</li>
					</ul>
				</nav>
			</section>
			<section>
				<h2><fmt:message key="find.car.later.notifications"/></h2>
				<nav class="simpleList">
					<ul>
						<li class="filter">
							<span><fmt:message key="find.car.later.start.sending"/></span>
							<span class="customComboBox">
								<stripes:select name="startSending">
									<stripes:option value="5">
										<fmt:message key="find.car.later.sending.minutes"><fmt:param value="5"/></fmt:message>
									</stripes:option>
									<stripes:option value="10">
										<fmt:message key="find.car.later.sending.minutes"><fmt:param value="10"/></fmt:message>
									</stripes:option>
									<stripes:option value="30">
										<fmt:message key="find.car.later.sending.minutes"><fmt:param value="30"/></fmt:message>
									</stripes:option>
									<stripes:option value="60">
										<fmt:message key="find.car.later.sending.hours"><fmt:param value="1"/></fmt:message>
									</stripes:option>
								</stripes:select>		
							</span>
						</li>
						<li class="filter">
							<span><fmt:message key="find.car.later.stop.sending"/></span>
							<span class="customComboBox">
								<stripes:select name="stopSending">
									<stripes:option value="5">
										<fmt:message key="find.car.later.sending.minutes"><fmt:param value="5"/></fmt:message>
									</stripes:option>
									<stripes:option value="10">
										<fmt:message key="find.car.later.sending.minutes"><fmt:param value="10"/></fmt:message>
									</stripes:option>
									<stripes:option value="30">
										<fmt:message key="find.car.later.sending.minutes"><fmt:param value="30"/></fmt:message>
									</stripes:option>
									<stripes:option value="60">
										<fmt:message key="find.car.later.sending.hours"><fmt:param value="1"/></fmt:message>
									</stripes:option>
								</stripes:select>
							</span>
						</li>
						<li class="filter">
							<span><fmt:message key="find.car.later.max.messages"/></span>
							<span class="customComboBox">
								<input type="number" name="maxMessages" value="${actionBean.maxMessages}" 
									   placeholder="<fmt:message key="find.car.later.max.messages.placeholder"/>"/>
							</span>
						</li>
					</ul>
				</nav>
			</section>
			
			<section class="submit">
				<stripes:submit name="createBookingInterest" class="submitBtn">
					<fmt:message key="find.car.later.save.button"/>
				</stripes:submit>
			</section>
		</article>
	</stripes:form> 

</t:main>

