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
package com.criticalsoftware.mobics.presentation.action.booking;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.ajax.JavaScriptResolution;
import net.sourceforge.stripes.validation.Validate;

import com.criticalsoftware.mobics.presentation.action.BaseActionBean;
import com.criticalsoftware.mobics.presentation.extension.DatetimeTypeConverter;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.GeolocationUtil;

/**
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class BookingInterestActionBean extends BaseActionBean {

    @Validate(required = true, on = {"createBookingInterest", "editBookingInterest"}, converter = DatetimeTypeConverter.class)
    protected Date startDate;

    @Validate(required = true, on = {"createBookingInterest", "editBookingInterest"})
    protected Integer distance;

    @Validate
    protected String carClazz;

    @Validate
    protected Boolean fromMyCarClub;

    @Validate(required = true, on = {"createBookingInterest", "editBookingInterest"})
    protected Integer startSending;

    @Validate(required = true, on = {"createBookingInterest", "editBookingInterest"})
    protected Integer stopSending;

    @Validate(required = true, on = {"createBookingInterest", "editBookingInterest"}, maxvalue = 99, maxlength = 2)
    protected Integer maxMessages;

    @Validate
    protected String address;

    @Validate
    protected String latitude;

    @Validate
    protected String longitude;

    @Validate
    protected String query;
    
    /**
     * Search a location
     * 
     * @return
     */
    public Resolution searchLocation() {
        return new ForwardResolution("/WEB-INF/map/mapSearchStreet.jsp").addParameter("search", "street");
    }

    /**
     * Search adress in geolocation
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public Resolution getAddressFromQuery() throws UnsupportedEncodingException {
        getContext().getResponse().setHeader("Stripes-Success", "OK");
        return new JavaScriptResolution(GeolocationUtil.getAddressFromText(query));
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String location) {
        this.address = location;
    }

    /**
     * @return the distance
     */
    public Integer getDistance() {
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    /**
     * @return the fromMyCarClub
     */
    public Boolean getFromMyCarClub() {
        return fromMyCarClub;
    }

    /**
     * @param fromMyCarClub the fromMyCarClub to set
     */
    public void setFromMyCarClub(Boolean fromMyCarClub) {
        this.fromMyCarClub = fromMyCarClub;
    }

    /**
     * @return the startSending
     */
    public Integer getStartSending() {
        return startSending;
    }

    /**
     * @param startSending the startSending to set
     */
    public void setStartSending(Integer startSending) {
        this.startSending = startSending;
    }

    /**
     * @return the stopSending
     */
    public Integer getStopSending() {
        return stopSending;
    }

    /**
     * @param stopSending the stopSending to set
     */
    public void setStopSending(Integer stopSending) {
        this.stopSending = stopSending;
    }

    /**
     * @return the maxMessages
     */
    public Integer getMaxMessages() {
        return maxMessages;
    }

    /**
     * @param maxMessages the maxMessages to set
     */
    public void setMaxMessages(Integer maxMessages) {
        this.maxMessages = maxMessages;
    }

    /**
     * @return the latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    /**
     * @param query the query to set
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * @param fromMyCarClub the fromMyCarClub to set
     */
    public void setFromMyCarClub(boolean fromMyCarClub) {
        this.fromMyCarClub = fromMyCarClub;
    }

    /**
     * @return the carClazz
     */
    public String getCarClazz() {
        return carClazz;
    }

    /**
     * @param carClazz the carClazz to set
     */
    public void setCarClazz(String carClazz) {
        this.carClazz = carClazz;
    }
}
