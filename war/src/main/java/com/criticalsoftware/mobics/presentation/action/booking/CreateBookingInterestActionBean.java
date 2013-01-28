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
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;
import net.sourceforge.stripes.validation.ValidationState;

import com.criticalsoftware.mobics.presentation.action.recent.RecentActivitiesActionBean;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.Configuration;
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
@MobiCSSecure
public class CreateBookingInterestActionBean extends BookingInterestActionBean {

    /**
     * Booking interest page
     * 
     * @return
     * @throws RemoteException
     */
    @DontValidate
    @DefaultHandler
    public Resolution main() throws RemoteException {
        startSending = startSending == null ? getContext().getUser().getCustomerPreferencesDTO()
                .getTimeToStartSending() : startSending;
        stopSending = stopSending == null ? getContext().getUser().getCustomerPreferencesDTO().getTimeToStopSending()
                : stopSending;

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) + 1);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        startDate = (startDate == null ? new Date(c.getTimeInMillis()) : startDate);

        maxMessages = maxMessages == null ? getContext().getUser().getCustomerPreferencesDTO()
                .getNumberOfNotifications() : maxMessages;
        distance = distance == null ? getContext().getUser().getCustomerPreferencesDTO().getSearchRadius() : distance;

        return new ForwardResolution("/WEB-INF/book/createBookingInterest.jsp");
    }

    @ValidationMethod(on = "createBookingInterest", when = ValidationState.NO_ERRORS)
    public void validateAdress(ValidationErrors errors) {
        if (latitude == null && longitude == null) {
            errors.add("address", new LocalizableError("validation.required.valueNotPresent"));
        }
    }

    /**
     * Create a booking interest
     * 
     * @return
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws OverlappedCarBookingExceptionException
     * @throws CarClassNotFoundExceptionException
     * @throws CustomerNotFoundExceptionException
     * @throws BookingValidationExceptionException
     * @throws IllegalDateExceptionException
     */
    public Resolution createBookingInterest() throws RemoteException, UnsupportedEncodingException,
            OverlappedCarBookingExceptionException, CarClassNotFoundExceptionException,
            ExpiredBookingInterestExceptionException, CustomerNotFoundExceptionException,
            BookingValidationExceptionException, IllegalDateExceptionException {
        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        bookingWSServiceStub.createBookingInterest(start, address, carClazz, getContext().getUser().getCarClub()
                .getIsStandalone() ? true : fromMyCarClub, new BigDecimal(longitude), new BigDecimal(latitude),
                distance, startSending.intValue(), stopSending.intValue(), maxMessages.intValue());

        getContext().getMessages().add(new LocalizableMessage("find.car.later.interest.created"));
        return new RedirectResolution(RecentActivitiesActionBean.class).flash(this);
    }
}
