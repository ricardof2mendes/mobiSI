<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>

<!-- Modal window for confirmation -->
<div class="confirm">
	<article>
		<section>
			<h2><fmt:message key="geolocation.alert.msg.error.title"/></h2>
			<h3 id="title1"><fmt:message key="geolocation.alert.msg.error1.subtitle"/></h3>
			<h3 id="title2"><fmt:message key="geolocation.alert.msg.error2.subtitle"/></h3>
			<a id="closeConfirm" href="#" class="alertBtn gray" >
				<fmt:message key="geolocation.alert.button.ok"/>
			</a>
		</section>
	</article>
</div>