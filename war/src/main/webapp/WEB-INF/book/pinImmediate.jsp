<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='pin.title' /></c:set>
<c:set var="placeholder" scope="page"><fmt:message key="pin.customer.input" /></c:set>

<t:main title="${title}">

	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
	
	<stripes:form beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" method="POST">
	  	<stripes:hidden name="licensePlate"/>
		<stripes:hidden name="car.licensePlate"/>
		<stripes:hidden name="car.carModelName"/>
		<stripes:hidden name="car.carBrandName"/>
		<stripes:hidden name="car.fuelType.name"/>
		<stripes:hidden name="car.fuelLevel"/>
		
		<article class="pin">
			<section>
				<div>
					<label>${actionBean.car.licensePlate}</label>
					<label>${actionBean.car.carBrandName}&nbsp;${actionBean.car.carModelName}</label>
					<label><fmt:message key="FuelType.${actionBean.car.fuelType.name}"/>&nbsp;

						<c:choose>
							<c:when test="${actionBean.car.fuelLevel != null}">(${actionBean.car.fuelLevel}%)</c:when>
							<c:otherwise>(<fmt:message key="application.value.not.available"/>)</c:otherwise>
						</c:choose>
					</label>					
				</div>
				<div>
					<stripes:text type="number" name="pin" id="pin" placeholder="${placeholder}" autocomplete="off"/>							
				</div>
				<div>
					<stripes:submit id="book" name="book"><fmt:message key="pin.button"/></stripes:submit>
				</div>
			</section>
		</article>
	</stripes:form>			
</t:main>