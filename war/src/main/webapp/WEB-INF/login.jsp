<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='login.title' /></c:set>

<t:login title="${title}">
	<article>
		<section id="logo">
		
		</section>
		<section>
			<form action="${contextPath}/Login.action" method="post">
				<div>
		            <span style="display: ${param.error ? 'block' : 'none'}" >
		            	<fmt:message key="login.error" />	                    
					</span>
					<input type="text" id="username" name="username" autofocus="autofocus" placeholder="<fmt:message key="login.j_username" />"></input>
					<input type="password" id="password" name="password" placeholder="<fmt:message key="login.j_password" />"></input>
				</div>
				<input type="submit" id="authenticate" name="authenticate" class="loginBtn" value="<fmt:message key="login.submit"/>" />
			</form>
		</section>
	</article>
	<footer class="clear">
		<fmt:message key="login.powered"/>
	</footer>
</t:login>
