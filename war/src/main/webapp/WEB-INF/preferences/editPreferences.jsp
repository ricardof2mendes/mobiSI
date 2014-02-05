<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='preferences.title' /></c:set>
<c:set var="placeholder" scope="page"><fmt:message key="preferences.placeholder"/></c:set>

<t:main title="${title}">
	<jsp:include page="/WEB-INF/common/message_error.jsp"/>
	<stripes:form beanclass="com.criticalsoftware.mobics.presentation.action.preferences.EditPreferencesActionBean" method="post">
		<article>
			<section>
				<nav class="simpleList">
					<h2><fmt:message key="preferences.search.title"/></h2>
					<ul>
						<li class="filter white">
							<span>
								<fmt:message key="preferences.radius"/>
							</span>
							<span class="customComboBox">
								<stripes:select name="searchRadius">
									<stripes:option value="${applicationScope.configuration.anyDistance}">
										<fmt:message key="book.now.search.distance.any"/>
									</stripes:option>
									<stripes:option value="500" >
										<fmt:message key="book.now.search.distance.label.meters"><fmt:param><mobi:formatMobics value="500" type="distance"/></fmt:param></fmt:message>
									</stripes:option>
									<stripes:option value="1000" >
										<fmt:message key="book.now.search.distance.label"><fmt:param><mobi:formatMobics value="1000" type="distance"/></fmt:param></fmt:message>
									</stripes:option>
									<stripes:option value="2000" >
										<fmt:message key="book.now.search.distance.label"><fmt:param><mobi:formatMobics value="2000" type="distance"/></fmt:param></fmt:message>
									</stripes:option>
									<stripes:option value="3000" >
										<fmt:message key="book.now.search.distance.label"><fmt:param><mobi:formatMobics value="3000" type="distance"/></fmt:param></fmt:message>
									</stripes:option>
								</stripes:select>
							</span>
						</li>
						<li class="anotherTitle">
							<span>
								<fmt:message key="preferences.sort"/>
							</span>
							<span>
								
							</span>
						</li>
						<li class="filter">
							<span>
								<fmt:message key="preferences.sort.1"/>
							</span>
							<span class="customComboBox">
								<stripes:select id="sort0" name="columnNames[0]">
                                    <stripes:option value="CAR_DISTANCE"><fmt:message key="OrderBy.CAR_DISTANCE"/></stripes:option>
                                    <stripes:option value="CAR_FARE"><fmt:message key="OrderBy.CAR_FARE"/></stripes:option>
                                    <stripes:option value="CAR_CATEGORY"><fmt:message key="OrderBy.CAR_CATEGORY"/></stripes:option>
								</stripes:select>
							</span>
						</li>
						<li class="filter">
							<span>
								<fmt:message key="preferences.sort.2"/>
							</span>
							<span class="customComboBox">
								<stripes:select id="sort1" name="columnNames[1]">
                                    <stripes:option value="CAR_DISTANCE"><fmt:message key="OrderBy.CAR_DISTANCE"/></stripes:option>
                                    <stripes:option value="CAR_FARE"><fmt:message key="OrderBy.CAR_FARE"/></stripes:option>
                                    <stripes:option value="CAR_CATEGORY"><fmt:message key="OrderBy.CAR_CATEGORY"/></stripes:option>
								</stripes:select>
							</span>
						</li>
						<li class="filter">
							<span>
								<fmt:message key="preferences.sort.3"/>
							</span>
							<span class="customComboBox">
								<stripes:select id="sort2" name="columnNames[2]">
                                    <stripes:option value="CAR_DISTANCE"><fmt:message key="OrderBy.CAR_DISTANCE"/></stripes:option>
                                    <stripes:option value="CAR_FARE"><fmt:message key="OrderBy.CAR_FARE"/></stripes:option>
                                    <stripes:option value="CAR_CATEGORY"><fmt:message key="OrderBy.CAR_CATEGORY"/></stripes:option>
								</stripes:select>	
							</span>
						</li>
					</ul>
				</nav>
			</section>
			<section>
				<nav class="simpleList">
					<h2><fmt:message key="preferences.communication.title"/></h2>
					<ul>
						<li class="filter white">
							<span>
								<fmt:message key="preferences.language"/>
							</span>
							<span class="customComboBox">
								<stripes:select name="language">
									<c:forEach items="${actionBean.languages}" var="lang">
										<stripes:option value="${lang.code}" >${lang.name}</stripes:option>
									</c:forEach>
								</stripes:select>
							</span>
						</li>
					
						<li class="anotherTitle">
							<span>
								<fmt:message key="preferences.active.channels"/>
							</span>
							<span></span>
						</li>
						<li class="filter">
							<span></span>
							<span class="customComboBox">
								<stripes:hidden name="communicationChannels[0]"/>
								<stripes:hidden name="sortAsc"/>
								<stripes:checkbox id="email" name="communicationChannels[0]" value="EMAIL" disabled="disabled"/>
								<fmt:message key="preferences.active.channels.email"/>
							</span>
						</li>
						<li id="appFilter" class="filter">
							<span></span>
							<span class="customComboBox">
								<stripes:checkbox id="app" name="communicationChannels[1]" value="IPHONE"/>
								<fmt:message key="preferences.active.channels.iphone"/>
							</span>
						</li>
						<li class="filter white">
							<span>
								<fmt:message key="preferences.default.channels"/>
							</span>
							<span class="customComboBox">
								<stripes:select id="communicationChannel" name="communicationChannel">
									<stripes:option id="emailOption" value="EMAIL"><fmt:message key="preferences.active.channels.email"/></stripes:option>
									<stripes:option id="appOption" value="IPHONE">
										<fmt:message key="preferences.active.channels.iphone"/>
									</stripes:option>
								</stripes:select>
							</span>
						</li>
					</ul>
				</nav>
			</section>
			<section>
				<nav class="panel">
					<h2><fmt:message key="preferences.notifications.title"/></h2>
					<ul>
						<li class="detail white">
							<span>
								<fmt:message key="preferences.start"/>
							</span>
							<span class="customComboBox">
								<stripes:select name="timeToStartSending">
									<stripes:option value="-7200">
										<fmt:message key="find.car.later.start.sending.hours"><fmt:param value="2"/></fmt:message>
									</stripes:option>
									<stripes:option value="-3600">
										<fmt:message key="find.car.later.start.sending.hour"><fmt:param value="1"/></fmt:message>
									</stripes:option>
									<stripes:option value="-1800">
										<fmt:message key="find.car.later.start.sending.minutes"><fmt:param value="30"/></fmt:message>
									</stripes:option>
									<stripes:option value="-900">
										<fmt:message key="find.car.later.start.sending.minutes"><fmt:param value="15"/></fmt:message>
									</stripes:option>
									<stripes:option value="300">
										<fmt:message key="find.car.later.start.sending.minutes"><fmt:param value="5"/></fmt:message>
									</stripes:option>
									<stripes:option value="0">
										<fmt:message key="find.car.later.on.time"/>
									</stripes:option>
								</stripes:select>
							</span>
						</li>
						<li class="detail white">
							<span>
								<fmt:message key="preferences.stop"/>
							</span>
							<span class="customComboBox">
								<stripes:select name="timeToStopSending">
									<stripes:option value="0">
										<fmt:message key="find.car.later.on.time"/>
									</stripes:option>
									<stripes:option value="300">
										<fmt:message key="find.car.later.end.sending.minutes"><fmt:param value="5"/></fmt:message>
									</stripes:option>
									<stripes:option value="900">
										<fmt:message key="find.car.later.end.sending.minutes"><fmt:param value="15"/></fmt:message>
									</stripes:option>
									<stripes:option value="1800">
										<fmt:message key="find.car.later.end.sending.minutes"><fmt:param value="30"/></fmt:message>
									</stripes:option>
									<stripes:option value="3600">
										<fmt:message key="find.car.later.end.sending.hour"><fmt:param value="1"/></fmt:message>
									</stripes:option>									
								</stripes:select>
							</span>
						</li>
						<li class="detail white">
							<span>
								<fmt:message key="preferences.max.number"/>
							</span>
							<span class="customComboBox">
								<stripes:text type="number" name="numberOfNotifications" 
									   placeholder="${placeholder}"/>
							</span>
						</li>
					</ul>
				</nav>
			</section>
			<section class="submit">
				<stripes:submit name="save" class="submitBtn gray"><fmt:message key="preferences.save"/></stripes:submit>
			</section>
		</article>	
	</stripes:form>
</t:main>