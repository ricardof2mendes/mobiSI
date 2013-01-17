<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='damage.report.title' /></c:set>
<c:set var="placeholder" scope="page"><fmt:message key="damage.report.license.placeholder"/></c:set>
<c:set var="placeholderDate" scope="page"><fmt:message key="damage.report.date.placeholder"/></c:set>
<c:set var="placeholderTextarea" scope="page"><fmt:message key="damage.report.textarea.placeholder"/></c:set>

<t:main title="${title}" addCalendar="true">

	<jsp:include page="/WEB-INF/common/message_error.jsp"/>

	<stripes:form id="damageReport" beanclass="com.criticalsoftware.mobics.presentation.action.contacts.ContactsAndDamageReportActionBean" method="post">
		<article>
			<section>
				<nav class="panel">
					<ul>
						<li class="detail white">
							<span><fmt:message key="damage.report.type"/></span>
							<span class="customComboBox">
								<stripes:select name="type" >
									<stripes:options-enumeration enum="com.criticalsoftware.mobics.presentation.util.DamageType" />								
								</stripes:select>
							</span>
						</li>
					</ul>
				</nav>
			</section>

			<section>
				<nav class="panel">
					<ul>
						<li class="detail white">
							<span><fmt:message key="damage.report.license"/></span>
							<span class="customComboBox">
								<stripes:text id="licenseReport" name="licensePlate" placeholder="${placeholder}" />																	
							</span>
						</li>
					</ul>
				</nav>
			</section>
			
			<div id="searchResults">
				<c:if test="${not empty actionBean.licensePlate}">
					<jsp:include page="/WEB-INF/contacts/carComponent.jsp"></jsp:include>
				</c:if>
			</div>
			
			<section>
				<nav class="panel">
					<ul>
						<li class="detail white">
							<span><fmt:message key="damage.report.date"/></span>
							<span class="customComboBox">
								<stripes:text  id="reportDamageDate" name="date" class="editable"
									   placeholder="${placeholderDate}" formatPattern="${applicationScope.configuration.dateTimePattern}"/>
							</span>
						</li>
						<li class="detail white">
							<span><fmt:message key="damage.report.i.assume"/></span>
							<span class="customComboBox">
								<stripes:select name="assume" >
									<stripes:option value="false"><fmt:message key="damage.report.i.assume.no"/></stripes:option>
									<stripes:option value="true"><fmt:message key="damage.report.i.assume.yes"/></stripes:option>
								</stripes:select>
							</span>
						</li>
						<li class="detail white">
							<div class="customComboBox">
								<stripes:textarea name="description" rows="5" cols="5" placeholder="${placeholderTextarea}"/>
							</div>								
						</li>
					</ul>
				</nav>
			</section>
			
			<section class="submit">
				<stripes:submit name="saveDamageReport" class="submitBtn gray">
					<fmt:message key="damage.report.save"/>
				</stripes:submit>
			</section>
		</article>
	</stripes:form> 

</t:main>

