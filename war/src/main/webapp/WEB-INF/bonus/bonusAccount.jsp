<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key="bonus.account.title" /></c:set>

<t:main title="${title}">
	<article>
        <section>
            <nav class="orderCriteria">
                <ul class="mid">
                    <li>
                        <stripes:link class="${actionBean.orderBy == 'BALANCE' ? 'selected' : '' }"
                                      beanclass="com.criticalsoftware.mobics.presentation.action.bonus.BonusAccountActionBean" event="main">
                            <fmt:message key="bonus.account.balance"/>
                        </stripes:link>
                    </li>
                    <li>
                        <stripes:link class="${actionBean.orderBy == 'TRANSACTIONS' ? 'selected' : '' }"
                                      beanclass="com.criticalsoftware.mobics.presentation.action.bonus.BonusAccountActionBean" event="transactions">
                            <fmt:message key="bonus.account.transactions"/>
                        </stripes:link>
                    </li>
                </ul>
                <ul><!--Do not remove this empty ul--></ul>
            </nav>
        </section>
        <div class="bottomShadow2"></div>
    </article>

    <c:choose>
        <c:when test="${actionBean.orderBy == 'BALANCE'}">
            <!-- Balance -->
            <article>
                <section>
                    <nav class="simpleList">
                        <ul>
                            <li class="balance">
                                <div>
                                    <fmt:message key="bonus.account.available">
                                        <fmt:param>
                                            <c:choose>
                                                <c:when test="${!empty actionBean.balanceList.availableNow}">
                                                    <mobi:formatMobics value="${actionBean.balanceList.availableNow}" type="currencySymbol"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <mobi:formatMobics value="0.00" type="currencySymbol"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </fmt:param>
                                    </fmt:message>
                                </div>
                                <div class="redText">
                                    <fmt:message key="bonus.account.expiring.on">
                                        <fmt:param>
                                            <c:choose>
                                                <c:when test="${!empty actionBean.balanceList.expiringNext}">
                                                    <mobi:formatMobics value="${actionBean.balanceList.expiringNext}" type="currencySymbol"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <mobi:formatMobics value="0.00" type="currencySymbol"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </fmt:param>
                                        <fmt:param>
                                            <c:choose>
                                                <c:when test="${!empty actionBean.balanceList.expiringDate}">
                                                    <fmt:formatDate value="${actionBean.balanceList.expiringDate.time}" pattern="${applicationScope.configuration.datePattern}"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <fmt:formatDate value="${actionBean.today}" pattern="${applicationScope.configuration.datePattern}"/>
                                                </c:otherwise>
                                            </c:choose>

                                        </fmt:param>
                                    </fmt:message>
                                </div>
                                <div class="clear"></div>
                            </li>
                        </ul>
                    </nav>
                </section>
                <c:if test="${!empty actionBean.balanceList.futureBonuses}">
                    <section>
                        <nav class="simpleList">
                            <ul>
                                <c:forEach items="${actionBean.balanceList.futureBonuses}" var="bonus">
                                    <li class="balance">
                                        <div>
                                            <mobi:formatMobics value="${pageScope.bonus.amount}" type="currencySymbol"/>
                                        </div>
                                        <div>
                                            <fmt:message key="bonus.account.available.from">
                                                <fmt:param><fmt:formatDate value="${pageScope.bonus.startDate.time}" pattern="${applicationScope.configuration.datePattern}"/></fmt:param>
                                                <fmt:param><fmt:formatDate value="${pageScope.bonus.endDate.time}" pattern="${applicationScope.configuration.datePattern}"/></fmt:param>
                                            </fmt:message>
                                        </div>
                                        <div class="clear"></div>
                                    </li>
                                </c:forEach>
                            </ul>
                        </nav>
                    </section>
                </article>
            </c:if>
        </c:when>
        <c:otherwise>
            <c:if test="${!empty actionBean.detailedList}">
                <article class="bodyWhite">
                    <section>
                        <nav class="twoColumnList">
                            <ul>
                                <c:forEach items="${actionBean.detailedList}" var="detail">
                                    <li class="transactions">
                                        <stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.recent.RecentActivitiesActionBean" event="tripDetails" addSourcePage="true">
                                        <stripes:param name="activityCode" value="${pageScope.detail.originNumber}"/>
                                            <div>
                                                <fmt:message key="bonus.account.transaction.${pageScope.detail.type}"/>
                                            </div>
                                            <div>
                                                <mobi:formatMobics value="${pageScope.detail.amount}" type="currencySymbol"/>
                                            </div>
                                            <div class="clear"></div>
                                            <div>
                                                <mobi:formatMobics value="${pageScope.detail.date}" type="timeWeek"/>
                                            </div>
                                            <div>
                                                <mobi:formatMobics value="${pageScope.detail.balance}" type="currencySymbol"/>
                                            </div>
                                            <div class="clear"></div>
                                        </stripes:link>
                                    </li>
                                </c:forEach>
                            </ul>
                        </nav>
                    </section>
                </article>
            </c:if>
        </c:otherwise>
    </c:choose>
</t:main>