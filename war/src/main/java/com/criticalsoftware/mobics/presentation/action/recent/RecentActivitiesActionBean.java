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
package com.criticalsoftware.mobics.presentation.action.recent;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Locale;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import com.criticalsoftware.mobics.booking.BookingInterestDTO;
import com.criticalsoftware.mobics.booking.CustomerActivityEnum;
import com.criticalsoftware.mobics.booking.CustomerActivityListDTO;
import com.criticalsoftware.mobics.booking.TripDetailsDTO;
import com.criticalsoftware.mobics.presentation.action.BaseActionBean;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.presentation.util.GeolocationUtil;
import com.criticalsoftware.mobics.proxy.booking.BookingNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.booking.BookingWSServiceStub;
import com.criticalsoftware.mobics.proxy.booking.CustomerNotFoundExceptionException;

/**
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class RecentActivitiesActionBean extends BaseActionBean {

    private CustomerActivityListDTO[] recents;
    
    private TripDetailsDTO trip;
    
    private BookingInterestDTO booking;

    @Validate(required = true, on = {"tripDetails", "bookingDetails"})
    private String activityCode;

    @DefaultHandler
    @DontValidate
    public Resolution main() throws RemoteException, UnsupportedEncodingException, CustomerNotFoundExceptionException {

        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));
        Calendar begin = Calendar.getInstance(), end = Calendar.getInstance();
        begin.add(Calendar.YEAR, - Configuration.INSTANCE.getRecentActivitiesFilterMonthGap());
        end.add(Calendar.YEAR, Configuration.INSTANCE.getRecentActivitiesFilterMonthGap());

        recents = bookingWSServiceStub.getRecentActivities(begin, end,
                CustomerActivityEnum.ALL.getValue());

        return new ForwardResolution("/WEB-INF/recent/recentActivities.jsp");
    }
    
    /**
     * Trip details
     * 
     * @return
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws BookingNotFoundExceptionException
     */
    public Resolution tripDetails() throws RemoteException, UnsupportedEncodingException, BookingNotFoundExceptionException{
        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));
        trip = bookingWSServiceStub.getTripDetails(activityCode);
        
        return new ForwardResolution("/WEB-INF/recent/tripDetails.jsp");
    }

    /**
     * Booking interest details
     * 
     * @return
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws BookingNotFoundExceptionException
     */
    public Resolution bookingDetails() throws RemoteException, UnsupportedEncodingException, BookingNotFoundExceptionException{
        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));
        
        // FIXME get booking
        // trip = bookingWSServiceStub.getAdvanceBookingDetails(activityCode);
        
        return new ForwardResolution("/WEB-INF/recent/bookingDetails.jsp");
    }
    
    /**
     * Booking interest details
     * 
     * @return
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws BookingNotFoundExceptionException
     */
    public Resolution interestDetails() throws RemoteException, UnsupportedEncodingException, BookingNotFoundExceptionException{
        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));
        
        bookingWSServiceStub.getBookingInterest(activityCode);
        
        return new ForwardResolution("/WEB-INF/recent/interestDetails.jsp");
    }
    
    /**
     * Get start location
     * 
     * @return the adress string
     */
    public String getStartLocation() {
        String location = new LocalizableMessage("application.value.not.available").getMessage(getContext().getLocale());
        if (trip.getStartLatitude() != null && trip.getStartLongitude() != null) {
            location = GeolocationUtil.getAddressFromCoordinates(trip.getStartLatitude().toString(), trip.getStartLongitude()
                    .toString());
        }
        return location;
    }

    /**
     * Get end location
     * 
     * @return the adress string
     */
    public String getEndLocation() {
        String location = new LocalizableMessage("application.value.not.available").getMessage(getContext().getLocale());
        if (trip.getEndtLatitude() != null && trip.getEndLongitude() != null) {
            location = GeolocationUtil.getAddressFromCoordinates(trip.getEndtLatitude().toString(), trip.getEndLongitude()
                    .toString());
        }
        return location;
    }

    
    /**
     * @return the activityCode
     */
    public String getActivityCode() {
        return activityCode;
    }

    /**
     * @param activityCode the activityCode to set
     */
    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }
    
    /**
     * @return the recents
     */
    public CustomerActivityListDTO[] getRecents() {
        return recents;
    }

    /**
     * @return the trip
     */
    public TripDetailsDTO getTrip() {
        return trip;
    }

    /**
     * @return the booking
     */
    public BookingInterestDTO getBooking() {
        return booking;
    }
}
