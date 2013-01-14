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
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Calendar;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
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
import com.criticalsoftware.mobics.proxy.booking.BookingInterestNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.booking.BookingNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.booking.BookingWSServiceStub;
import com.criticalsoftware.mobics.proxy.booking.CustomerNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.booking.UnauthorizedCustomerExceptionException;

/**
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class RecentActivitiesActionBean extends BaseActionBean {

    private CustomerActivityListDTO[] recents;

    private TripDetailsDTO trip;

    private BookingInterestDTO booking;

    private BigDecimal time;

    @Validate(required = true, on = { "tripDetails", "bookingDetails", "advanceBookingDetails",
            "cancelAdvanceBooking"})
    private String activityCode;

    /**
     * Main handler
     * 
     * @return a page resolution
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws CustomerNotFoundExceptionException
     */
    @DefaultHandler
    @DontValidate
    public Resolution main() throws RemoteException, UnsupportedEncodingException, CustomerNotFoundExceptionException {

        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));
        Calendar begin = Calendar.getInstance(), end = Calendar.getInstance();
        begin.add(Calendar.MONTH, -Configuration.INSTANCE.getRecentActivitiesFilterMonthGap());
        end.add(Calendar.MONTH, Configuration.INSTANCE.getRecentActivitiesFilterMonthGap());

        recents = bookingWSServiceStub.getRecentActivities(begin, end, CustomerActivityEnum.ALL.getValue());

        return new ForwardResolution("/WEB-INF/recent/recentActivities.jsp");
    }

    /**
     * Trip details
     * 
     * @return a page resolution
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws BookingNotFoundExceptionException
     */
    public Resolution tripDetails() throws RemoteException, UnsupportedEncodingException,
            BookingNotFoundExceptionException {
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
     * @return a page resolution
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws BookingNotFoundExceptionException
     */
    public Resolution advanceBookingDetails() throws RemoteException, UnsupportedEncodingException,
            BookingNotFoundExceptionException {
        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));

        trip = bookingWSServiceStub.getTripDetails(activityCode);
        // FIXME falta um servico para devolver o tempo
        time = new BigDecimal(3600);

        return new ForwardResolution("/WEB-INF/recent/advanceBookingDetails.jsp");
    }

    /**
     * Cancel advance booking
     * 
     * @return a page resolution
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws BookingNotFoundExceptionException
     * @throws CustomerNotFoundExceptionException
     * @throws UnauthorizedCustomerExceptionException
     */
    public Resolution cancelAdvanceBooking() throws RemoteException, UnsupportedEncodingException,
            BookingNotFoundExceptionException, CustomerNotFoundExceptionException,
            UnauthorizedCustomerExceptionException {
        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));

        bookingWSServiceStub.cancelAdvanceBooking(activityCode);
        
        getContext().getMessages().add(new LocalizableMessage("trip.detail.advance.booking.cancel.success"));

        return new RedirectResolution(this.getClass()).flash(this);
    }

    /**
     * Booking interest details
     * 
     * @return
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws BookingNotFoundExceptionException
     */
    public Resolution interestDetails() throws RemoteException, UnsupportedEncodingException,
            BookingInterestNotFoundExceptionException {
        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));

        booking = bookingWSServiceStub.getBookingInterest(activityCode);

        return new ForwardResolution("/WEB-INF/recent/interestBookingDetails.jsp");
    }

    /**
     * Get trip start location
     * 
     * @return the adress string
     */
    public String getStartLocation() {
        String location = new LocalizableMessage("application.value.not.available")
                .getMessage(getContext().getLocale());
        if (trip.getStartLatitude() != null && trip.getStartLongitude() != null) {
            location = GeolocationUtil.getAddressFromCoordinates(trip.getStartLatitude().toString(), trip
                    .getStartLongitude().toString());
        }
        return location;
    }

    /**
     * Get trip end location
     * 
     * @return the adress string
     */
    public String getEndLocation() {
        String location = new LocalizableMessage("application.value.not.available")
                .getMessage(getContext().getLocale());
        if (trip.getEndtLatitude() != null && trip.getEndLongitude() != null) {
            location = GeolocationUtil.getAddressFromCoordinates(trip.getEndtLatitude().toString(), trip
                    .getEndLongitude().toString());
        }
        return location;
    }
    
    /**
     * Get the booking car location
     * @return
     */
    public String getBookingLocation() {
        String location = new LocalizableMessage("application.value.not.available")
                .getMessage(getContext().getLocale());
        if (booking.getLocation()!= null && booking.getLocation().getLatitude() != null && booking.getLocation().getLongitude() != null) {
            location = GeolocationUtil.getAddressFromCoordinates(booking.getLocation().getLatitude().toString(), booking.getLocation().getLongitude().toString());
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

    /**
     * @return the time
     */
    public BigDecimal getTime() {
        return time;
    }

}
