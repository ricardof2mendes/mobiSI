<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key="license.plate.title"/></c:set>

<t:main title="${title}" hideFooter="true">
	<article class="autocomplete">
		<section>
			<div>
				<stripes:form class="licensePlate" id="licensePlateBookForm" name="licensePlateBook" 
							  beanclass="com.criticalsoftware.mobics.presentation.action.book.LicensePlateActionBean" method="GET">
					<stripes:submit id="licensePlateBook" name="licensePlateBook"><fmt:message key="license.plate.button"/></stripes:submit>
					<div>
						<input type="text" name="licensePlate" id="licensePlate" placeholder="<fmt:message key="license.plate.input" />" autocomplete="off"/>						
					</div>
					<stripes:hidden id="latitude" name="latitude"/>
					<stripes:hidden id="longitude" name="longitude"/>
				</stripes:form>			
			</div>
		</section>
	</article>
	<article id="articleContainer">
		<!-- car list here given by ajax (see javascript) -->
	</article>
</t:main>