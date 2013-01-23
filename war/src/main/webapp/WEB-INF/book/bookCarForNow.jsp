<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page">
	<fmt:message key='book.now.title' />
</c:set>

<t:main title="${title}">
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
	<article>
		<section>
			<nav class="simpleList">
				<ul>
					<li class="nearestcarsearch">
						<stripes:link
							beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" 
							id="nearestCar"  name="nearestCarBook" event="nearestCarBook" addSourcePage="true">
							<fmt:message key="nearest.car.title" />
						</stripes:link>
					</li>
					<li class="licenseplatesearch">
						<stripes:link
							beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean"
									event="licensePlateSearch">
							<fmt:message key="license.plate.title" />
						</stripes:link>
					</li>
				</ul>
			</nav>
		</section>
		<section>
			<h2><fmt:message key="book.now.search"/></h2>
			<nav class="simpleList">
				<stripes:form id="searchForm" name="main" beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" method="get">
					<stripes:hidden name="searchImmediateInMap"/>
					<stripes:hidden id="latitude" name="latitude"/>
					<stripes:hidden id="longitude" name="longitude"/>
					<stripes:hidden name="orderBy" value="${actionBean.context.user.customerPreferencesDTO.fleetOrder[0].columnName}"/>
					<ul>
						<li class="listsearch">
							<stripes:link id="searchCarsForBook" name="searchCarsForBook" href="#">
								<fmt:message key="book.now.search.list"/>
							</stripes:link>
						</li>
						<li class="title"><fmt:message key="book.now.search.limit"/></li>
						<li class="filter">
							<span><fmt:message key="book.now.search.price"/></span>
							<span class="customComboBox">
								<stripes:select name="price" value="8">
									<stripes:option value=""><fmt:message key="book.now.search.price.any"/></stripes:option>
									<c:forEach begin="5" end="25" var="index">
										<stripes:option value="${index}" >
											<fmt:message key="book.now.search.price.label">
												<fmt:param value="${index}"/>
											</fmt:message>
										</stripes:option>
									</c:forEach>
								</stripes:select>								
							</span>
						</li>
						<li class="filter">
							<span><fmt:message key="book.now.search.distance"/></span>
							<span class="customComboBox">
								<stripes:select name="distance" value="1">
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
						<li class="filter">
							<span><fmt:message key="book.now.search.class"/></span>
							<span class="customComboBox">
								<stripes:select name="clazz">
									<stripes:option value=""><fmt:message key="CarClazz.NOT_SPECIFIED"/></stripes:option>
									<c:forEach items="${actionBean.carClasses}" var="class">
										<stripes:option value="${class.code}"><fmt:message key="CarClazz.${class.description}"/></stripes:option>
									</c:forEach>
								</stripes:select>
							</span>
						</li>
						<li class="filter">
							<span><fmt:message key="book.now.search.fuel"/></span>
							<span class="customComboBox">
								<stripes:select name="fuel">
									<stripes:options-enumeration enum="com.criticalsoftware.mobics.presentation.util.FuelType" sort="type" />
								</stripes:select>
							</span>
						</li>
					</ul>
				</stripes:form>  
			</nav>
		</section>
	</article>
	<jsp:include page="/WEB-INF/common/geolocationErrorAlert.jsp"/>
</t:main>

