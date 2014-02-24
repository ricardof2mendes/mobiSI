<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key="account.information.contact.title" /></c:set>
<c:set var="placeholder" scope="page"><fmt:message key="account.information.placeholder"/></c:set>

<t:main title="${title}">
	
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>

	<stripes:form beanclass="com.criticalsoftware.mobics.presentation.action.account.EditInformationContactActionBean" method="post">
		<stripes:hidden name="pin"/>
		<article>
			<section>
				<nav class="panel">
					<ul>
						<li class="detail white">
							<span><fmt:message key="account.information.fullname"/></span>
							<span class="customComboBox">
								<stripes:text name="fullName" class="editable" placeholder="${placeholder}"/>
							</span>
						</li>
					</ul>
				</nav>
			</section>
			
			<section>
				<nav class="panel">
					<ul>
						<li class="detail white">
							<span><fmt:message key="account.information.countryCode"/></span>
							<span class="customComboBox">
								<stripes:select name="countryCode">
									<c:forEach items="${actionBean.countries}" var="country">
										<stripes:option value="${country.code}">${country.name}</stripes:option>
									</c:forEach>
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
							<span><fmt:message key="account.information.address"/></span>
							<span class="customComboBox">
								<stripes:text name="address" class="editable" placeholder="${placeholder}" />
							</span>
						</li>
						<li class="detail white">
							<span><fmt:message key="account.information.zipCode1"/></span>
							<span class="customComboBox">
								<stripes:text name="zipCode1" class="editable" placeholder="${placeholder}" />
							</span>

						</li>
						<li class="detail white">
							<span><fmt:message key="account.information.locality"/></span>
							<span class="customComboBox">
								<stripes:text name="locality" class="editable" placeholder="${placeholder}" />
							</span>
						</li>
					</ul>
				</nav>
			</section>
			
			<section>
				<nav class="panel">
					<ul>
						<li class="detail white">
							<span><fmt:message key="account.information.phoneNumber"/></span>
							<span class="customComboBox">
								<stripes:text name="phoneNumber" class="editable" placeholder="${placeholder}" />
							</span>
						</li>
					</ul>
				</nav>
			</section>
			
			<section>
				<nav class="panel">
					<ul>
						<li class="detail white">
							<span><fmt:message key="account.information.taxNumber"/></span>
							<span class="customComboBox">
								<stripes:text name="taxNumber" readonly="true" class="editable"
									   placeholder="${placeholder}" 
									   value="${actionBean.taxNumber }"/>
							</span>
						</li>
					</ul>
				</nav>
			</section>
			
			<section class="submit">
				<stripes:submit name="saveData" class="submitBtn gray">
					<fmt:message key="account.button.ok"/>
				</stripes:submit>		
			</section>
			<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.account.AccountActionBean" class="linkBtn orangered">
				<fmt:message key="account.button.cancel"/>
			</stripes:link>
		</article>
	</stripes:form> 
</t:main>