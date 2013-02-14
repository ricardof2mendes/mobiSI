/*
 * $Id: $
 *
 * Copyright (c) Critical Software S.A., All Rights Reserved.
 * (www.criticalsoftware.com)
 *
 * This software is the proprietary information of Critical Software S.A.
 * Use is subject to license terms.
 *
 * Last changed on: $Date: $
 * Last changed by: $Author: $
 */
package com.criticalsoftware.mobics.presentation.util;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ltiago
 * @version $Revision: $
 */
public class Configuration implements Serializable{

    /** */
    private static final long serialVersionUID = 7216539220608643618L;

    private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

    /**
     * The Configuration instance property.
     */
    public static final Configuration INSTANCE = new Configuration();

    /** Application Encoding **/
    private final String uriEnconding = "UTF-8";
    
    /** Meter Pattern **/
    private final String meterPattern = "#";

    /** Kilometer Pattern **/
    private final String kilometerPattern = "#,##0.0";

    /** Currency Pattern **/
    private final String currencyPattern = "#,##0.00";

    /** Date time Pattern **/
    private final String dateTimePattern = "dd/MM/yyyy HH:mm";
    
    /** Date time Pattern **/
    private final String timePattern = "HH:mm";

    /** Javascript date pattern **/
    private final String jsDatePattern = "dd/mm/yy";

    /** Javascript time pattern **/
    private final String jsTimePattern = "HH:ii";

    /** Authentication failure string return by webservices on Axis fault **/
    private final String authenticationFailureString = "Failed Authentication";

    /** Car Club Endpoint **/
    private final String carClubEndpoint = "http://mobics02.critical.pt:8080/mobics-webservices/CarClub";
    
    /** Car Endpoint **/
    private final String carEndpoint = "http://mobics02.critical.pt:8080/mobics-webservices/Car";

    /** Fleet Endpoint **/
    private final String fleetEndpoint = "http://mobics02.critical.pt:8080/mobics-webservices/Fleet";

    /** Booking Endpoint **/
    private final String bookingEndpoint = "http://mobics02.critical.pt:8080/mobics-webservices/Booking";

    /** Customer Endpoint **/
    private final String customerEndpoint = "http://mobics02.critical.pt:8080/mobics-webservices/Customer";

    /** Miscellaneous Endpoint **/
    private final String miscellaneousEnpoint = "http://mobics02.critical.pt:8080/mobics-webservices/Miscellaneous";

    /** Geolocation servce **/
    private final String geolocationServer = "nominatim.openstreetmap.org";

    /** Thumbnail width **/
    private final int thumbnailWidth = 58;

    /** Thumbnail height **/
    private final int thumbnailHeight = 58;

    /** Min results **/
    private final int minResults = 1;

    /** Max results **/
    private final int maxResults = 99;
    
    /** Recent activities filter month gap **/
    private final int recentActivitiesFilterMonthGap = 6;
    
    /** Default theme warm word **/
    private final String defaultThemeWarmWord = "warm";
    
    /** Default theme style **/
    private final String defaultThemeStyle = "map";

    /** Default theme color **/
    private final String defaultThemeColor = "darkgreen";
    
    /** Allowed countries on search location server **/
    private final String geolocationServerAllowedCountries = "pt";
    
    /** Any distance for radius **/
    private final int anyDistance = 9999999;
    
    /** State polling interval in milliseconds **/
    private final int statePollingIntervalMilliseconds = 3000;
    
    /** State polling timeout in milliseconds **/
    private final int statePollingTimeoutMilliseconds = 60000;
    

    /**
     * @return the uriEnconding
     */
    public String getUriEnconding() {
        return getValue("org.apache.catalina.connector.URI_ENCODING", uriEnconding);
    }

    /**
     * @return the meterPattern
     */
    public String getMeterPattern() {
        return getValue("mobics.config.meter.pattern", meterPattern);
    }

    /**
     * @return the kilometerPattern
     */
    public String getKilometerPattern() {
        return getValue("mobics.config.kilometer.pattern", kilometerPattern);
    }

    /**
     * @return the currencyPattern
     */
    public String getCurrencyPattern() {
        return getValue("mobics.config.currency.pattern", currencyPattern);
    }

    /**
     * @return the authenticationFailureString
     */
    public String getAuthenticationFailureString() {
        return getValue("mobics.config.authentication.failure.string", authenticationFailureString);
    }

    /**
     * @return the carClubEndpoint
     */
    public String getCarClubEndpoint() {
        return getValue("mobics.config.endpoint.carclub", carClubEndpoint);
    }
    
