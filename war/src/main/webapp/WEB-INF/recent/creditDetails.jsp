<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key="credit.title" /></c:set>

<t:main title="${title}">
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
	
	<article>
		<section>
			<nav class="panel">
				<ul>
					<li class="detail white">
						<span>
							<fmt:message key="credir.value"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.credit.amount}" type="currencySymbol"/>
						</span>
					</li>
					<li class="detail white">
						<span>
							<fmt:message key="credit.origin"/>
						</span>
						<span>
                            <fmt:message key="CreditPurchaseModeEnum.${actionBean.credit.purchaseMode.value}"/>
						</span>
					</li>

                    <c:choose>
                        <c:when test="${actionBean.credit.paymentMethod.value == 'CREDIT_CARD'}">
                            <li class="detail white">
                                <span>
                                    <fmt:message key="credit.payment"/>
                                </span>
                                <span>
                                    <fmt:message key="CreditCardTypeEnum.${actionBean.credit.creditCardType.value}">
                                        <fmt:param value="${actionBean.credit.creditCardLastFourDigits}"/>
                                    </fmt:message>
                                </span>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="detail white">
                                <span>
                                    <fmt:message key="credit.payment"/>
                                </span>
                                <span>
                                    <fmt:message key="credit.payment.of.services"/>
                                </span>
                            </li>
                            <li class="detail white">
                                <span>
                                    <fmt:message key="credit.entity"/>
                                </span>
                                <span>
                                    ${actionBean.credit.multibancoEntity}
                                </span>
                            </li>
                            <li class="detail white">
                                <span>
                                    <fmt:message key="credit.reference"/>
                                </span>
                                <span>
                                    ${actionBean.credit.multibancoReference}
                                </span>
                            </li>
                        </c:otherwise>
                    </c:choose>

                    <li class="detail white">
						<span>
							<fmt:message key="credit.date"/>
						</span>
						<span>
                            <fmt:formatDate value="${actionBean.credit.timeCreated.time}" pattern="${applicationScope.configuration.dateTimePattern}"/>
                        </span>
                    </li>
				</ul>			 
			</nav>
		</section>
	</article>
</t:main>