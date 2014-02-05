<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key="promotions.title" /></c:set>

<t:main title="${title}">
    <c:if test="${!empty actionBean.ongoingPromotions}">
        <div class="promoTitle correctMargin">
            <fmt:message key="promotions.ongoing"/>
        </div>
        <article>
            <section>
                <nav class="threeColumnList">
                    <ul>
                        <c:forEach items="${actionBean.ongoingPromotions}" var="promo" varStatus="i">
                            <li class="${pageScope.i.index % 2 == 0 ? 'promotionsLightGray' : 'promotionsGray'}">
                                <stripes:link class="high" beanclass="com.criticalsoftware.mobics.presentation.action.promotions.PromotionsActionBean" event="detail">
                                    <stripes:param name="code" value="${promo.code}"/>
                                    <div>
                                        <img class="promotionImage" src="${contextPath}/promotions/Promotions.action?getPromotionImage=&code=${promo.code}&width=58&height=58" />
                                    </div>
                                    <div>
                                        <div>
                                            <span>${promo.name}</span>
                                            <span>
                                                <fmt:message key="promotions.discount">
                                                    <fmt:param>
                                                        <fmt:formatNumber value="${promo.discount}" pattern="${applicationScope.configuration.meterPattern}"/>
                                                    </fmt:param>
                                                </fmt:message>
                                            </span>
                                            <span>${promo.description}</span>
                                        </div>
                                        <div><span></span><span></span><span></span></div>
                                    </div>
                                </stripes:link>
                            </li>

                        </c:forEach>
                    </ul>
                </nav>
            </section>
        </article>
    </c:if>

    <c:if test="${!empty actionBean.futurePromotions}">
        <div class="promoTitle">
            <fmt:message key="promotions.future"/>
        </div>
        <article>
            <section>
                <nav class="threeColumnList">
                    <ul>
                        <c:forEach items="${actionBean.futurePromotions}" var="promo" varStatus="i">
                            <li class="${pageScope.i.index % 2 == 0 ? 'promotionsLightGray' : 'promotionsGray'}">
                                <stripes:link class="high" beanclass="com.criticalsoftware.mobics.presentation.action.promotions.PromotionsActionBean" addSourcePage="true" event="detail">
                                    <stripes:param name="code" value="${promo.code}"/>
                                    <div>
                                    <img class="promotionImage" src="${contextPath}/promotions/Promotions.action?getPromotionImage=&code=${promo.code}&width=58&height=58" />
                                    </div>
                                    <div>
                                        <div>
                                            <span>${promo.name}</span>
                                            <span>
                                                <fmt:message key="promotions.discount">
                                                    <fmt:param>
                                                        <fmt:formatNumber value="${promo.discount}" pattern="${applicationScope.configuration.meterPattern}"/>
                                                    </fmt:param>
                                                </fmt:message>
                                            </span>
                                            <span>${promo.description}</span>
                                        </div>
                                        <div><span></span><span></span><span></span></div>
                                    </div>
                                </stripes:link>
                            </li>

                        </c:forEach>
                    </ul>
                </nav>
            </section>
        </article>
    </c:if>
    <c:if test="${empty actionBean.ongoingPromotions && empty actionBean.futurePromotions}">
        <article>
            <section id="nopromotions">
                <label><fmt:message key="promotions.no.results.found"/></label>
            </section>
        </article>
    </c:if>

</t:main>