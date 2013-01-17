<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='book.advance.title' /></c:set>
<c:set var="placeholder" scope="page"><fmt:message key="book.advance.date.placeholder"/></c:set>

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
				<nav class="panel">
					<ul>
						<li class="detail">
							<span><fmt:message key="book.advance.start.date"/></span>
							<span class="customComboBox">
								<stripes:text id="startDate" name="startDate" class="editable" 
									   placeholder="${placeholder}" formatPattern="${applicationScope.configuration.dateTimePattern}"/>
							</span>
						</li>
						<li class="detail">
							<span><fmt:message key="book.advance.end.date"/></span>
							<span class="customComboBox">
								<stripes:text  id="endDate" name="endDate" class="editable" placeholder="${placeholder}" formatPattern="${applicationScope.configuration.dateTimePattern}"/>
							</span>
						</li>
					</ul>
				</nav>
			</section>
			<section class="submit">
				<stripes:submit name="searchCarsAdvance" class="submitBtn gray">
					<fmt:message key="book.advance.search.button"/>
				</stripes:submit>
			</section>
		</article>
	</stripes:form>  
</t:main>

