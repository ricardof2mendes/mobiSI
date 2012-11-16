<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page">
	<fmt:message key='search.location.title' />
</c:set>

<t:map title="${title}">

	<stripes:form id="streetSearch" beanclass="com.criticalsoftware.mobics.presentation.action.booking.FindCarForLaterActionBean" >
		<div class="holder">
			<div class="inputHolder">
				<input type="text" id="query" placeHolder="<fmt:message key="search.location.placeholder"/>"/>
			</div>
		</div>
	</stripes:form>
	
	<div id="map" class="olMap maplocation"></div>
	
	<div id="searchResults">
		
	</div>	
</t:map>

