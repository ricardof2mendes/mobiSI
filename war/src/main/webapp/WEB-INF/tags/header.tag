<%@tag description="Header template" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp" %>
<%@attribute name="title" required="true" %>

<header class="${actionBean.headerStyle}">
	<a href="#" class="menuBtn"></a>	
	<h1>${title}</h1>
</header>

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

        <li class="bonus ${actionBean.activeMenu == 'bonus' ? 'bonusActive' : ''}">
            <stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.bonus.BonusAccountActionBean">
                <fmt:message key="bonus.account.title"/>
            </stripes:link>
        </li>

        <!--li class="addons ${actionBean.activeMenu == 'addons' ? 'addonsActive' : ''}">
            <stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.addons.AddOnsActionBean">
                <fmt:message key="add.ons.title"/>
            </stripes:link>
        </li-->

        <li class="promotions ${actionBean.activeMenu == 'promotions' ? 'promotionsActive' : ''}">
            <stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.promotions.PromotionsActionBean">
                <fmt:message key="promotions.title"/>
            </stripes:link>
        </li>

		<li class="contacts ${actionBean.activeMenu == 'contacts' ? 'contactsActive' : ''}">
			<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.contacts.ContactsAndDamageReportActionBean">
				<fmt:message key="contacts.title"/>
			</stripes:link>
		</li>

        <li class="preferences ${actionBean.activeMenu == 'preferences' ? 'preferencesActive' : ''}">
			<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.preferences.EditPreferencesActionBean">
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

<div class="bottomShadow"></div>