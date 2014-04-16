<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<div id="report-button" class="report-no-cars-found hidden" >
	<article>
		<section id="noResults">
			<stripes:form beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" method="post">
				<stripes:hidden id="negatedTripLatitude" name="negatedTripLatitude" value="${param.latitude}"/>
				<stripes:hidden id="negatedTripLongitude" name="negatedTripLongitude" value="${param.longitude}"/>
				<stripes:submit name="createNegatedTrip" class="submitBtn orange">
					<fmt:message key="book.now.no.results.report.negated.trip"/>						
				</stripes:submit>	
			</stripes:form>
		</section>
	</article>
</div>