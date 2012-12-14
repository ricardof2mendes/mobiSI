<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page">
	<fmt:message key='book.now.search.results' />
</c:set>

<t:map title="${title}">
	<div id="container" class="white">
		<div id="map"></div>
		<section id="whiteBar">
			<nav class="threeColumnList">
				<ul class="hidden">
					<li>
						<a href="${contextPath}/booking/ImmediateBooking.action?licensePlateBook=">
							<div>
								<img src="" />
							</div>
							<div>
								<div>
									<span id="licensePlate"></span>
									<span id="carBrandName"></span>
									<span id="fuelType"></span>
								</div>
								<div>
									<span id="priceInUse">
										<%--mobi:formatMobics value="" type="currencyHour" /--%>
									</span>
									<span id="distance">
										<%--mobi:formatMobics value="${car.distance}" type="distance"  /--%>
									</span>
								</div>
							</div>
						</a>
					</li>
				</ul>
			</nav>
		</section>
	</div>
</t:map>

