<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>

<article id="legend" class=" hidden">
	<section>
		<nav class="simpleList">
			<ul>
				<li>
					<span><fmt:message key="location.legend.my.location"/></span>
				</li>
				
				<c:choose>
			    	<c:when test="${param.carLocation == '' || param.searchImmediateInMap == ''}">
				    	<li>
							<span><fmt:message key="location.legend.car.location"/></span>
						</li>
						<c:if test="${param.searchImmediateInMap == null}">
							<li>
								<span><fmt:message key="location.legend.regular.zone"/></span>
							</li>
							<li>
								<span><fmt:message key="location.legend.undesire.zone"/></span>
							</li>
							<li>
								<span><fmt:message key="location.legend.forbidden.zone"/></span>
							</li>
						</c:if>
			    	</c:when>
			    	<c:when test="${param.parkLocation == ''}">
			    		<li class="hidden"></li>
			    		<li>
							<span><fmt:message key="location.legend.park.zone"/></span>
						</li>
			    	</c:when>
			    	<c:otherwise></c:otherwise>
			    </c:choose>
			</ul>
		</nav>
	</section>
	<stripes:link id="closeLegend" href="#toggle" class="linkBtn gray">
		<fmt:message key="location.legend.button.close"/>
	</stripes:link>
</article>