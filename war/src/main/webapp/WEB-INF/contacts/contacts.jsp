<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='contacts.title' /></c:set>

<t:main title="${title}">

	<jsp:include page="/WEB-INF/common/message_error.jsp"/>

	<article>
		<section>
			<nav class=panel>
				<ul>
					<li class="imageCarClub">
						<div>
							<div>
								<div class="${actionBean.contactImageStyle}">
									<img src="${contextPath}/contacts/ContactsAndDamageReport.action?_eventName=carClubImage" />
								</div>
							</div>
							<div>${actionBean.context.user.carClub.carClubName}</div>
						</div>
					</li>
				
					<li class="link">
						<a href="tel:${actionBean.context.user.carClub.carClubContactPhone}">
							<span><fmt:message key="contacts.phone"/></span>
							<span class="ellipsis">${actionBean.context.user.carClub.carClubContactPhone}</span>
						</a>
					</li>
					<li class="link">
						<a href="mailto:${actionBean.context.user.carClub.carClubContactEmail}">
							<span><fmt:message key="contacts.email"/></span>
							<span class="ellipsis">${actionBean.context.user.carClub.carClubContactEmail}</span>
						</a>
					</li>
					<li class="link">
						<stripes:link href="${actionBean.context.user.carClub.carClubWebSiteURL}" target="_blank">
							<span><fmt:message key="contacts.web"/></span>
							<span class="ellipsis">${actionBean.context.user.carClub.carClubWebSiteURL}</span>
						</stripes:link>
					</li>
				</ul>
			</nav>
		</section>
		<stripes:link id="showPin" beanclass="com.criticalsoftware.mobics.presentation.action.contacts.ContactsAndDamageReportActionBean" class="linkBtn gray" event="damageReport">
			<fmt:message key="contacts.report.car"/>
		</stripes:link>
	</article>	
</t:main>