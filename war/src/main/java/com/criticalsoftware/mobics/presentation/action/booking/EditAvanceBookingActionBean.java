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
import java.util.HashMap;
import java.util.Map;

import com.criticalsoftware.mobics.booking.TripDetailsDTO;
import com.criticalsoftware.mobics.presentation.action.recent.RecentActivitiesActionBean;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.presentation.util.DataConversionUtils;
import com.criticalsoftware.mobics.proxy.booking.BookingNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.booking.BookingValidationExceptionException;
import com.criticalsoftware.mobics.proxy.booking.BookingWSServiceStub;
import com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.booking.CustomerNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.booking.InvalidCustomerPinExceptionException;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

/**
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class EditAvanceBookingActionBean extends AdvanceBookingActionBean {

    private TripDetailsDTO trip;

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
     * @throws CustomerNotFoundExceptionException
     */
    @DefaultHandler
    public Resolution editAdvanceBooking() throws RemoteException, UnsupportedEncodingException,
    BookingNotFoundExceptionException, CustomerNotFoundExceptionException {
        final BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));
        trip = bookingWSServiceStub.getTripDetails(activityCode);

        final long time = bookingWSServiceStub.getNextAdvanceBooking(activityCode);
        Calendar c = null;
        if(time > 0) {
            c = Calendar.getInstance();
            c.setTimeInMillis(time);
        }
        DataConversionUtils dataConversionUtils = new DataConversionUtils(getContext());
        extendBookingDate = c == null ? null : dataConversionUtils.fromUTC(c.getTime());

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
        final BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));
        final Calendar c = Calendar.getInstance();
        DataConversionUtils dataConversionUtils = new DataConversionUtils(getContext());
        c.setTime(dataConversionUtils.toUTC(endDate));
        bookingWSServiceStub.extendAdvanceBooking(activityCode, c.getTimeInMillis());

        final Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("activityCode", activityCode);
        parameters.put("extended", "true");

        return new RedirectResolution(RecentActivitiesActionBean.class, "advanceBookingDetails").addParameters(parameters).flash(this);
    }

    /**
     * @return the endDate
     */
    @Override
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    @Override
    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the trip
     */
    public TripDetailsDTO getTrip() {
        return trip;
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
    public void setActivityCode(final String activityCode) {
        this.activityCode = activityCode;
    }
}
