<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key="license.plate.title"/></c:set>
<c:set var="placeholder" scope="page"><fmt:message key="license.plate.input" /></c:set>

<t:main title="${title}">
	
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
	
	<article id="licsearch" class="simpleArticle">
		<section>
			<stripes:form id="licensePlateBookForm" 
						  beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" method="GET">
				<div>
					<stripes:text name="licensePlate" id="licensePlate" placeholder="${placeholder}" autocomplete="off" />
					<div></div>						
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