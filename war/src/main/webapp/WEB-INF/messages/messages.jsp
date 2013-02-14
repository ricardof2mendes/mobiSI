<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page">
	<fmt:message key='messages.header.title'>
		<fmt:param value="${actionBean.size}"/>
	</fmt:message>
</c:set>

<t:main title="${title}">
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
	
	<article class="bodyWhite">
		<c:choose>
			<c:when test="${not empty actionBean.messages}">
				<section>
					<nav class="threeColumnList">
						<ul>
							<c:forEach items="${actionBean.messages}" var="msg">
								<li>
									<stripes:link class="messageBook" beanclass="com.criticalsoftware.mobics.presentation.action.messages.MessagesActionBean" event="read" addSourcePage="true">
										<stripes:param name="licensePlate" value="${msg.carPlate}"/>
										<stripes:param name="code" value="${msg.code}"/>
										
										<div class="msg-read-${msg.isRead}"></div>
										
										<div class="msg-body">
											<div>
												<span><fmt:message key="messages.car.available"/></span>
												<span>${msg.carName},&nbsp;<fmt:message key="FuelType.${msg.fuelType.name}"/></span>
												<span><mobi:formatMobics type="currencySymbol" value="${msg.priceInUse}"/></span>
											</div>
											<div>
												<span>
													<fmt:formatDate value="${msg.timestamp.time}" pattern="${applicationScope.configuration.timePattern}"/>
												</span>
												<span></span>
											</div>
										</div>
									</stripes:link>
								</li>
							</c:forEach>
						</ul>	
					</nav>
				</section>
			</c:when>
			<c:otherwise>
				<section id="noresults">
					<label><fmt:message key="messages.no.results.found"/></label>
				</section>
			</c:otherwise>
		</c:choose>
	</article>
</t:main>