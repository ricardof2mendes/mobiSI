<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>

<c:choose>
	<c:when test="${not empty actionBean.car}">
		<input type="hidden" id="carLicensePlate" name="car.licensePlate" value="${actionBean.car.licensePlate}"/>
		<input type="hidden" name="car.carBrandName" value="${actionBean.car.carBrandName}"/>
		<input type="hidden" name="car.carModelName" value="${actionBean.car.carModelName}"/>
		<input type="hidden" name="car.fuelType.name" value="${actionBean.car.fuelType.name}"/>
		
		<section>
			<nav  class="panel">
				<ul>
					<li class="imageNoLink">
						<div>
							<img class="carImage" src="${contextPath}/booking/ImmediateBooking.action?getCarImage=&licensePlate=${actionBean.car.licensePlate}&width=58&height=58" />
						</div>
						<div>
							<div>
								<span>${actionBean.car.licensePlate}</span>
								<span>${actionBean.car.carBrandName}&nbsp;${actionBean.car.carModelName}</span>
								<span><fmt:message key="FuelType.${actionBean.car.fuelType.name}"/></span>
							</div>
							<div>
								<span>
								</span>
							</div>
						</div>
								
					</li>
				</ul>	
			</nav>
		</section>
	</c:when>
	<c:otherwise>
		<section id="noresultsdamage">
			<label><fmt:message key="damage.report.no.results.found"/></label>
		</section>
	</c:otherwise>
</c:choose>
