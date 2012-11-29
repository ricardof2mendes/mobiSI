<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key="account.authentication.email.login.title" /></c:set>
<c:set var="placeholder" scope="page"><fmt:message key="account.authentication.email.placeholder"/></c:set>
<c:set var="placeholderConfirm" scope="page"><fmt:message key="account.authentication.email.confirm.placeholder"/></c:set>

<t:main title="${title}">
	
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>

	<stripes:form beanclass="com.criticalsoftware.mobics.presentation.action.account.EditEmailLoginActionBean" method="post">
		<stripes:hidden name="pin"/>
		<article>
			<section>
				<nav class="panel">
					<ul>
						<li class="detail white">
							<span class="customComboBox fullWith">
								<stripes:text name="email" class="editable" placeholder="${placeholder}"/>
							</span>
						</li>				
						<li class="detail white">
							<span class="customComboBox fullWith">
								<stripes:text name="emailConfirm" class="editable" placeholder="${placeholderConfirm}"/>
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
			<div class="warningMessage">
				<fmt:message key="account.authentication.email.message"/>
			</div>	
		</article>
	</stripes:form> 
</t:main>