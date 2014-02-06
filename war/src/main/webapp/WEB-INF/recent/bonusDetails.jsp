<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key="bonus.details.title" /></c:set>

<t:main title="${title}">
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
	
	<article>
		<section>
			<nav class="panel">
				<ul>
					<li class="detail white">
						<span>
							<fmt:message key="bonus.details.code"/>
						</span>
						<span>
							${actionBean.bonus.bonusCode}
						</span>
					</li>
					<li class="detail white">
						<span>
							<fmt:message key="bonus.details.account.origin"/>
						</span>
						<span>
                            <fmt:message key="BonusAccountOriginEnum.${actionBean.bonus.bonusOrigin.value}"/>
						</span>
					</li>
					<li class="detail white">
						<span>
							<fmt:message key="bonus.details.account.type"/>
						</span>
						<span>
                            <fmt:message key="BonusAccountTypeEnum.${actionBean.bonus.bonusType.value}"/>
						</span>
					</li>					
                    <li class="detail white">
						<span>
							<fmt:message key="bonus.details.date"/>
						</span>
						<span>
                            <fmt:formatDate value="${actionBean.bonus.expirationDate.time}" pattern="${applicationScope.configuration.datePattern}"/>
                        </span>
                    </li>
					<li class="detail white">
						<span>
							<fmt:message key="bonus.details.value"/>
						</span>
						<span>
							<mobi:formatMobics value="${actionBean.bonus.value}" type="currencySymbol"/>
						</span>
					</li>                    
				</ul>			 
			</nav>
		</section>
	</article>
</t:main>