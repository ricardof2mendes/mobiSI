<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page">
	<fmt:message key='messages.header.title'>
		<fmt:param value="${actionBean.size}"/>
	</fmt:message>
</c:set>

<t:main title="${title}">
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
	
	<c:choose>
		<c:when test="${not empty actionBean.messages}">
			<section>
				<nav class="threeColumnList">
					<ul>
						<c:forEach items="${actionBean.messages}" var="msg">
							<li>
								<stripes:link class="messageBook" beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" event="licensePlateBook" addSourcePage="true">
									<stripes:param name="licensePlate" value="BM-03-20"/>
									
									<div class="nosheet">
										<c:choose>
											<c:when test="${msg.isRead}">
												
											</c:when>
											<c:otherwise>
												<img src="http://www2.espares.co.za:81/Images/BlueBullet.gif" />
											</c:otherwise>
										</c:choose>	
				
									</div>
									<div>
										<div>
											<span><fmt:message key="messages.car.available"/></span>
											<span>${msg.carName}&nbsp;${msg.carPlate}</span>
											<span>${msg.code}</span>
										</div>
										<div>
											<span>
												xxx
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
</t:main>