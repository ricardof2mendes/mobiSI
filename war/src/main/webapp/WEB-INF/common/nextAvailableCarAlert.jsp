<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/common/taglibs.jsp"%>

<article>
    <section>
        <!-- Modal window for confirmation -->
        <div class="confirm">
            <article>
                <section>
                    <h2><fmt:message key="book.advance.no.results.found.header"/></h2>
                    <h3><fmt:message key="book.advance.no.results.found"/></h3>
                    <stripes:link beanclass="com.criticalsoftware.mobics.presentation.action.booking.AdvanceBookingActionBean"
                                  class="alertBtn green"
                                  event="nextAvailableCar" addSourcePage="true">
                        <stripes:param name="startDate"><fmt:formatDate value="${actionBean.startDate}" pattern="${applicationScope.configuration.dateTimePattern}"/></stripes:param>
                        <stripes:param name="endDate"><fmt:formatDate value="${actionBean.endDate}" pattern="${applicationScope.configuration.dateTimePattern}"/></stripes:param>
                        <stripes:param name="location" value="${actionBean.location}"/>
                        <stripes:param name="zone" value="${actionBean.zone}"/>

                        <fmt:message key="trip.detail.advance.booking.next.available"/>
                    </stripes:link>
                    <a id="closeConfirm" href="#" class="alertBtn gray" >
                        <fmt:message key="book.advance.cancel"/>
                    </a>
                </section>
            </article>
        </div>
    </section>
</article>