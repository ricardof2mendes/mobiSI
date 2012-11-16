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
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.ajax.JavaScriptResolution;
import net.sourceforge.stripes.validation.Validate;

import com.criticalsoftware.mobics.presentation.action.BaseActionBean;
import com.criticalsoftware.mobics.presentation.action.recent.RecentActivitiesActionBean;
import com.criticalsoftware.mobics.presentation.extension.DatetimeTypeConverter;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.presentation.util.GeolocationUtil;
import com.criticalsoftware.mobics.proxy.booking.BookingValidationExceptionException;
import com.criticalsoftware.mobics.proxy.booking.BookingWSServiceStub;
import com.criticalsoftware.mobics.proxy.booking.CarClassNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.booking.CustomerNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.booking.IllegalDateExceptionException;
import com.criticalsoftware.mobics.proxy.booking.OverlappedCarBookingExceptionException;

/**
 * @author ltiago
 * @version $Revision: $
 */
public class FindCarForLaterActionBean extends BaseActionBean {

    @Validate(required = true, on = "createBookingInterest", converter = DatetimeTypeConverter.class)
    private Date startDate;

    @Validate(required = true, on = "createBookingInterest")
    private String address;

    @Validate(required = true, on = "createBookingInterest")
    private Integer distance;

    @Validate(required = true, on = "createBookingInterest")
    private String carClazz;

    @Validate(required = true, on = "createBookingInterest")
    private boolean fromMyCarClub;

    @Validate(required = true, on = "createBookingInterest")
    private Integer startSending;

    @Validate(required = true, on = "createBookingInterest")
    private Integer stopSending;

    @Validate(required = true, on = "createBookingInterest")
    private Integer maxMessages;

    @Validate(required = true, on = { "main", "createBookingInterest" })
    private String latitude;

    @Validate(required = true, on = { "main", "createBookingInterest" })
    private String longitude;

    @Validate(required = true, on = "searchAdress")
    private String query;

    private static final Integer START_SENDING = 30;
    
    private static final Integer STOP_SENDING = 5;

    private static final Integer DISTANCE = 3000;
    
    private static final Integer MAX_MESSAGES = 10;

    @DontValidate
    @DefaultHandler
    public Resolution main() throws RemoteException {
        if (startSending == null) {
            startSending = START_SENDING;
        }
        if (stopSending == null) {
            stopSending = STOP_SENDING;
        }
        if (startDate == null) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) + 1);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            startDate = new Date(c.getTimeInMillis());
        }
        if(maxMessages == null){
            maxMessages = MAX_MESSAGES;
        }
        if(distance == null) {
            distance = DISTANCE;
        }
        return new ForwardResolution("/WEB-INF/book/findCarForLater.jsp");
    }

    public Resolution createBookingInterest() throws RemoteException, UnsupportedEncodingException,
            OverlappedCarBookingExceptionException, CarClassNotFoundExceptionException,
            CustomerNotFoundExceptionException, BookingValidationExceptionException,
            IllegalDateExceptionException {
        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);

        bookingWSServiceStub.createBookingInterest(getContext().getUser().getCarClubCode(), start, carClazz,
                fromMyCarClub, new BigDecimal(longitude), new BigDecimal(latitude), distance.intValue(),
                startSending.intValue(), stopSending.intValue(), maxMessages.intValue(), Calendar.getInstance());

        getContext().getMessages().add(new LocalizableMessage("find.car.later.interest.created"));
        return new RedirectResolution(RecentActivitiesActionBean.class).flash(this);
    }

    public Resolution searchLocation() {
        return new ForwardResolution("/WEB-INF/book/streetLocation.jsp").addParameter("search", "street");
    }

    public Resolution searchAdress() {
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

}