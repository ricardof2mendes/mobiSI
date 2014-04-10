<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/common/taglibs.jsp"%>

<div class="left">
   <span>
       <fmt:message key="trip.detail.max.cost"/>
   </span>
</div>
<div class="right" style="font-size: 100% !important; text-align: right;">
	<table>
        <tr>
            <td><fmt:message key="trip.detail.max.cost.one.hour"/></td>
            <td style="padding-left: 8px"><mobi:formatMobics value="${param.maxCostPerHour}" type="currencySymbol" /></td>
            <td style="padding-left: 8px"><mobi:formatMobics value="${param.distanceThreshold}" type="distance" /></td>
        </tr>
		<tr>
		    <td>${param.configurableTime}h</td>
            <td style="padding-left: 8px"><mobi:formatMobics value="${param.maxCostPerConfigurableHour}" type="currencySymbol" /></td>
            <td style="padding-left: 8px"><mobi:formatMobics value="${param.includedDistancePerConfigurableHour}" type="distance" /></td>
        </tr>
		<tr>
            <td><fmt:message key="trip.detail.max.cost.max.hour"/></td>
            <td style="padding-left: 8px"><mobi:formatMobics value="${param.maxCostPerDay}" type="currencySymbol" /></td>
            <td style="padding-left: 8px"><mobi:formatMobics value="${param.includedDistancePerDay}" type="distance" /></td>
        </tr> 
    </table>
</div>