<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='pin.title' /></c:set>

<t:main title="${title}">

	<stripes:errors/>
	
	<article class="pin">
		<section>
			<div>
				<label>${actionBean.car.licensePlate}</label>
				<label>${actionBean.car.carBrandName}&nbsp;${actionBean.car.carModelName}</label>
				<label>${actionBean.car.fuelType}&nbsp;
					<c:choose>
						<c:when test="${actionBean.car.range != null}">(${actionBean.car.range})</c:when>
						<c:otherwise>(<fmt:message key="application.value.not.available"/>)</c:otherwise>
					</c:choose>
				</label>					
			</div>
		</section>
	</article>
	<article class="pin">
		<section>
			<div>
				<stripes:form id="pinForm"
							  beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" method="POST">
					<stripes:submit id="book" name="book"><fmt:message key="pin.button"/></stripes:submit>
					<div>
						<input type="number" name="pin" id="pin" maxlength="4" placeholder="<fmt:message key="pin.customer.input" />" autocomplete="off"/>						
					</div>
					<stripes:hidden name="licensePlate"/>
					<stripes:hidden name="car.licensePlate"/>
					<stripes:hidden name="car.carModelName"/>
					<stripes:hidden name="car.carBrandName"/>
					<stripes:hidden name="car.fuelType"/>
					<stripes:hidden name="car.range"/>
				</stripes:form>			
			</div>	
		</section>
	</article>
</t:main>