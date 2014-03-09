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
                            ${actionBean.station.address.street}, ${actionBean.station.address.number}, ${actionBean.station.address.postalCode}, ${actionBean.station.address.city}, ${actionBean.station.address.countryCode}
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
                            <fmt:message key="charging.stations.available.sat"/>
                        </span>
                        <span>
                                ${actionBean.station.availableSattelites}
                        </span>
                    </li>
                    <li class="detail">
                        <span>
                            <fmt:message key="charging.stations.totalStat"/>
                        </span>
                        <span>
                                ${actionBean.station.totalSattelites}
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
                            <fmt:message key="charging.stations.code"/>
                        </span>
                        <span>
                                ${actionBean.station.chargingStationCode}
                        </span>
                    </li>
                    <li class="detail">
                        <span>
                            <fmt:message key="charging.stations.manufactor"/>
                        </span>
                        <span>
                                ${actionBean.station.manufacturer}
                        </span>
                    </li>
                    <li class="detail">
                        <span>
                            <fmt:message key="charging.stations.model"/>
                        </span>
                        <span>
                                ${actionBean.station.model}
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
                            <fmt:message key="charging.stations.bookable"/>
                        </span>
                        <span>
                                ${actionBean.station.bookable}
                        </span>
                    </li>
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
                            <fmt:message key="charging.stations.standalone"/>
                        </span>
                        <span>
                                ${actionBean.station.standalone}
                        </span>
                    </li>
                </ul>
            </nav>
        </section>
    </article>
</t:main>