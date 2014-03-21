<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp" %>
<c:set var="title" scope="page"><fmt:message key='charging.stations'/></c:set>

<t:main title="${title}">
    <article>
        <section>
            <nav class="panel">
                <ul>
                    <li class="detail">
                        <span>
                            <fmt:message key="charging.stations.address"/>
                        </span>
                        <span>
                            ${actionBean.stationAddress}
                        </span>
                    </li>
                    <li class="detail">
                        <span>
                            <fmt:message key="charging.stations.coordinates"/>
                        </span>
                        <span>
                            ${actionBean.station.coordinates.latitude},${actionBean.station.coordinates.longitude}
                        </span>
                    </li>
                </ul>
            </nav>
        </section>
    </article>
    <article>
        <section>
            <nav class="panel">
                <ul>
                	<li class="detail">
                        <span>
                            <fmt:message key="charging.stations.status"/>
                        </span>
                        <span>
                                ${actionBean.station.status}
                        </span>
                    </li>
                    <li class="detail">
                        <span>
                            <fmt:message key="charging.stations.type"/>
                        </span>
                        <span>
                                ${actionBean.station.type}
                        </span>
                    </li>
                    <li class="detail">
                        <span>
                            <fmt:message key="charging.stations.available_services"/>
                        </span>
                        <span>
                                <fmt:message key="charging.stations.charging"/>
                        </span>
                    </li>
                    <li class="detail">
                        <span>
                            <fmt:message key="charging.stations.operator"/>
                        </span>
                        <span>
                                ${actionBean.station.operator}
                        </span>
                    </li>
                    <li class="detail">
                        <span>
                            <fmt:message key="charging.stations.compatibility"/>
                        </span>
                        <span>
                                ${actionBean.station.type}
                        </span>
                    </li>
                </ul>
            </nav>
        </section>
    </article>
</t:main>