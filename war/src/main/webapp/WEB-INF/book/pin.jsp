<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='pin.title' /></c:set>

<t:main title="${title}" hideFooter="true">

	<stripes:errors/>
	
	<article class="simpleArticle">
		<section>
			<div class="carDetails">
				<label>${actionBean.car.licensePlate}</label>
				<label>${actionBean.car.carBrandName}&nbsp;${actionBean.car.carModelName}</label>
				<label>${actionBean.car.fuelType}&nbsp;(${actionBean.car.range})</label>
			</div>
		</section>
	</article>
	<article class="simpleArticle">
		<section>
			<div>
				<stripes:form id="pinForm"
							  beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" method="POST">
					<stripes:submit id="book" name="book"><fmt:message key="license.plate.button"/></stripes:submit>
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