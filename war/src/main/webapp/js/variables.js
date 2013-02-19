'<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>';
//
// Any variable declared here must be replicated in the correct jsp tag (main/map)
//

// context path
var CONTEXT_PATH = '${pageContext.request.contextPath}';
// pooling interval
var POOLING_INTERVAL = '${applicationScope.configuration.statePollingIntervalMilliseconds}';
var TIMEOUT_INTERVAL = '${applicationScope.configuration.statePollingTimeoutMilliseconds}';
// image size
var IMG_WIDTH = '${applicationScope.configuration.thumbnailWidth}px';
var IMG_HEIGHT = '${applicationScope.configuration.thumbnailHeight}px';

// localizable strings
var ZONE_ALL_LABEL = '<fmt:message key="book.advance.zone.any" />';
var GEOLOCATION_NOT_SUPPORTED_LABEL = '<fmt:message key="geolocation.alert.msg.not.supported"/>';
var OK_LABEL = '<fmt:message key="calendar.ok"/>';
var MINUTES_LABEL = '<fmt:message key="calendar.minutes"/>';
// date patterns for calendar
var DATE_TIME_PATTERN = '${applicationScope.configuration.jsDateTimePattern}';
var DATE_PATTERN = '${applicationScope.configuration.jsDatePattern}';
var TIME_PATTERN = '${applicationScope.configuration.jsTimePattern}';

var ANY_DISTANCE = '${applicationScope.configuration.anyDistance}';