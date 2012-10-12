<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key="license.plate.title"/></c:set>

<t:main title="${title}" hideFooter="true">
	<stripes:errors/>
	<article class="simpleArticle">
		<section>
			<stripes:form class="licensePlate" id="licensePlateBookForm" name="licensePlateBook" 
						  beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" method="GET">
				<div>
					<input type="text" name="licensePlate" id="licensePlate" placeholder="<fmt:message key="license.plate.input" />" autocomplete="off"/>						
					<stripes:hidden id="latitude" name="latitude"/>
					<stripes:hidden id="longitude" name="longitude"/>
				</div>
			</stripes:form>			
		</section>
	</article>
	<article id="articleContainer">
		<!-- car list here given by ajax (see javascript) -->
	</article>
</t:main>