    /**
     * @return the carEndpoint
     */
    public String getCarEndpoint() {
        return getValue("mobics.config.endpoint.car", carEndpoint);
    }

    /**
     * @return the fleetEndpoint
     */
    public String getFleetEndpoint() {
        return getValue("mobics.config.endpoint.fleet", fleetEndpoint);
    }

    /**
     * @return the bookingEndpoint
     */
    public String getBookingEndpoint() {
        return getValue("mobics.config.endpoint.booking", bookingEndpoint);
    }

    /**
     * @return the customerEndpoint
     */
    public String getCustomerEndpoint() {
        return getValue("mobics.config.endpoint.customer", customerEndpoint);
    }

    /**
     * @return the miscellaneousEnpoint
     */
    public String getMiscellaneousEnpoint() {
        return getValue("mobics.config.endpoint.miscellaneous", miscellaneousEnpoint);
    }

    /**
     * @return the geolocationServer
     */
    public String getGeolocationServer() {
        return getValue("mobics.config.geolocation.server", geolocationServer);
    }

    /**
     * @return the carThumbnailWidth
     */
    public int getThumbnailWidth() {
        return Integer.valueOf(getValue("mobics.config.thumbnail.width", thumbnailWidth));
    }

    /**
     * @return the carThumbnailHeight
     */
    public int getThumbnailHeight() {
        return Integer.valueOf(getValue("mobics.config.thumbnail.height", thumbnailHeight));
    }

    /**
     * @return the minResults
     */
    public int getMinResults() {
        return Integer.valueOf(getValue("mobics.config.min.results", minResults));
    }

    /**
     * @return the maxResults
     */
    public int getMaxResults() {
        return Integer.valueOf(getValue("mobics.config.max.results", maxResults));
    }

    /**
     * @return the datePattern
     */
    public String getJsDatePattern() {
        return getValue("mobics.config.js.date.pattern", jsDatePattern);
    }

    /**
     * @return the timePattern
     */
    public String getJsTimePattern() {
        return getValue("mobics.config.js.time.pattern", jsTimePattern);
    }

    /**
     * @return the dateTimePattern
     */
    public String getDateTimePattern() {
        return getValue("mobics.config.datetime.pattern", dateTimePattern);
    }
    
    public int getRecentActivitiesFilterMonthGap() {
        return Integer.valueOf(getValue("mobics.config.recents.filter.month.gap", recentActivitiesFilterMonthGap));
    }
    
    /**
     * 
     * @return the defaultThemeStyle
     */
    public String getDefaultThemeStyle() {
        return getValue("mobics.config.default.theme.style", defaultThemeStyle);
    }

    /**
     * 
     * @return the defaultThemeColor
     */
    public String getDefaultThemeColor() {
        return getValue("mobics.config.default.theme.color", defaultThemeColor);
    }

    /**
     * 
     * @return the defaultThemeStyle
     */
    public String getDefaultThemeWarmWord() {
        return getValue("mobics.config.default.theme.warm.word", defaultThemeWarmWord);
    }
    

    /**
     * @return the geolocationServerAllowedCountries
     */
    public String getGeolocationServerAllowedCountries() {
        return getValue("mobics.config.geolocation.server.allowed.countries", geolocationServerAllowedCountries);
    }
    
    /**
     * @return the timePattern
     */
    public String getTimePattern() {
        return getValue("mobics.config.time.pattern", timePattern);
    }

    /**
     * @return the anyDistance
     */
    public int getAnyDistance() {
        return Integer.valueOf(getValue("mobics.config.radius.any.distance", anyDistance));
    }
    
    /**
     * @return the statePollingIntervalMilliseconds
     */
    public int getStatePollingIntervalMilliseconds() {
        return Integer.valueOf(getValue("mobics.config.state.polling.interval.milliseconds", statePollingIntervalMilliseconds));
    }

    /**
     * @return the statePollingTimeoutMilliseconds
     */
    public int getStatePollingTimeoutMilliseconds() {
        return Integer.valueOf(getValue("mobics.config.state.polling.timeout.milliseconds", statePollingTimeoutMilliseconds));
    }

    private Configuration() {
    }

    private String getValue(String key, Object defaultValue) {
        String value = String.valueOf(defaultValue);

        if (System.getProperty(key) == null) {
            LOGGER.warn("System property '{}' does not exist or is empty.", key);
        } else {
            value = System.getProperty(key);
        }

        return value;
    }

}
