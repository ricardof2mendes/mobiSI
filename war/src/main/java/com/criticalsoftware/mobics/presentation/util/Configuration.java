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

    /**
     * The Configuration instance property.
     */
    public static final Configuration INSTANCE = new Configuration();

    private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

    // Main configurations

    private final String meterPattern = "#";

    private final String kilometerPattern = "#,##0.0";

    private final String currencyPattern = "#,##0.00";

    private final String dateTimePattern = "dd/MM/yyyy HH:mm";

    private final String jsDatePattern = "dd/mm/yy";
    
    private final String jsTimePattern = "HH:ii";

    private final String authenticationFailureString = "Failed Authentication";

    private final String carClubEndpoint = "http://mobics02.critical.pt:8080/mobics-webservices/CarClub";
    
    private final String carEndpoint = "http://mobics02.critical.pt:8080/mobics-webservices/Car";

    private final String fleetEndpoint = "http://mobics02.critical.pt:8080/mobics-webservices/Fleet";

    private final String bookingEndpoint = "http://mobics02.critical.pt:8080/mobics-webservices/Booking";

    private final String customerEndpoint = "http://mobics02.critical.pt:8080/mobics-webservices/Customer";

    private final String miscellaneousEnpoint = "http://mobics02.critical.pt:8080/mobics-webservices/Miscellaneous";

    private final String geolocationServer = "nominatim.openstreetmap.org";

    private final int carThumbnailWidth = 58;

    private final int carThumbnailHeight = 58;

    private final int minResults = 1;

    private final int maxResults = 99;
    
    private final int recentsActivitiesFilterMonthGap = 6;
    
    private final String helpdeskPhone = "219999000";
    
    private final String helpdeskEmail = "helpdesk@mobiag.pt";

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
    public int getCarThumbnailWidth() {
        return Integer.valueOf(getValue("mobics.config.thumbnail.width", carThumbnailWidth));
    }

    /**
     * @return the carThumbnailHeight
     */
    public int getCarThumbnailHeight() {
        return Integer.valueOf(getValue("mobics.config.thumbnail.height", carThumbnailHeight));
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
        return Integer.valueOf(getValue("mobics.config.recents.filter.month.gap", recentsActivitiesFilterMonthGap));
    }
    
    /**
     * @return the helpdeskPhone
     */
    public String getHelpdeskPhone() {
        return getValue("mobics.config.helpdesk.phone", helpdeskPhone);
    }

    /**
     * @return the helpdeskEmail
     */
    public String getHelpdeskEmail() {
        return getValue("mobics.config.helpdesk.email", helpdeskEmail);
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
