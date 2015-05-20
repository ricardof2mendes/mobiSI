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
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.ajax.JavaScriptResolution;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.criticalsoftware.mobics.booking.BookingInterestDTO;
import com.criticalsoftware.mobics.booking.TripDetailsDTO;
import com.criticalsoftware.mobics.customer.BonusDetailsDTO;
import com.criticalsoftware.mobics.customer.CreditPurchaseDetailsDTO;
import com.criticalsoftware.mobics.customer.CustomerActivityEnum;
import com.criticalsoftware.mobics.customer.CustomerActivityListDTO;
import com.criticalsoftware.mobics.customer.IncidentForCustomerDTO;
import com.criticalsoftware.mobics.presentation.action.BaseActionBean;
import com.criticalsoftware.mobics.presentation.action.trip.TripActionBean;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.BookingState;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.presentation.util.GeolocationUtil;
import com.criticalsoftware.mobics.proxy.booking.BookingInterestNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.booking.BookingNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.booking.BookingWSServiceStub;
import com.criticalsoftware.mobics.proxy.booking.BookingWrongStateExceptionException;
import com.criticalsoftware.mobics.proxy.booking.CustomerNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.booking.UnauthorizedCustomerExceptionException;
import com.criticalsoftware.mobics.proxy.carclub.PromotionCodeNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.customer.CreditPurchaseExceptionException;
import com.criticalsoftware.mobics.proxy.customer.CustomerWSServiceStub;
import com.criticalsoftware.mobics.proxy.customer.EventNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.customer.PromotionalCodeCodeInvalidExceptionException;
import com.criticalsoftware.mobics.proxy.customer.PromotionalCodeCodeNotFoundExceptionException;

