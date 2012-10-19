<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page">
	<fmt:message key='find.car.later.title' />
</c:set>

<t:main title="${title}">
	<div class="globalErrors">
		<stripes:errors/>
	</div>
	<article>
		<section>
			<nav class="simpleList">
				<ul>
					<li>
						<stripes:link
							beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" 
							id="nearestCar"  name="nearestCarBook" event="nearestCarBook" addSourcePage="true">
							<fmt:message key="nearest.car.title" />
						</stripes:link>
					</li>
					<li>
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
						<input type="hidden" name="_eventName" value="searchCars"/>
						<stripes:hidden id="latitude" name="latitude"/>
						<stripes:hidden id="longitude" name="longitude"/>
						<stripes:hidden name="orderBy" value="DISTANCE"/>
						<ul>
							<li>
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
							<li class="filter">
								<span><fmt:message key="book.now.search.class"/></span>
								<span class="customComboBox">
									<stripes:select name="clazz" value="">
										<stripes:options-enumeration enum="com.criticalsoftware.mobics.presentation.util.CarClazz" sort="clazz" />
									</stripes:select>
								</span>
							</li>
							<li class="filter">
								<span><fmt:message key="book.now.search.fuel"/></span>
								<span class="customComboBox">
									<stripes:select name="fuel" value="">
										<stripes:options-enumeration enum="com.criticalsoftware.mobics.presentation.util.FuelType" sort="type" />
									</stripes:select>
								</span>
							</li>
						</ul>
					</stripes:form>  
				</nav>
			</section>
	</article>

</t:main>

