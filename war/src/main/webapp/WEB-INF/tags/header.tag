<%@tag description="Header template" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp" %>
<%@attribute name="title" required="true" %>

<header class="${actionBean.headerStyle}">
	<a href="#" class="menuBtn"></a>	
	<h1>${title}</h1>
</header>

<div class="bottomShadow"></div>

<nav id="menu" class="hidden">
	<ul>
		<li class="booking ${actionBean.activeMenu == 'booking' ? 'bookingActive' : ''}">
			<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.HomeActionBean">
				<fmt:message key="home.title"/>
			</stripes:link>
		</li>
		<li class="trip ${actionBean.activeMenu == 'trip' ? 'tripActive' : ''}">
			<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean">
				<fmt:message key="current.trip.title"/>
			</stripes:link>
		</li>
		<li class="recent ${actionBean.activeMenu == 'recent' ? 'recentActive' : ''}">
			<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.recent.RecentActivitiesActionBean">
				<fmt:message key="recent.title"/>
			</stripes:link>
		</li>
		
		<li class="messages ${actionBean.activeMenu == 'messages' ? 'messagesActive' : ''}">
			<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.messages.MessagesActionBean">
				<fmt:message key="messages.title"/>
			</stripes:link>
		</li>
		<li class="contacts ${actionBean.activeMenu == 'contacts' ? 'contactsActive' : ''}">
			<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.contacts.ContactsAndDamageReportActionBean">
				<fmt:message key="contacts.title"/>
			</stripes:link>
		</li>
		<li class="preferences ${actionBean.activeMenu == 'preferences' ? 'preferencesActive' : ''}">
			<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.preferences.PreferencesActionBean">
				<fmt:message key="preferences.title"/>
			</stripes:link>
		</li>

		<li class="account ${actionBean.activeMenu == 'account' ? 'accountActive' : ''}">
			<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.account.AccountActionBean">
				<fmt:message key="account.title"/>
			</stripes:link>
		</li>
	</ul>
</nav>
<article id="legend" class=" hidden">
	<section>
		<nav class="simpleList">
			<ul>
				<li>
					<span><fmt:message key="location.legend.my.location"/></span>
				</li>
				
				<c:choose>
			    	<c:when test="${param.carLocation == '' || param.searchImmediateInMap == ''}">
				    	<li>
							<span><fmt:message key="location.legend.car.location"/></span>
						</li>
						<c:if test="${param.searchImmediateInMap == null}">
							<li>
								<span><fmt:message key="location.legend.regular.zone"/></span>
							</li>
							<li>
								<span><fmt:message key="location.legend.undesire.zone"/></span>
							</li>
							<li>
								<span><fmt:message key="location.legend.forbidden.zone"/></span>
							</li>
						</c:if>
			    	</c:when>
			    	<c:when test="${param.parkLocation == ''}">
			    		<li class="hidden"></li>
			    		<li>
							<span><fmt:message key="location.legend.park.zone"/></span>
						</li>
			    	</c:when>
			    	<c:otherwise></c:otherwise>
			    </c:choose>
			</ul>
		</nav>
	</section>
</article>