<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key="add.ons.title"/></c:set>

<t:main title="${title}">

    <article>
        <section>
            <nav class=panel>
                <ul>
                    <li class="addonHeader">
                        <div>
                            <div>
                                <span>${actionBean.addon.name}</span>
                                <span>
                                    <fmt:message key="promotions.discount">
                                        <fmt:param>
                                            <fmt:formatNumber value="${actionBean.addon.discount}" pattern="${applicationScope.configuration.meterPattern}"/>
                                        </fmt:param>
                                    </fmt:message>
                                </span>
								<span>
                                    <fmt:message key="promotions.effective.on">
                                        <fmt:param>
                                            <fmt:formatDate value="${actionBean.addon.startDate.time}" pattern="${applicationScope.configuration.datePattern}"/>
                                        </fmt:param>
                                    </fmt:message>
								</span>
                            </div>
                        </div>
                    </li>
                    <li class="detail">
						<span>
							<fmt:message key="promotions.description"/>
						</span>
						<span>
                            ${actionBean.addon.description}
                        </span>
                    </li>
                    <li class="detail">
						<span>
							<fmt:message key="promotion.discount.title"/>
						</span>
						<span>
                            <fmt:formatNumber value="${actionBean.addon.discount}" pattern="${applicationScope.configuration.meterPattern}"/>&nbsp;%
                        </span>
                    </li>
                    <li class="detail">
						<span>
							<fmt:message key="add.ons.included.km"/>
						</span>
						<span>
                             <fmt:message key="add.ons.kms">
                                <fmt:param>
                                    <fmt:formatNumber value="${actionBean.addon.includedKm/1000}" pattern="${applicationScope.configuration.kilometerPattern}"/>
                                </fmt:param>
                            </fmt:message>
                        </span>
                    </li>
                    <li class="detail">
						<span>
							<fmt:message key="add.ons.extra.km"/>
						</span>
						<span>
                             <mobi:formatMobics value="${actionBean.addon.extraKm}" type="currencySymbol"/>
                        </span>
                    </li>
                    <li class="detail">
						<span>
							<fmt:message key="add.ons.start.date"/>
						</span>
						<span>
                             <fmt:formatDate value="${actionBean.addon.startDate.time}" pattern="${applicationScope.configuration.datePattern}"/>
                        </span>
                    </li>
                    <li class="detail">
						<span>
							<fmt:message key="add.ons.end.date"/>
						</span>
						<span>
                            <fmt:formatDate value="${actionBean.addon.endDate.time}" pattern="${applicationScope.configuration.datePattern}"/>
                        </span>
                    </li>
                </ul>
            </nav>
        </section>


        <c:choose>
            <c:when test="${!empty actionBean.restrictions}">
                <section>
                    <h2><fmt:message key="promotion.restrictions.title"/></h2>
                    <nav class=panel>
                        <ul>
                            <c:forEach items="${actionBean.restrictions}" var="day">
                                <c:forEach var="rest" items="${actionBean.restrictions[pageScope.day.key]}" varStatus="status">
                                    <li class="promoRest">
                                        <span>
                                            <c:choose>
                                                <c:when test="${pageScope.status.index == 0}">
                                                    <fmt:message key="promotion.day.${pageScope.day.key}"/>
                                                </c:when>
                                                <c:otherwise>&nbsp;</c:otherwise>
                                            </c:choose>
                                        </span>
                                        <span>
                                            <fmt:message key="promotion.restriction">
                                                <fmt:param><fmt:message key="promotions.restriction.type.${pageScope.rest.type}"/></fmt:param>
                                                <fmt:param><fmt:formatDate value="${pageScope.rest.startDate.time}" pattern="${applicationScope.configuration.timePattern}"/></fmt:param>
                                                <fmt:param><fmt:formatDate value="${pageScope.rest.endDate.time}" pattern="${applicationScope.configuration.timePattern}"/></fmt:param>
                                            </fmt:message>
                                        </span>
                                    </li>
                                </c:forEach>
                            </c:forEach>
                        </ul>
                    </nav>
                </section>
            </c:when>
            <c:otherwise>
                <section id="noresultsrestrictions">
                    <label><fmt:message key="promotions.no.restrictions"/></label>
                </section>
            </c:otherwise>
        </c:choose>

        <c:if test="${!empty actionBean.addon.eventsList}">
            <section>
                <h2><fmt:message key="add.ons.events.list"/></h2>
                <nav class="panel">
                    <ul>
                        <c:forEach items="${actionBean.addon.eventsList}" var="event">
                            <li class="correctPadding detail">
                                <div>${pageScope.event.name}</div>
                                <div>
                                    <fmt:message key="add.ons.dates.to">
                                        <fmt:param>
                                            <fmt:formatDate value="${pageScope.event.startDate.time}" pattern="${applicationScope.configuration.dateTimePattern}"/>
                                        </fmt:param>
                                        <fmt:param>
                                            <fmt:formatDate value="${pageScope.event.endDate.time}" pattern="${applicationScope.configuration.dateTimePattern}"/>
                                        </fmt:param>
                                    </fmt:message>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                </nav>
            </section>

        </c:if>

        <c:if test="${!empty actionBean.addon.zonesList}">
            <section>
                <h2><fmt:message key="add.ons.restriction.zones"/></h2>
                <nav class="panel">
                    <ul>
                        <c:forEach items="${actionBean.addon.zonesList}" var="zone">
                            <li class="link">
                                <stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.addons.AddOnsActionBean" event="zoneLocation">
                                    <stripes:param name="code">${pageScope.zone.code}</stripes:param>
                                    <div>${pageScope.zone.zone}</div>
                                </stripes:link>
                            </li>
                        </c:forEach>
                    </ul>
                </nav>
            </section>
        </c:if>


        <section id="promotionsDiscount">
            <label><fmt:message key="promotions.discount.message"/></label>
        </section>
    </article>

</t:main>