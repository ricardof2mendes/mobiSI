<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='error.title' /></c:set>

<t:main title="${title}">
	<div class="globalErrors">
		<c:choose>
		    <c:when test="${not empty param.httpError}">
		    	<section class="errors">
					<ul>
						<li>
							<fmt:message key="error.http.${param.httpError}" />
						</li>
					</ul>
				</section>
		    </c:when>
		    <c:when test="${not empty requestScope.error}">
		    	<section class="errors">
					<ul>
						<li>
		        			${requestScope.error}
	        			</li>
	       			</ul>
	     		</section>
		    </c:when>
		    <c:when test="${not empty actionBean.context.validationErrors}">
        		<c:choose>
					<c:when test="${actionBean.fieldErrors}">
						<stripes:errors/>
					</c:when>
					<c:otherwise>
						<div class="globalErrors">
							<stripes:errors/>
						</div>
					</c:otherwise>
				</c:choose>
		    </c:when>
		    <c:otherwise>
		    	<section class="errors">
					<ul>
						<li>
		        			<fmt:message key="error.internal" />
		        		</li>
	       			</ul>
	     		</section>
		    </c:otherwise>
		</c:choose>
	</div>
</t:main>
