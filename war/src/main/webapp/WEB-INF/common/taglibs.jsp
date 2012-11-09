<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="mobi" uri="http://www.criticalsoftware.com/taglib/formatter"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<c:set var="splashScreenStyle" value="${actionBean.splashScreenStyle}" />
<c:set var="headerStyle" value="${actionBean.headerStyle}" />