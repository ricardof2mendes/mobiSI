<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page">
	<fmt:message key='pin.title' />
</c:set>
<c:set var="placeholder" scope="page">
	<fmt:message key="pin.customer.input" />
</c:set>

<t:main title="${title}">

	<jsp:include page="/WEB-INF/common/message_error.jsp" />

	<stripes:form
		beanclass="com.criticalsoftware.mobics.presentation.action.trip.DamageReportActionBean"
		method="POST">
		<stripes:hidden name="licensePlate" />
		<stripes:hidden name="car.licensePlate" />
		<stripes:hidden name="car.carModelName" />
		<stripes:hidden name="car.carBrandName" />
		<stripes:hidden name="car.fuelType.name" />
		<stripes:hidden name="car.fuelLevel" />
		<stripes:hidden id="carState" name="car.state" />

		<article class="pin">
			<section>
				<div>
					<label>${actionBean.car.licensePlate}</label> <label>${actionBean.car.carBrandName}&nbsp;${actionBean.car.carModelName}</label>
					<label><fmt:message
							key="FuelType.${actionBean.car.fuelType.name}" />&nbsp; <c:choose>
							<c:when test="${actionBean.car.fuelLevel != null}">(${actionBean.car.fuelLevel}%)</c:when>
							<c:otherwise>(<fmt:message
									key="application.value.not.available" />)</c:otherwise>
						</c:choose> </label>
				</div>
				<div>
					<stripes:text type="number" name="pin" id="pin"
						placeholder="${placeholder}" autocomplete="off" />
				</div>
				<div>
					<stripes:submit id="continueToTrip" name="data">
						<fmt:message key="pin.button" />
					</stripes:submit>
				</div>
			</section>
		</article>
		<div class="confirm2">
			<article>
				<section id="validating" class="hidden">
					<h2>
						<fmt:message key="current.trip.damages.unlocking.car" />
						&nbsp;&nbsp;&nbsp;<img src="${contextPath}/img/indicator.gif" />
					</h2>
					<h3>
						<fmt:message key="current.trip.validating.seconds">
							<fmt:param>
								<mobi:formatMobics type="milliseconds"
									value="${applicationScope.configuration.unlockPollingTimeoutMilliseconds}" />
							</fmt:param>
						</fmt:message>
					</h3>
				</section>
			</article>
		</div>
	</stripes:form>
</t:main>