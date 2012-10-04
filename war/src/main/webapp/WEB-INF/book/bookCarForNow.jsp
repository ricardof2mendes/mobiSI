<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page">
	<fmt:message key='book.now.title' />
</c:set>

<t:main title="${title}" hideFooter="true">
	<article>
		<section>
			<nav class="simpleList">
				<ul>
					<li><stripes:link
							id="nearestCar" href="#">
							<fmt:message key="nearest.car.title" />
						</stripes:link></li>
					<li><stripes:link
							beanclass="com.criticalsoftware.mobics.presentation.action.book.LicensePlateActionBean">
							<fmt:message key="license.plate.title" />
						</stripes:link></li>
				</ul>
			</nav>
		</section>
		<section>
			<h2><fmt:message key="book.now.search"/></h2>
			<nav class="simpleList">
				<ul>
					<li><a href="#"><fmt:message key="book.now.search.list"/></a></li>
					<li class="title"><fmt:message key="book.now.search.limit"/></li>
					<li class="filter">
						<span><fmt:message key="book.now.search.price"/></span>
						<span class="customComboBox">
							<select>
								<option>Up to €8/h</option>
								<option>Valor 2</option>
								<option>Valor 3</option>
								<option>Valor 4</option>
								<option>Valor 5</option>
								<option>Valor 6</option>
							</select>
						</span>
					</li>
					<li class="filter">
						<span><fmt:message key="book.now.search.distance"/></span>
						<span class="customComboBox">
							<select>
								<option>Within 1km</option>
								<option>Valor 2</option>
								<option>Valor 3</option>
								<option>Valor 4</option>
								<option>Valor 5</option>
								<option>Valor 6</option>
							</select>
						</span>
					</li>
					<li class="filter">
						<span><fmt:message key="book.now.search.class"/></span>
						<span class="customComboBox">
							<select>
								<option>Any class</option>
								<option>Valor 2</option>
								<option>Valor 3</option>
								<option>Valor 4</option>
								<option>Valor 5</option>
								<option>Valor 6</option>
							</select>
						</span>
					</li>
				</ul>
			</nav>
		</section>
	</article>

	<stripes:form id="geolocation"
		beanclass="com.criticalsoftware.mobics.presentation.action.book.NearestCarActionBean"
		method="GET">
		<stripes:hidden id="latitude" name="latitude" />
		<stripes:hidden id="longitude" name="longitude" />
	</stripes:form>

</t:main>

