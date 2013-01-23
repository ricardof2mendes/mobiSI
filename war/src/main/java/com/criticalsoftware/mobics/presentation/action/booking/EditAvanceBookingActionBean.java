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
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import com.criticalsoftware.mobics.booking.BookingInterestDTO;
import com.criticalsoftware.mobics.booking.TripDetailsDTO;
import com.criticalsoftware.mobics.presentation.action.recent.RecentActivitiesActionBean;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.proxy.booking.BookingNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.booking.BookingValidationExceptionException;
import com.criticalsoftware.mobics.proxy.booking.BookingWSServiceStub;
import com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.booking.InvalidCustomerPinExceptionException;

/**
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class EditAvanceBookingActionBean extends AdvanceBookingActionBean {

    private TripDetailsDTO trip;

    private BookingInterestDTO booking;

    private Date extendBookingDate;

    @Validate(required = true, on = "saveAdvanceBooking")
    private Date endDate;

    @Validate(required = true, on = { "editAdvanceBooking", "saveAdvanceBooking" })
    private String activityCode;

    /**
     * Show Edition page
     * 
     * @return
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws BookingNotFoundExceptionException
     */
    @DefaultHandler
    public Resolution editAdvanceBooking() throws RemoteException, UnsupportedEncodingException,
            BookingNotFoundExceptionException {
        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));
        trip = bookingWSServiceStub.getTripDetails(activityCode);

        Calendar c = bookingWSServiceStub.getNextAdvanceBooking(activityCode);
        System.out.println(c.getTime());
        extendBookingDate = c == null ? null : c.getTime();

        return new ForwardResolution("/WEB-INF/recent/editAdvanceBookingDetails.jsp");
    }

    /**
     * Save the advance booking
     * 
     * @return
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws BookingNotFoundExceptionException
     * @throws BookingValidationExceptionException
     * @throws InvalidCustomerPinExceptionException
     * @throws CarLicensePlateNotFoundExceptionException
     */
    public Resolution saveAdvanceBooking() throws RemoteException, UnsupportedEncodingException,
            BookingNotFoundExceptionException, BookingValidationExceptionException,
            InvalidCustomerPinExceptionException, CarLicensePlateNotFoundExceptionException {
        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));
        Calendar c = Calendar.getInstance();
        c.setTime(endDate);
        bookingWSServiceStub.extendAdvanceBooking(activityCode, c);

        getContext().getMessages().add(new LocalizableMessage("trip.detail.advance.booking.edit.success"));

        return new RedirectResolution(RecentActivitiesActionBean.class, "advanceBookingDetails").addParameter("activityCode",
                activityCode).flash(this);
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
     * @return the extendBookingDate
     */
    public Date getExtendBookingDate() {
        return extendBookingDate;
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
