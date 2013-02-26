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

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import com.criticalsoftware.mobics.booking.BookingInterestDTO;
import com.criticalsoftware.mobics.presentation.action.recent.RecentActivitiesActionBean;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.proxy.booking.BookingInterestNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.booking.BookingValidationExceptionException;
import com.criticalsoftware.mobics.proxy.booking.BookingWSServiceStub;
import com.criticalsoftware.mobics.proxy.booking.CarClassNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.booking.CustomerNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.booking.ExpiredBookingInterestExceptionException;
import com.criticalsoftware.mobics.proxy.booking.IllegalDateExceptionException;
import com.criticalsoftware.mobics.proxy.booking.OverlappedCarBookingExceptionException;

/**
 * @author ltiago
 * @version $Revision: $
 */
public class EditBookingInterestActionBean extends BookingInterestActionBean {

    @Validate(required = true, on = { "main", "editBookingInterest" })
    private String activityCode;

    /**
     * Edit booking interest page
     * 
     * @return resolution
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws BookingInterestNotFoundExceptionException
     */
    @DontValidate
    @DefaultHandler
    public Resolution main() throws RemoteException, UnsupportedEncodingException,
            BookingInterestNotFoundExceptionException {

        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));

        BookingInterestDTO booking = bookingWSServiceStub.getBookingInterest(activityCode);

        activityCode = booking.getCode();
        startDate = booking.getPickupDate().getTime();
        latitude = booking.getLocation().getLatitude().toString();
        longitude = booking.getLocation().getLongitude().toString();
        address = booking.getLocationName();
        distance = booking.getRadius();
        carClazz = booking.getCarClass();
        fromMyCarClub = booking.getCarClubCarsOnly();
        startSending = booking.getNotificationStartTime();
        stopSending = booking.getNotificationStopTime();
        maxMessages = booking.getNumOfNotifications();

        return new ForwardResolution("/WEB-INF/recent/editBookingInterest.jsp");
    }

    /**
     * Resolution to return from map to edit page
     * 
     * @return resolution
     */
    public Resolution returnToEdit() {
        return new ForwardResolution("/WEB-INF/recent/editBookingInterest.jsp");
    }

    /**
     * Save edited booking interest
     * 
     * @return resolution
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws ExpiredBookingInterestExceptionException
     * @throws BookingInterestNotFoundExceptionException
     * @throws OverlappedCarBookingExceptionException
     * @throws CarClassNotFoundExceptionException
     * @throws CustomerNotFoundExceptionException
     * @throws BookingValidationExceptionException
     * @throws IllegalDateExceptionException
     * @throws InvalidBookingInterestUpdateExceptionException
     */
    @Validate
    public Resolution editBookingInterest() throws RemoteException, UnsupportedEncodingException,
            BookingInterestNotFoundExceptionException, OverlappedCarBookingExceptionException,
            CarClassNotFoundExceptionException, CustomerNotFoundExceptionException,
            BookingValidationExceptionException, IllegalDateExceptionException,
            ExpiredBookingInterestExceptionException, InvalidBookingInterestUpdateExceptionException {
        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);

        bookingWSServiceStub.updateBookingInterest(activityCode, getContext().getUser().getCarClub().getCarClubCode(),
                start, address, carClazz, getContext().getUser().getCarClub().getIsStandalone() ? true : fromMyCarClub,
                new BigDecimal(longitude), new BigDecimal(latitude), distance != null ? distance.intValue()
                        : Configuration.INSTANCE.getAnyDistance(), startSending.intValue(), stopSending.intValue(),
                maxMessages.intValue(), Calendar.getInstance());

        getContext().getMessages().add(new LocalizableMessage("find.car.later.interest.updated"));
        return new RedirectResolution(RecentActivitiesActionBean.class).flash(this);
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

}
