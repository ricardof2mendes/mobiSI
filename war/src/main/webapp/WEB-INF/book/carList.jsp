<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>

<c:if test="${actionBean.cars != null}">
	<section>
		<nav class="threeColumnList">
			<ul>
				<c:forEach items="${actionBean.cars}" var="car">
					<li>
						<stripes:link beanclass="${actionBean.actionBean}" event="licensePlateBook">
							<stripes:param name="licensePlate" value="${car.licensePlate}"/>
							<stripes:param name="latitude" value="${actionBean.latitude}"/>
							<stripes:param name="longitude" value="${actionBean.longitude}"/>
							<div>
								<img src="${contextPath}/book/LicensePlate.action?getCarImage=&licensePlate=${car.licensePlate}" />
							</div>
							<div>
								<div>
									<span>${car.licensePlate}</span>
									<span>${car.carBrandName}&nbsp;${car.carModelName}</span>
									<span>${car.fuelType}&nbsp;(${car.range})</span>
								</div>
								<div>
									<span>€${car.priceInUse}/h</span>
									<span>?m</span>
								</div>
							</div>
						</stripes:link>
					</li>
				</c:forEach>
			</ul>	
		</nav>
	</section>
</c:if>
