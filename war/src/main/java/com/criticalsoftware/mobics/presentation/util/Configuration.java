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

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ltiago
 * @version $Revision: $
 */
public class Configuration {
    
    /**
     * The Configuration instance property.
     */
    public static final Configuration INSTANCE = new Configuration();

    private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

    // Main configurations
    private final String encoding = "UTF-8";

    private final String locale = "pt";
    
    private final String meterPattern = "#";

    private final String kilometerPattern = "#,##0.0";
    
    private final String currencyPattern = "#,##0.00";

    private final String authenticationFailureString = "Failed Authentication";

    private final String carClubEndpoint = "http://mobics02.critical.pt:8080/mobics-webservices/CarClub";
    
    private final String fleetEndpoint = "http://mobics02.critical.pt:8080/mobics-webservices/Fleet";
    
    private final String bookingEndpoint = "http://mobics02.critical.pt:8080/mobics-webservices/Booking";
    
    private final String customerEndpoint = "http://mobics02.critical.pt:8080/mobics-webservices/Customer";
    
    private final String miscellaneousEnpoint = "http://mobics02.critical.pt:8080/mobics-webservices/Miscellaneous";
    
    private final String geolocationServer = "nominatim.openstreetmap.org";

    private final int carThumbnailWidth = 58;
    
    private final int carThumbnailHeight = 58;
    
    private final int minResults = 1;
    
    private final int maxResults = 99;
    
    /**
     * Gets the encoding.
     * 
     * @return the encoding
     */
    public String getEncoding() {
        return getValue("mobics.config.encoding", encoding);
    }

    /**
     * Gets the locale.
     * 
     * @param culture the user's culture
     * @return the locale
     */
    public Locale getLocale(String culture) {
        return new Locale(getValue("mobics.config.locale." + culture, locale));
    }
    
    /**
     * @return the locale
     */
    public String getLocale() {
        return  getValue("mobics.config.locale", locale);
    }
    
    /**
     * @return the meterPattern
     */
    public String getMeterPattern() {
        return meterPattern;
    }

    /**
     * @return the kilometerPattern
     */
    public String getKilometerPattern() {
        return kilometerPattern;
    }

    /**
     * @return the currencyPattern
     */
    public String getCurrencyPattern() {
        return currencyPattern;
    }

    /**
     * @return the authenticationFailureString
     */
    public String getAuthenticationFailureString() {
        return  getValue("mobics.config.authentication.failure.string",authenticationFailureString);
    }

    /**
     * @return the carClubEndpoint
     */
    public String getCarClubEndpoint() {
        return  getValue("mobics.config.endpoint.carclub", carClubEndpoint);
    }

    /**
     * @return the fleetEndpoint
     */
    public String getFleetEndpoint() {
        return  getValue("mobics.config.endpoint.fleet", fleetEndpoint);
    }

    /**
     * @return the bookingEndpoint
     */
    public String getBookingEndpoint() {
        return  getValue("mobics.config.endpoint.booking", bookingEndpoint);
    }

    /**
     * @return the customerEndpoint
     */
    public String getCustomerEndpoint() {
        return  getValue("mobics.config.endpoint.customer", customerEndpoint);
    }

    /**
     * @return the miscellaneousEnpoint
     */
    public String getMiscellaneousEnpoint() {
        return  getValue("mobics.config.endpoint.miscellaneous", miscellaneousEnpoint);
    }

    /**
     * @return the geolocationServer
     */
    public String getGeolocationServer() {
        return  getValue("mobics.config.geolocation.server", geolocationServer);
    }

    /**
     * @return the carThumbnailWidth
     */
    public int getCarThumbnailWidth() {
        return  Integer.valueOf(getValue("mobics.config.thumbnail.width", carThumbnailWidth));
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
