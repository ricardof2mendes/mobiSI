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
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.criticalsoftware.www.mobios.country.configuration.CountryConfiguration;

/**
 * @author ltiago
 * @version $Revision: $
 */
public class Configuration implements Serializable{
    /**
     * Application state
     * 
     * @author ltiago
     * @version $Revision: $
     */
    public enum ApplicationState {
        PRODUCTION, DEVELOPMENT, DEVELOPMENT_WITH_JREBEL
    }

    /** */
    private static final long serialVersionUID = 7216539220608643618L;

    private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

    public static final String CCOME_CLASS = "com.criticalsoftware.mobios.hal.drivers.obsconvadis.ConvadisEDriverProvider";
    
    public static final boolean CCOME_MODE_ACTIVATED = false;
    
    /**
     * The Configuration instance property.
     */
    public static final Configuration INSTANCE = new Configuration();
    
    /** Application state */
    private final ApplicationState applicationState = ApplicationState.DEVELOPMENT_WITH_JREBEL;

    /** Application Encoding **/
    private final String uriEnconding = "UTF-8";
    
    /** Zip Code Pattern **/
    private final String zipCodePattern = "^[0-9]{4}-[0-9]{3}$";
    
    /** Meter Pattern **/
    private final String meterPattern = "#";

    /** Kilometer Pattern **/
    private final String kilometerPattern = "#,##0.0";

    /** Currency Pattern **/
    private final String currencyPattern = "#,##0.00";

    /** Date time **/
    private final String datePattern = "dd/MM/yyyy";

    /** Date time Pattern **/
    private final String dateTimePattern = "dd/MM/yyyy HH:mm";
    
    /** Date time Pattern **/
    private final String timePattern = "HH:mm";

    /** Javascript date time pattern moment.js**/
    private final String jsDateTimePattern = "DD/MM/YYYY HH:mm";
    
    /** Javascript date pattern **/
    private final String jsDatePattern = "dd/mm/yy";

    /** Javascript time pattern **/
    private final String jsTimePattern = "HH:ii";

    private final String weekPattern = "E";

    /** Authentication failure string return by webservices on Axis fault **/
    private final String authenticationFailureString = "Failed Authentication";

    /** Car Club Endpoint **/
    private final String carClubEndpoint = "http://176.111.104.20/mobics-webservices/CarClub";
    
    /** Car Endpoint **/
    private final String carEndpoint = "http://176.111.104.20/mobics-webservices/Car";

    /** Fleet Endpoint **/
    private final String fleetEndpoint = "http://176.111.104.20/mobics-webservices/Fleet";

    /** Booking Endpoint **/
    private final String bookingEndpoint = "http://176.111.104.20/mobics-webservices/Booking";

    /** Customer Endpoint **/
    private final String customerEndpoint = "http://176.111.104.20/mobics-webservices/Customer";

    /** Miscellaneous Endpoint **/
    private final String miscellaneousEnpoint = "http://176.111.104.20/mobics-webservices/Miscellaneous";

    /** Billing Endpoint **/
    private final String billingEndpoint = "http://176.111.104.20/mobics-webservices/Billing";

    /** Geolocation servce **/
    private final String geolocationServer = "nominatim.openstreetmap.org";

    /** Max results **/
    private final int maxResults = 99;
    
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
    private final int pollingIntervalMilliseconds = 15000;
    
    /** State polling timeout in milliseconds **/
    private final int statePollingTimeoutMilliseconds = 120000;
    
    /** State polling timeout in milliseconds **/
    private final int unlockPollingTimeoutMilliseconds = 60000;
    
    /** State polling timeout in milliseconds **/
    private final int lockEndPollingTimeoutMilliseconds = 60000;

    private Map<String, String> carClubConfiguration = new HashMap<String, String>();

    public void setCarClubConfiguration(CountryConfiguration carClubConfiguration){
        this.carClubConfiguration.put("mobics.config.zipcode.pattern", carClubConfiguration.getPatterns().getPostalCodeValidatorPattern());
        
        this.carClubConfiguration.put("mobics.config.meter.pattern", carClubConfiguration.getPatterns().getShortDistancePattern());
        this.carClubConfiguration.put("mobics.config.meter.string.pattern",
                                      carClubConfiguration.getPatterns().getShortDistanceConversionPattern());

        this.carClubConfiguration.put("mobics.config.kilometer.pattern", carClubConfiguration.getPatterns().getDistancePattern());
        this.carClubConfiguration.put("mobics.config.kilometer.string.pattern",
                                      carClubConfiguration.getPatterns().getDistanceConversionPattern());

        this.carClubConfiguration.put("mobics.config.currency.pattern", carClubConfiguration.getPatterns().getCurrencyPattern());
        this.carClubConfiguration.put("mobics.config.currency.symbol", carClubConfiguration.getCurrencySymbol());

        this.carClubConfiguration.put("mobics.config.date.pattern", carClubConfiguration.getPatterns().getDatePattern());
        this.carClubConfiguration.put("mobics.config.datetime.pattern", carClubConfiguration.getPatterns().getTimestampPattern());
        this.carClubConfiguration.put("mobics.config.time.pattern", carClubConfiguration.getPatterns().getHoursMinutesPattern());
        this.carClubConfiguration.put("mobics.config.week.pattern", carClubConfiguration.getPatterns().getWeekPattern());

        this.carClubConfiguration.put("mobics.config.js.date.pattern", carClubConfiguration.getPatterns().getJsDatePattern());
        this.carClubConfiguration.put("mobics.config.js.date.time.pattern", carClubConfiguration.getPatterns().getJsDateTimePattern());
        this.carClubConfiguration.put("mobics.config.js.time.pattern", carClubConfiguration.getPatterns().getJsTimePattern());
    }

