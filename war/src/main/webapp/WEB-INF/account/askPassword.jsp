<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key="account.security.check.title"/></c:set>
<c:set var="placeholder" scope="page"><fmt:message key="password.customer.input" /></c:set>


<t:main title="${title}">
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
	
	<stripes:form beanclass="${actionBean.class.name}" method="POST">
	  	<article class="pin">
			<section>
				<div>
					<label><fmt:message key="account.security.enter.password"/></label>
				</div>
				<div>
					<stripes:password id="password" name="password" />
					<%-- <stripes:text type="number" name="pin" id="pin" placeholder="${placeholder}" autocomplete="off"/>--%> 				
				</div>
				
				<div>
					<stripes:submit name="data"><fmt:message key="password.button"/></stripes:submit>
				</div>
			</section>
		</article>
	</stripes:form>
</t:main>