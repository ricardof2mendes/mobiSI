<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key="account.title" /></c:set>

<t:main title="${title}">
	<article>
		<section>
			<h2><fmt:message key="account.identification"/></h2>
			<nav class="panel">
				<ul>
					<li class="detail">
						<span>
							<fmt:message key="account.fullname"/> 
						</span>
						<span>
							${actionBean.customer.fullName}
						</span>
					</li>
					<li class="detail">
						<span>
							<fmt:message key="account.email"/>
						</span>
						<span>
							${actionBean.context.user.username}
						</span>	
					</li>
					<li class="detail">
						<span>
							<fmt:message key="account.password"/>
						</span>
						<span>
							${actionBean.context.user.password}
						</span>	
					</li>
					<li class="detail">
						<span>
							<fmt:message key="account.pin"/>
						</span>
						<span>
							TODO
						</span>	
					</li>
				</ul>			 
			</nav>
		</section>		
		<section>
			<h2><fmt:message key="account.contact"/></h2>
			<nav class="panel">
				<ul>
					<li class="detail">
						<span>
							<fmt:message key="account.country"/> 
						</span>
						<span>
							${actionBean.customer.country.name}
						</span>
					</li>
					<li class="detail">
						<span>
							<fmt:message key="account.address"/>
						</span>
						<span>
							${actionBean.customer.address}
						</span>	
					</li>
					<li class="detail">
						<span>
							<fmt:message key="account.phone"/>
						</span>
						<span>
							${actionBean.customer.phoneNumber}
						</span>	
					</li>
				</ul>			 
			</nav>
		</section>
		<section>
			<h2><fmt:message key="account.payment"/></h2>
			<nav class="panel">
				<ul>
					<li class="detail">
						<span>
							<fmt:message key="account.method"/> 
						</span>
						<span>
							TODO
						</span>
					</li>
					<li class="detail">
						<span>
							<fmt:message key="account.card"/>
						</span>
						<span>
							TODO
						</span>	
					</li>
					<li class="detail">
						<span>
							<fmt:message key="account.number"/>
						</span>
						<span>
							TODO
						</span>	
					</li>
					<li class="detail">
						<span>
							<fmt:message key="account.expires"/>
						</span>
						<span>
							TODO
						</span>	
					</li>
				</ul>			 
			</nav>
		</section>			
	</article>	
		
	<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.LogoutActionBean" class="linkBtn red">
		<fmt:message key="main.logout"/>
	</stripes:link>
</t:main>