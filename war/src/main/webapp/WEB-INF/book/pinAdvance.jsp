<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='pin.title' /></c:set>
<c:set var="placeholder" scope="page"><fmt:message key="pin.customer.input" /></c:set>

<t:main title="${title}">

	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
	
	<stripes:form id="pinForm" beanclass="com.criticalsoftware.mobics.presentation.action.booking.AdvanceBookingActionBean" method="POST">
		<stripes:hidden name="licensePlate"/>
		<stripes:hidden name="car.licensePlate"/>
		<stripes:hidden name="car.carModelName"/>
		<stripes:hidden name="car.carBrandName"/>
		<input type="hidden" name="car.zones" value="${actionBean.car.zones[0].zone}"/>
		
		<input type="hidden" name="startDate" value="<fmt:formatDate value="${actionBean.startDate}" pattern="${applicationScope.configuration.dateTimePattern}"/>"/>
		<input type="hidden" name="endDate" value="<fmt:formatDate value="${actionBean.endDate}" pattern="${applicationScope.configuration.dateTimePattern}"/>"/>
	
		<article class="pin">
			<section>
				<div>
					<label>${actionBean.car.licensePlate}</label>
					<label>${actionBean.car.carBrandName}&nbsp;${actionBean.car.carModelName}</label>
					
					<c:choose>
						<c:when test="${actionBean.car.zones != null}"><label>${actionBean.car.zones[0].zone}</label></c:when>
						<c:otherwise><label><fmt:message key="application.value.not.available"/></label></c:otherwise>
					</c:choose>
					
					<label>
						<fmt:message key="pin.dates">
							<fmt:param>
								<fmt:formatDate value="${actionBean.startDate}" pattern="${applicationScope.configuration.dateTimePattern}"/>
							</fmt:param>
							<fmt:param>
								<fmt:formatDate value="${actionBean.endDate}" pattern="${applicationScope.configuration.dateTimePattern}"/>
							</fmt:param>
						</fmt:message>
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