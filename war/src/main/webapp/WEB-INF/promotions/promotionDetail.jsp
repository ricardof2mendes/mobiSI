<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page">${actionBean.promotion.promotionName}</c:set>

<t:main title="${title}">

    <article>
        <section>
            <nav class=panel>
                <ul>
                    <li class="imageNoLink">
                        <div>
                            <img class="carImage" src="${contextPath}/promotions/Promotions.action?getPromotionImage=&code=${actionBean.promotion.code}&width=58&height=58" />
                        </div>
                        <div>
                            <div>
                                <span>${actionBean.promotion.promotionName}</span>
                                <span>
                                    <fmt:message key="promotions.discount">
                                        <fmt:param>
                                            <fmt:formatNumber value="${actionBean.promotion.discount}" pattern="${applicationScope.configuration.meterPattern}"/>
                                        </fmt:param>
                                    </fmt:message>
                                </span>
								<span>
                                    <fmt:message key="promotions.effective.on">
                                        <fmt:param>
                                            <fmt:formatDate value="${actionBean.promotion.startDate.time}" pattern="${applicationScope.configuration.datePattern}"/>
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
						    ${actionBean.promotion.message}
                        </span>
                    </li>
                    <li class="detail">
						<span>
							<fmt:message key="promotion.discount.title"/>
						</span>
						<span>
                                <fmt:formatNumber value="${actionBean.promotion.discount}" pattern="${applicationScope.configuration.meterPattern}"/>&nbsp;%
                        </span>
                    </li>
                    <li class="detail">
						<span>
							<fmt:message key="promotion.discount.type"/>
						</span>
						<span>
                            <fmt:message key="promotion.discount.type.${actionBean.promotion.discountType}"/>
                        </span>
                    </li>
                    <li class="detail">
						<span>
							<fmt:message key="promotion.usage"/>
						</span>
						<span>
                            <c:choose>
                                <c:when test="${actionBean.promotion.usageMaxNr == 0}">
                                    <fmt:message key="promotion.usage.unlimited"/>
                                </c:when>
                                <c:otherwise>
                                    ${actionBean.promotion.usageMaxNr}
                                </c:otherwise>
                            </c:choose>
                        </span>
                    </li>
                    <li class="detail">
						<span>
							<fmt:message key="promotion.min.trip.duration"/>
						</span>
						<span>
                            <mobi:formatMobics value="${actionBean.promotion.minTripDuration}" type="time"/>
						</span>
                    </li>
                    <li class="detail">
						<span>
							<fmt:message key="promotion.start.date"/>
						</span>
						<span>
							<fmt:formatDate value="${actionBean.promotion.startDate.time}" pattern="${applicationScope.configuration.dateTimePattern}"/>
						</span>
                    </li>
                    <li class="detail">
						<span>
							<fmt:message key="promotion.end.date"/>
						</span>
						<span>
							<fmt:formatDate value="${actionBean.promotion.endDate.time}" pattern="${applicationScope.configuration.dateTimePattern}"/>
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

        <section id="promotionsDiscount">
            <label><fmt:message key="promotions.discount.message"/></label>
        </section>
    </article>

</t:main>