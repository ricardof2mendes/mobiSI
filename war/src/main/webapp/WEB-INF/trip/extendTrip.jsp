<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='current.trip.extend.title' /></c:set>
<c:set var="placeholder" scope="page"><fmt:message key="book.advance.date.placeholder"/></c:set>

<t:main title="${title}" addCalendar="true">
	
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
	
	<stripes:form beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" method="POST">
		<stripes:hidden name="bookingCode"/>
		<article>
			<section>
				<nav class="panel">
					<ul>
						<li class="detail white">
							<span><fmt:message key="car.details.start.date"/></span>
							<span>
								<fmt:formatDate value="${actionBean.current.startDate.time}" pattern="${applicationScope.configuration.dateTimePattern}"/>
							</span>
						</li>
						<li class="detail white">
							<span><fmt:message key="car.details.end.date"/></span>
							<span class="customComboBox">
							
							<c:set var="begin" scope="page">
								<fmt:formatDate value="${actionBean.current.endDate.time}" pattern="${applicationScope.configuration.dateTimePattern}"/>
							</c:set>
							<c:set var="end" scope="page">
								<fmt:formatDate value="${actionBean.extendBookingDate}" pattern="${applicationScope.configuration.dateTimePattern}"/>
							</c:set>
							
								<stripes:text id="limited" name="endDate" class="editable" 
									placeholder="${placeholder}" formatPattern="${applicationScope.configuration.dateTimePattern}"
									data-begin="${pageScope.begin}" 
									data-limit="${pageScope.end}"/>
							</span>
						</li>
					</ul>
				</nav>
			</section>
			<section class="submit">
				<div class="warningMessage">
					<c:choose>
						<c:when test="${not empty actionBean.extendBookingDate}">
							<fmt:message key="current.trip.extend.date">
								<fmt:param>
									<fmt:formatDate value="${actionBean.extendBookingDate}" pattern="${applicationScope.configuration.dateTimePattern}"/>
								</fmt:param>
							</fmt:message>
						</c:when>
						<c:otherwise>
							<fmt:message key="current.trip.extend.date.free"/>
						</c:otherwise>
					</c:choose>			
				</div>
				
				<stripes:submit name="save" class="submitBtn green"><fmt:message key="current.trip.extend.ok"/></stripes:submit>
				
				<div class="warningMessage">
				  <c:choose>
				    <c:when test="${actionBean.current.extendCost.unscaledValue() != 0}">
							<fmt:message key="trip.detail.advance.booking.edit.cost">
							<fmt:param><mobi:formatMobics value="${actionBean.current.extendCost}" type="currencySymbol"/></fmt:param>
							</fmt:message>
            </c:when>
            <c:otherwise>	
              <fmt:message key="trip.detail.advance.booking.edit.no.fees"/>
            </c:otherwise>
          </c:choose>   					
				</div>
			</section>
			<section>
				<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.trip.TripActionBean" class="linkBtn gray">
					<fmt:message key="current.trip.extend.cancel"/>
				</stripes:link>
			</section>
		</article>	
	</stripes:form>
		
</t:main>