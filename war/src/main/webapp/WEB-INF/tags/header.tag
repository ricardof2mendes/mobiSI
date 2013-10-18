<%@tag description="Header template" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp" %>
<%@attribute name="title" required="true" %>

<header class="${actionBean.headerStyle}">
	<a href="#" class="menuBtn"></a>	
	<h1>${title}</h1>
</header>

<nav id="menu" class="hidden">
	<ul>
		<li class="${actionBean.activeMenu == 'booking' ? 'bookingActive' : 'booking'}">
			<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.HomeActionBean">
				<fmt:message key="home.title"/>
			</stripes:link>
		</li>
		<li class="${actionBean.activeMenu == 'trip' ? 'tripActive' : 'trip'}">
			<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean">
				<fmt:message key="current.trip.title"/>
			</stripes:link>
		</li>
		<li class="${actionBean.activeMenu == 'recent' ? 'recentActive' : 'recent'}">
			<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.recent.RecentActivitiesActionBean">
				<fmt:message key="recent.title"/>
			</stripes:link>
		</li>
		
		<li class="${actionBean.activeMenu == 'messages' ? 'messagesActive' : 'messages '}">
			<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.messages.MessagesActionBean">
				<fmt:message key="messages.title"/>
			</stripes:link>
		</li>

        <li class="${actionBean.activeMenu == 'bonus' ? 'bonusActive' : 'bonus'}">
            <stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.bonus.BonusAccountActionBean">
                <fmt:message key="bonus.account.title"/>
            </stripes:link>
        </li>

        <!--li class="${actionBean.activeMenu == 'addons' ? 'addonsActive' : 'addons'}">
            <stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.addons.AddOnsActionBean">
                <fmt:message key="add.ons.title"/>
            </stripes:link>
        </li-->

        <li class="${actionBean.activeMenu == 'promotions' ? 'promotionsActive' : 'promotions'}">
            <stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.promotions.PromotionsActionBean">
                <fmt:message key="promotions.title"/>
            </stripes:link>
        </li>

		<li class="${actionBean.activeMenu == 'contacts' ? 'contactsActive' : 'contacts'}">
			<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.contacts.ContactsAndDamageReportActionBean">
				<fmt:message key="contacts.title"/>
			</stripes:link>
		</li>

        <li class="${actionBean.activeMenu == 'preferences' ? 'preferencesActive' : 'preferences'}">
			<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.preferences.EditPreferencesActionBean">
				<fmt:message key="preferences.title"/>
			</stripes:link>
		</li>

		<li class="${actionBean.activeMenu == 'account' ? 'accountActive' : 'account'}">
			<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.account.AccountActionBean">
				<fmt:message key="account.title"/>
			</stripes:link>
		</li>
	</ul>
</nav>

<div class="bottomShadow"></div>