    public Map<String, String> getCarClubConfiguration(){
        return this.carClubConfiguration;
    }

    /**
     * @return the uriEnconding
     */
    public String getUriEnconding() {
        return getValue("org.apache.catalina.connector.URI_ENCODING", uriEnconding);
    }
    
    /**
     * @return the applicationState
     */
    public ApplicationState getApplicationState() {
        return ApplicationState.valueOf(getValue("mobics.config.application.state", applicationState.name()));
    }

    /**
     * @return the zipCodePattern
     */
    public String getZipCodePattern() {
        return getPattern("mobics.config.zipcode.pattern", zipCodePattern);
    }
    
    /**
     * @return the meterPattern
     */
    public String getMeterPattern() {
        return getPattern("mobics.config.meter.pattern", meterPattern);
    }

    /**
     * Get the week pattern
     * @return
     */
    public String getWeekPattern() {
        return getPattern("mobics.config.week.pattern", weekPattern);
    }

    /**
     * @return the kilometerPattern
     */
    public String getKilometerPattern() {
        return getPattern("mobics.config.kilometer.pattern", kilometerPattern);
    }

    /**
     * @return the currencyPattern
     */
    public String getCurrencyPattern() {
        return getPattern("mobics.config.currency.pattern", currencyPattern);
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
     * @return the billing endpoint
     */
    public String getBillingEndpoint() {
        return getValue("mobics.config.endpoint.billing", billingEndpoint);
    }

    /**
     * @return the geolocationServer
     */
    public String getGeolocationServer() {
        return getValue("mobics.config.geolocation.server", geolocationServer);
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
        return getPattern("mobics.config.js.date.pattern", jsDatePattern);
    }
    
    /**
     * @return the dateTimePattern
     */
    public String getJsDateTimePattern() {
        return getPattern("mobics.config.js.date.time.pattern", jsDateTimePattern);
    }

    /**
     * @return the timePattern
     */
    public String getJsTimePattern() {
        return getPattern("mobics.config.js.time.pattern", jsTimePattern);
    }

    /**
     * @return the dateTimePattern
     */
    public String getDateTimePattern() {
        return getPattern("mobics.config.datetime.pattern", dateTimePattern);
    }

    /**
     * @return the datePattern
     */
    public String getDatePattern() {
        return getPattern("mobics.config.date.pattern", datePattern);
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
        return getPattern("mobics.config.time.pattern", timePattern);
    }

    /**
     * @return the anyDistance
     */
    public int getAnyDistance() {
        return Integer.valueOf(getValue("mobics.config.radius.any.distance", anyDistance));
    }
    
    /**
     * @return the pollingIntervalMilliseconds
     */
    public int getPollingIntervalMilliseconds() {
        return Integer.valueOf(getValue("mobics.config.polling.interval.milliseconds", pollingIntervalMilliseconds));
    }

    /**
     * @return the statePollingTimeoutMilliseconds
     */
    public int getStatePollingTimeoutMilliseconds() {
        return Integer.valueOf(getValue("mobics.config.state.polling.timeout.milliseconds", statePollingTimeoutMilliseconds));
    }
    
    /**
     * @return the unlockPollingTimeoutMilliseconds
     */
    public int getUnlockPollingTimeoutMilliseconds() {
        return Integer.valueOf(getValue("mobics.config.unlock.polling.timeout.milliseconds", unlockPollingTimeoutMilliseconds));
    }

    /**
     * @return the lockEndPollingTimeoutMilliseconds
     */
    public int getLockEndPollingTimeoutMilliseconds() {
        return Integer.valueOf(getValue("mobics.config.lock.end.polling.timeout.milliseconds", lockEndPollingTimeoutMilliseconds));
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

    private String getPattern(String key, Object defaultValue) {
        String value = String.valueOf(defaultValue);

        if (carClubConfiguration == null || carClubConfiguration.get(key) == null) {
            LOGGER.warn("Car Club Pattern '{}' does not exist or is empty.", key);
        } else {
            value = carClubConfiguration.get(key);
        }

        return value;
    }

}
