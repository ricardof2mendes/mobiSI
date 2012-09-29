<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='licence.plate.title' /></c:set>

<t:main title="${title}" hideFooter="true">
	<article>
		<section>
			<stripes:form beanclass="com.criticalsoftware.mobics.presentation.action.book.LicensePlateActionBean" method="GET">
				<input type="text" name="licensePlate" id="licensePlate" class="uppercase" autofocus="autofocus"/>
				<div id="autocompleteContainer" class="completionListElement" style="display: none; max-height: 300px;"></div>
				<stripes:submit name="search">submit</stripes:submit>
			</stripes:form>			
		</section>
	</article>
</t:main>