/**
 * Recent activities action bean
 *
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class RecentActivitiesActionBean extends BaseActionBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecentActivitiesActionBean.class);

    private CustomerActivityListDTO[] recents;

    private TripDetailsDTO trip;

    private BookingInterestDTO booking;

    private IncidentForCustomerDTO incident;

    private CreditPurchaseDetailsDTO credit;

    private BonusDetailsDTO bonus;

    @Validate(
            required = true,
            on = {"tripDetails", "bookingDetails", "advanceBookingDetails", "cancelAdvanceBooking", "incidentDetails"})
    private String activityCode;

    @Validate(required = true, on = "incidentDetails")
    private String activityType;

    @Validate
    private String extended;

    /**
     * Main handler
     *
     * @return a page resolution
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws CustomerNotFoundExceptionException
     *
     */
    @DefaultHandler
    @DontValidate
    public Resolution main() throws RemoteException, UnsupportedEncodingException,
    com.criticalsoftware.mobics.proxy.customer.CustomerNotFoundExceptionException {

        final CustomerWSServiceStub customerWSServiceStub = new CustomerWSServiceStub(
                Configuration.INSTANCE.getCustomerEndpoint());
        customerWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext().getUser()
                        .getPassword()));

        // TODO add to configuration
        final Calendar thisMonth = Calendar.getInstance();
        final Calendar lastMonth = Calendar.getInstance();
        lastMonth.add(Calendar.MONTH, -1);

        this.recents = customerWSServiceStub.getRecentActivities(lastMonth.getTimeInMillis(), thisMonth.getTimeInMillis(),
                CustomerActivityEnum.ALL.getValue(), 0, Integer.MAX_VALUE);

        return new ForwardResolution("/WEB-INF/recent/recentActivities.jsp");
    }

    /**
     * Trip details
     *
     * @return a page resolution
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws BookingNotFoundExceptionException
     * @throws CustomerNotFoundExceptionException
     */
    public Resolution tripDetails() throws RemoteException, UnsupportedEncodingException,
            BookingNotFoundExceptionException, CustomerNotFoundExceptionException {
        final BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext().getUser()
                        .getPassword()));
        this.trip = bookingWSServiceStub.getTripDetails(this.activityCode);

        return new ForwardResolution("/WEB-INF/recent/tripDetails.jsp");
    }

    /**
     * Booking interest details
     *
     * @return a page resolution
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws BookingNotFoundExceptionException
     * @throws CustomerNotFoundExceptionException
     */
    public Resolution advanceBookingDetails() throws RemoteException, UnsupportedEncodingException,
            BookingNotFoundExceptionException, CustomerNotFoundExceptionException {
        final BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext().getUser()
                        .getPassword()));

        this.trip = bookingWSServiceStub.getTripDetails(this.activityCode);
        Resolution resolution = new ForwardResolution("/WEB-INF/recent/advanceBookingDetails.jsp");

        if (BookingState.IN_USE.name().equals(this.trip.getState())) {
            resolution = new RedirectResolution(TripActionBean.class);
        }

        return resolution;
    }

    /**
     * Check if it has cancel cost
     *
     * @return
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws BookingNotFoundExceptionException
     * @throws CustomerNotFoundExceptionException
     */
    public Resolution hasCancelCost() throws RemoteException, UnsupportedEncodingException,
            BookingNotFoundExceptionException, CustomerNotFoundExceptionException {
        final BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext().getUser()
                        .getPassword()));
        this.getContext().getResponse().setHeader("Stripes-Success", "OK");
        return new JavaScriptResolution(bookingWSServiceStub.getTripDetails(this.activityCode));
    }

    /**
     * Cancel advance booking
     *
     * @return a page resolution
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws BookingNotFoundExceptionException
     *
     * @throws CustomerNotFoundExceptionException
     *
     * @throws UnauthorizedCustomerExceptionException
     *
     */
    public Resolution cancelAdvanceBooking() throws RemoteException, UnsupportedEncodingException,
    BookingNotFoundExceptionException,
    CustomerNotFoundExceptionException,
    UnauthorizedCustomerExceptionException,
    com.criticalsoftware.mobics.proxy.booking.CustomerNotFoundExceptionException {
        Class<? extends BaseActionBean> clazz = this.getClass();
        final BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext().getUser()
                        .getPassword()));

        try {
            bookingWSServiceStub.cancelAdvanceBooking(this.activityCode);
            this.getContext().getMessages().add(new LocalizableMessage("trip.detail.advance.booking.cancel.success"));
        } catch (final BookingWrongStateExceptionException e) {
            LOGGER.debug("Could not cancel advance booking. Booking is in wrong state to cancel!");
            this.getContext().getValidationErrors().addGlobalError(
                    new LocalizableError("trip.detail.advance.booking.cancel.unsuccess"));
            clazz = TripActionBean.class;
        }

        return new RedirectResolution(clazz).flash(this);
    }

    /**
     * Get the advance booking state
     *
     * @return booking state
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws CustomerNotFoundExceptionException
     */
    public Resolution getState() throws RemoteException, UnsupportedEncodingException,
            CustomerNotFoundExceptionException {
        final BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext().getUser()
                        .getPassword()));

        try {
            this.trip = bookingWSServiceStub.getTripDetails(this.activityCode);
        } catch (final BookingNotFoundExceptionException e) {
            LOGGER.error("Booking not found", e);
        }

        this.getContext().getResponse().setHeader("Stripes-Success", "OK");

        return new JavaScriptResolution(this.trip == null ? null : this.trip.getState());
    }

    /**
     * @return
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws CustomerNotFoundExceptionException
     *
     */
    public Resolution showMessage() throws RemoteException, UnsupportedEncodingException,
    CustomerNotFoundExceptionException {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("advanceBookingDetails", "");
        params.put("activityCode", this.activityCode);

        if (Boolean.parseBoolean(this.extended)) {
            this.getContext().getMessages().add(new LocalizableMessage("trip.detail.advance.booking.edit.success"));
        } else {
            this.getContext().getMessages().add(new LocalizableMessage("car.details.book.done"));
        }
        return new RedirectResolution(this.getClass()).addParameters(params).flash(this);
    }

    /**
     * Booking incident details
     *
     * @return streaming resolution
     */
    public Resolution incidentDetails() throws RemoteException, UnsupportedEncodingException,
    EventNotFoundExceptionException,
    com.criticalsoftware.mobics.proxy.customer.EventNotFoundExceptionException {
        final CustomerWSServiceStub customerWSServiceStub = new CustomerWSServiceStub(
                Configuration.INSTANCE.getCustomerEndpoint());
        customerWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext().getUser()
                        .getPassword()));
        this.incident = customerWSServiceStub.getIncidentDetailsForCustomer(
                this.activityCode, this.getContext().getUser().getCustomerPreferencesDTO().getLanguage());

        return new ForwardResolution("/WEB-INF/recent/incidentDetails.jsp");
    }

    /**
     * Booking interest details
     *
     * @return
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws BookingNotFoundExceptionException
     *
     */
    public Resolution interestDetails() throws RemoteException, UnsupportedEncodingException,
    BookingInterestNotFoundExceptionException {
        final BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext().getUser()
                        .getPassword()));

        this.booking = bookingWSServiceStub.getBookingInterest(this.activityCode);

        return new ForwardResolution("/WEB-INF/recent/detailBookingInterest.jsp");
    }

    /**
     * Credit details
     *
     * @return
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws com.criticalsoftware.mobics.proxy.customer.CustomerNotFoundExceptionException
     * @throws CreditPurchaseExceptionException
     */
    public Resolution creditDetails() throws RemoteException, UnsupportedEncodingException,
    com.criticalsoftware.mobics.proxy.customer.CustomerNotFoundExceptionException,
    CreditPurchaseExceptionException {
        final CustomerWSServiceStub customerWSServiceStub = new CustomerWSServiceStub(
                Configuration.INSTANCE.getCustomerEndpoint());
        customerWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext().getUser()
                        .getPassword()));

        this.credit = customerWSServiceStub.getCreditDetailsByOrderReference(this.activityCode);

        return new ForwardResolution("/WEB-INF/recent/creditDetails.jsp");
    }

    /**
     * Credit details
     *
     * @return
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws com.criticalsoftware.mobics.proxy.customer.CustomerNotFoundExceptionException
     * @throws PromotionCodeNotFoundExceptionException
     * @throws PromotionalCodeCodeNotFoundExceptionException
     * @throws PromotionalCodeCodeInvalidExceptionException
     */
    public Resolution bonusDetails() throws RemoteException, UnsupportedEncodingException,
    com.criticalsoftware.mobics.proxy.customer.CustomerNotFoundExceptionException,
    PromotionalCodeCodeInvalidExceptionException, PromotionalCodeCodeNotFoundExceptionException {
        final CustomerWSServiceStub customerWSServiceStub = new CustomerWSServiceStub(
                Configuration.INSTANCE.getCustomerEndpoint());
        customerWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext().getUser()
                        .getPassword()));

        this.bonus = customerWSServiceStub.getBonusDetail(this.activityCode);

        return new ForwardResolution("/WEB-INF/recent/bonusDetails.jsp");
    }

    /**
     * Cancel booking interest
     *
     * @return a page resolution
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws BookingNotFoundExceptionException
     *
     * @throws CustomerNotFoundExceptionException
     *
     * @throws UnauthorizedCustomerExceptionException
     *
     */
    public Resolution cancelInterest() throws RemoteException, UnsupportedEncodingException {
        final BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext().getUser()
                        .getPassword()));

        bookingWSServiceStub.removeBookingInterest(this.activityCode);

        this.getContext().getMessages().add(new LocalizableMessage("trip.detail.advance.booking.cancel.success"));

        return new RedirectResolution(this.getClass()).flash(this);
    }

    /**
     * Get trip start location
     *
     * @return the adress string
     */
    public String getStartLocation() {
        String location = new LocalizableMessage("application.value.not.available")
        .getMessage(this.getContext().getLocale());
        if ((this.trip.getStartLatitude() != null) && (this.trip.getStartLongitude() != null)) {
            location = GeolocationUtil.getAddressFromCoordinates(this.trip.getStartLatitude().toString(), this.trip
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
        .getMessage(this.getContext().getLocale());
        if ((this.trip.getEndtLatitude() != null) && (this.trip.getEndLongitude() != null)) {
            location = GeolocationUtil.getAddressFromCoordinates(this.trip.getEndtLatitude().toString(), this.trip
                    .getEndLongitude().toString());
        }
        return location;
    }

    /**
     * @return the activityCode
     */
    public String getActivityCode() {
        return this.activityCode;
    }

    /**
     * @param activityCode the activityCode to set
     */
    public void setActivityCode(final String activityCode) {
        this.activityCode = activityCode;
    }

    /**
     * @return the recents
     */
    public CustomerActivityListDTO[] getRecents() {
        return this.recents;
    }

    /**
     * @return the trip
     */
    public TripDetailsDTO getTrip() {
        return this.trip;
    }

    /**
     * @return the booking
     */
    public BookingInterestDTO getBooking() {
        return this.booking;
    }

    /**
     * @return the extended
     */
    public String getExtended() {
        return this.extended;
    }

    /**
     * @param extended the extended to set
     */
    public void setExtended(final String extended) {
        this.extended = extended;
    }

    public IncidentForCustomerDTO getIncident() {
        return this.incident;
    }

    public String getActivityType() {
        return this.activityType;
    }

    public void setActivityType(final String activityType) {
        this.activityType = activityType;
    }

    public CreditPurchaseDetailsDTO getCredit() {
        return this.credit;
    }

    public BonusDetailsDTO getBonus() {
        return this.bonus;
    }

}
