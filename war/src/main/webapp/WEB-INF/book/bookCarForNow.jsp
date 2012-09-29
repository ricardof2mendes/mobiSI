<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='book.now.title' /></c:set>

<t:main title="${title}" hideFooter="true">
	<article>
		<section>
			<nav>
				<ul>
					<li>
						<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.book.NearestCarActionBean">
							<fmt:message key="nearest.car.title"/>
						</stripes:link>
					</li>
					<li>
						<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.book.LicensePlateActionBean">
							<fmt:message key="licence.plate.title"/>
						</stripes:link>
					</li>
				</ul>
			</nav>
		</section>
	</article>
</t:main>