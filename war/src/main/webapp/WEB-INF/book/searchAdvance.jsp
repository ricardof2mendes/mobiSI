<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page">
	<fmt:message key='book.advance.title' />
</c:set>

<t:main title="${title}" addCalendar="true">
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>

	<stripes:form beanclass="com.criticalsoftware.mobics.presentation.action.booking.AdvanceBookingActionBean" method="get">
		<article>
			<section>
				<h2><fmt:message key="book.advance.search"/></h2>
				<nav class="simpleList">
					<ul>
						<li class="filter">
							<span><fmt:message key="book.advance.location"/></span>
							<span class="customComboBox">
								<stripes:select id="location" name="location">
									<stripes:options-collection collection="${actionBean.locations}" value="code" label="name"/>
								</stripes:select>								
							</span>
						</li>
						<li class="filter">
							<span><fmt:message key="book.advance.zone"/></span>
							<span class="customComboBox">
								<stripes:select id="zone" name="zone">
									<stripes:option value=""><fmt:message key="book.advance.zone.any"/></stripes:option>
								</stripes:select>
							</span>
						</li>
					</ul>
				</nav>
			</section>
			<section>
				<nav class="simpleList">
					<ul>
						<li class="filter">
							<span><fmt:message key="book.advance.start.date"/></span>
							<span class="customComboBox">
								<input type="text" id="startDate" name="startDate" 
									   placeholder="<fmt:message key="book.advance.date.placeholder"/>" 
									   value="<fmt:formatDate value="${actionBean.startDate}" pattern="${applicationScope.configuration.dateTimePattern}"/>"/>
							</span>
						</li>
						<li class="filter">
							<span><fmt:message key="book.advance.end.date"/></span>
							<span class="customComboBox">
								<input type="text" id="endDate" name="endDate" 
									   placeholder="<fmt:message key="book.advance.date.placeholder"/>" 
									   value="<fmt:formatDate value="${actionBean.endDate}" pattern="${applicationScope.configuration.dateTimePattern}"/>"/>
							</span>
						</li>
					</ul>
				</nav>
			</section>
			<section class="submit">
				<stripes:submit name="searchCarsAdvance" class="submitBtn">
					<fmt:message key="book.advance.search.button"/>
				</stripes:submit>
			</section>
		</article>
	</stripes:form>  
</t:main>

