<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>
<c:set var="title" scope="page"><fmt:message key='damage.report.title' /></c:set>
<c:set var="placeholder" scope="page"><fmt:message key="damage.report.license.placeholder"/></c:set>
<c:set var="placeholderDate" scope="page"><fmt:message key="damage.report.date.placeholder"/></c:set>
<c:set var="placeholderTextarea" scope="page"><fmt:message key="damage.report.textarea.placeholder"/></c:set>

<t:main title="${title}" addCalendar="true">

	<jsp:include page="/WEB-INF/common/message_error.jsp"/>

	<div id="errorForm"></div>

	<stripes:form id="damageReport" beanclass="com.criticalsoftware.mobics.presentation.action.trip.DamageReportActionBean" method="post">
		<stripes:hidden name="incidentCode"/>
		<stripes:hidden name="incidentType"/>
		<stripes:hidden name="rowCoord"/>
		<stripes:hidden name="colCoord"/>
	
		<!--  Begin: Check if exists damages and image map section -->
		<article class="selectDamage">
			<!--  Begin: Top Area - Car Description -->
			<section>
				<nav class="panel">
					<ul>
						<li class="image">
							<c:if test="${not empty actionBean.licensePlate}">
							<stripes:hidden name="licensePlate" value="${actionBean.car.licensePlate}"/>
								<%-- <jsp:include page="/WEB-INF/contacts/carComponent.jsp"></jsp:include> --%>
								<stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.ImmediateBookingActionBean" event="carDetails" addSourcePage="true">
										<stripes:param name="licensePlate" value="${actionBean.car.licensePlate}"/>
										<div>
											<img  class="carImage" src="${contextPath}/booking/ImmediateBooking.action?getCarImage=&licensePlate=${actionBean.car.licensePlate}&width=58&height=58" />
										</div>
										<div>
											<div>
												<span id="licensePlate">${actionBean.car.licensePlate} </span>
												<span>${actionBean.car.carBrandName}&nbsp;${actionBean.car.carModelName}</span>
												<span><fmt:message key="FuelType.${actionBean.car.fuelType.name}"/>&nbsp;
			
			                                    <c:choose>
			                                        <c:when test="${actionBean.car.fuelLevel != null}">(${actionBean.car.fuelLevel}%)</c:when>
			                                        <c:otherwise>(<fmt:message key="application.value.not.available"/>)</c:otherwise>
			                                    </c:choose>
												</span>
											</div>
										</div>
									</stripes:link>
								</c:if>
							</li>
						</ul>
					</nav>
			</section>
			<!--  End: Top Area - Car Description -->
			
			<!--  Begin: Top Area - Incident Type Buttons -->
	        <section >
	            <nav class="damageButtons">
	                <ul class="mid">
	                   <li>
	                   		<stripes:link id="damageCarAreaInterior" beanclass="com.criticalsoftware.mobics.presentation.action.trip.DamageReportActionBean" event="changeIncidentCode" class="selected">
	                            <fmt:message key="damage.report.interior"/>
	                        </stripes:link>
	                    </li>
	                    <li>
	                    	<stripes:link id="damageCarAreaExterior" beanclass="com.criticalsoftware.mobics.presentation.action.trip.DamageReportActionBean" event="changeIncidentCode" class="">
	                            <fmt:message key="damage.report.exterior"/>
	                        </stripes:link>
	                    </li>
	                </ul>
	                <ul><!--Do not remove this empty ul--></ul>
	            </nav>
	        </section>
			<!--  End: Top Area - Incident Type Buttons -->
			
			<!--  Begin: Car Images - Internal and External -->
			<section>
				<nav class="panel">
				
					 <ul>
						<li class="detail white">
							<div id="svgContainer">
							<!-- Interior -->
							<svg id="imageInternal" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="300" height="300">
								<image  class="imageDamage map" 
									xlink:href="${contextPath}/booking/ImmediateBooking.action?getCarInternalImage=&licensePlate=${actionBean.car.licensePlate}&width=360&height=360" 
									usemap="#damageZoneMap" width="300" height="300"/>
								<!-- Draw the clickable area -->
								<c:set var="row" value="11" scope="page" />
								<c:forEach begin="0" end="273" step="27" var="lines">
									<c:set var="col" value="1" scope="page" />
									<c:forEach begin="0" end="267"  step="33" var="columns">
								    	<rect class="interiorRect" width="33" height="27"
								    		x="<c:out value="${columns}"/>" 
								    		y="<c:out value="${lines}"/>"  
								    		row="<c:out value="${row}"/>" 
								    		col="<c:out value="${col}"/>" ></rect>	
							    		<c:set var="col" value="${col + 1}" scope="page"/>
							    	</c:forEach>
							    	<c:set var="row" value="${row + 1}" scope="page"/>
								</c:forEach>
								<!-- Draw the damages circles -->
								<c:forEach items="${actionBean.carDamages}" var="damages">
									<c:if test="${damages.row >= 11 }">
							    		<circle r="10"
							    			cx="<c:out value="${((damages.col)*33)-16}"/>" 
							    			cy="<c:out value="${((damages.row-10)*27)-13}"/>"  
							    			row="<c:out value="${damages.row}"/>" 
							    			col="<c:out value="${damages.col}"/>"  />
							    	</c:if>
								</c:forEach>
							</svg>
							<!-- Exterior -->
							<svg id="imageExternal" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="300" height="300">
								<image  class="imageDamage map" 
									xlink:href="${contextPath}/booking/ImmediateBooking.action?getCarExternalImage=&licensePlate=${actionBean.car.licensePlate}&width=360&height=360" 
									usemap="#damageZoneMap" width="300" height="300"/>
								<!-- Draw the clickable area -->
								<c:set var="row" value="0" scope="page" />
								<c:forEach begin="0" end="273" step="27" var="lines">
									<c:set var="col" value="1" scope="page" />
									<c:forEach begin="0" end="267"  step="33" var="columns">
								    	<rect  class="exteriorRect" width="33" height="27"
									    	x="<c:out value="${columns}"/>" 
									    	y="<c:out value="${lines}"/>"  
									    	row="<c:out value="${row}"/>" 
									    	col="<c:out value="${col}"/>" ></rect>
								    	<c:set var="col" value="${col + 1}" scope="page"/>
							    	</c:forEach>
							    	<c:set var="row" value="${row + 1}" scope="page"/>
								</c:forEach>
								<!-- Draw the damages circles -->
								<c:forEach items="${actionBean.carDamages}" var="damages">
									<c:if test="${damages.row < 11 }">
										<circle r="10" 
											cx="<c:out value="${((damages.col)*33)-16}"/>" 
											cy="<c:out value="${((damages.row+1)*27)-13}"/>" 
											row="<c:out value="${damages.row}"/>" 
											col="<c:out value="${damages.col}"/>" />
							    	</c:if>
								</c:forEach>
							</svg>
							</div>
						</li>
					</ul> 
				</nav>
			</section>
			<!--  End: Car Images - Internal and External -->
			
			<!--  Begin: Car Images - Internal and External -->
			<section>
				<nav id="checkIfExistDamages" class="panelNoBorder panel">
					<ul>
						<li class="detail link">
							<div>
								<fmt:message key="damage.report.check.if.exists.damages"/>
							</div>
						</li>
						<li>
					        <section class="submit">
								<stripes:submit name="createDamageReport" class="damageReport gray">
									<fmt:message key="damage.report.i.assume.yes"/>
								</stripes:submit>
								<stripes:submit name="continueToTrip" class="damageReport gray">
									<fmt:message key="damage.report.i.assume.no"/>
								</stripes:submit>
							</section>
				        </li>
			        </ul>
			    </nav>
			    <nav id="cancelReportDamage" class="panelNoBorder panel" style="display: none;">
					<ul>
						<li class="detail link">
							<div>
								<fmt:message key="damage.report.click.image.select.damages"/>
							</div>
						</li>
						<li>
					        <section class="submit">
								<stripes:submit name="" class="submitBtn gray cancelBtn">
									<fmt:message key="damage.report.i.assume.cancel"/>
								</stripes:submit>
								<stripes:submit name="continueBtn" class="submitBtn green" >
				                	<fmt:message key="damage.report.continue"/>
								</stripes:submit>
							</section>
				        </li>
			        </ul>
			    </nav>
			</section>
			<!--  End: Car Images - Internal and External -->
		</article>
		<!--  End: Check if exists damages and image map section -->
		
		
		<!--  Begin: Report damages area  -->
		<article class="damageDetails" style="display: none;">
			<section>
				<c:if test="${not empty actionBean.licensePlate}">
					<jsp:include page="/WEB-INF/contacts/carComponent.jsp"></jsp:include>
				</c:if>
			</section>
			
			<!--  Begin: Top Button - Incident Type  -->
			<!-- <section>
	            <nav class="damageDetailsButtons"> 
	                <ul class="mid">
	                	<li>
	                   		<stripes:link id="typeScratched" beanclass="com.criticalsoftware.mobics.presentation.action.trip.DamageReportActionBean" event="changeIncidentType" class="selected">
	                            <fmt:message key="damage.report.damage.type.scratched"/>
	                        </stripes:link>
	                    </li>
	                    <li>
	                    	<stripes:link id="typeSmashed" beanclass="com.criticalsoftware.mobics.presentation.action.trip.DamageReportActionBean" event="changeIncidentType" class="">
	                           <fmt:message key="damage.report.damage.type.smashed"/>
	                        </stripes:link>
	                    </li>
	                </ul>
	                <ul><!--Do not remove this empty ul-- ></ul>
	            </nav>
	        </section> -->
	        <!--  End: Report damages area  -->
	        
	        <!--  Begin: Report damages area  -->
	        <section>
				<nav  class="panelNoBorder panelPicture">
					<ul>
						<li class="detail link">
							<div>
								<b><fmt:message key="damage.report.picture"/></b>
							</div>
						</li>
						<li class="imageNoLink">
							<div class="picSelector">
								<div id="multiUpload" class="hidden"></div>
								<c:forEach begin="1" end="10" varStatus="loop">
									<stripes:file id="upload${loop.index}" name="files[${loop.index}]" style="display:none" accept="image/*"/>
								</c:forEach> 
								<div class="picSelectorSnap">
									<!-- Selected images thumbs will be added here -->
									<div id="noPicturesMessage">
										<fmt:message key="damage.report.no.pictures"/>
									</div>
								</div>
							</div>
						</li>
					</ul>	
				</nav>
			</section>
			<section>
				<nav class="panelNoBorder panelPicture">
					<ul>
						<li class="detail link">
							<div>
								<fmt:message key="damage.report.textarea.placeholder"/>
							</div>
						</li>
						<li class="detail white">
							<span class="customComboBox">
								<stripes:textarea id="damageDescription" placeholder="Please type here" name="description"  class="editable"  rows="6" cols="40"></stripes:textarea>
							</span>
						</li>
					</ul>
				</nav>
			</section>
			<section>
				<nav id="Damages" class="panelNoBorder panel">
					<ul>
						<li class="detail link">
							<div>
								<fmt:message key="damage.report.send.cancel"/>
							</div>
						</li>
						<li>
					        <section class="submit">
								<stripes:submit name="submitDamageReport" class="damageReport green">
									<fmt:message key="damage.report.submit"/>
								</stripes:submit>
								<stripes:submit name="" class="damageReport red cancelBtn">
									<fmt:message key="damage.report.i.assume.cancel"/>
								</stripes:submit>
							</section>
				        </li>
			        </ul>
			    </nav>
			</section>
		</article>
		<!--  End: Report damages area  -->
		
		<div class="confirm2">
			<article>
				<!-- Open Camera Modal -->
				<section id="openCameraOrGalery" class="hidden">
					<stripes:link id="" href="#" class="alertBtn openGaleryBtn gray" >
	                	<fmt:message key="damage.report.open.galery"/>
					</stripes:link>
					<stripes:link href="#" class="alertBtn red cancelBtn" >
	                	<fmt:message key="damage.report.i.assume.cancel"/>
					</stripes:link>
				</section>
				
				<!-- Remove Image Modal -->
				<section id="removeSelectedImg" class="hidden">
					<stripes:link id="removeImg" href="#" class="alertBtn gray" >
	                	<fmt:message key="damage.report.remove.image"/>
					</stripes:link>
					<stripes:link  href="#" class="alertBtn red cancelBtn" >
	                	<fmt:message key="damage.report.i.assume.cancel"/>
					</stripes:link>
				</section>
				
				<!-- New Damage or Send Report Modal -->
				<section id="sendOrReportNew" class="hidden">
					<nav class="damageDetailsButtons"> 
		                <ul class="mid">
		                	<li>
		                   		<stripes:link id="typeScratched" beanclass="com.criticalsoftware.mobics.presentation.action.trip.DamageReportActionBean" event="changeIncidentType" class="selected">
		                            <fmt:message key="damage.report.damage.type.scratched"/>
		                        </stripes:link>
		                    </li>
		                    <li>
		                    	<stripes:link id="typeSmashed" beanclass="com.criticalsoftware.mobics.presentation.action.trip.DamageReportActionBean" event="changeIncidentType" class="">
		                           <fmt:message key="damage.report.damage.type.smashed"/>
		                        </stripes:link>
		                    </li>
		                </ul>
		                <ul><!--Do not remove this empty ul--></ul>
		            </nav>
					<stripes:link id="" href="#" class="alertBtn openGaleryBtn gray" >
	                	<fmt:message key="damage.report.open.galery"/>
					</stripes:link>
					<stripes:link id="reportNewBtn" href="#" class="alertBtn gray" >
	                	<fmt:message key="damage.report.select.other.damage"/>
					</stripes:link>
					<stripes:link  href="#" class="alertBtn red removeLastDamage" >
	                	<fmt:message key="damage.report.i.assume.cancel"/>
					</stripes:link>
				</section>
				
				<!-- New Damage or Send Report Modal -->
				<section id="removeCircleOrCancel" class="hidden">
					<stripes:link id="removeClickedCirle" href="#" class="alertBtn gray" >
	                	<fmt:message key="damage.report.remove.unreported"/>
					</stripes:link>
					<stripes:link  href="#" class="alertBtn red cancelRemoveBtn" >
	                	<fmt:message key="damage.report.i.assume.cancel"/>
					</stripes:link>
				</section>
				
				<!-- Without Damage To Report Modal -->
				<section id="noDamageToReport" class="hidden">
					<h3>
						<fmt:message key="damage.report.damages.not.selected"/>
					</h3>
					<stripes:link id="reportNewBtn" href="#" class="alertBtn red" >
	                	<fmt:message key="damage.report.i.assume.cancel"/>
					</stripes:link>
				</section>
				
				<!-- Sending Report Modal -->
				<section id="sendingReport" class="hidden">
					<h2><fmt:message key="damage.report.submiting"/>&nbsp;&nbsp;&nbsp;<img src="${contextPath}/img/indicator.gif"/></h2>
					<h3>
						<fmt:message key="damage.report.submiting">
							<fmt:param>
								<mobi:formatMobics type="milliseconds" value="${applicationScope.configuration.lockEndPollingTimeoutMilliseconds}"/>
							</fmt:param>
						</fmt:message>
					</h3>
				</section>
			</article>
		</div>
	</stripes:form> 

</t:main>

