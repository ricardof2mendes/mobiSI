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
package com.criticalsoftware.mobics.presentation.action.trip;

import java.io.UnsupportedEncodingException;
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
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.criticalsoftware.mobics.booking.CurrentTripDTO;
import com.criticalsoftware.mobics.booking.TripDetailsDTO;
import com.criticalsoftware.mobics.presentation.action.BaseActionBean;
import com.criticalsoftware.mobics.presentation.extension.DatetimeTypeConverter;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.presentation.util.GeolocationUtil;
import com.criticalsoftware.mobics.proxy.booking.BookingNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.booking.BookingValidationExceptionException;
import com.criticalsoftware.mobics.proxy.booking.BookingWSServiceStub;
import com.criticalsoftware.mobics.proxy.booking.CustomerNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.booking.InvalidCustomerPinExceptionException;
import com.criticalsoftware.mobics.proxy.car.CarLicensePlateNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.car.CarWSServiceStub;

/**
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class TripActionBean extends BaseActionBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(TripActionBean.class);

    private static final String BOOKED = "BOOKED";

    private static final String IN_USE = "IN_USE";

    private TripDetailsDTO last;

    private CurrentTripDTO current;

    @Validate(required = true, on = "unlockCar")
    private String licensePlate;

    @Validate(required = true, on = "save", converter = DatetimeTypeConverter.class)
    private Date endDate;

    @Validate(required = true, on = "save")
    private String bookingCode;

    private Date extendBookingDate;

    @Validate(required = true, on = "finish")
    private Boolean successOp;

    @Validate(required = true, on = "finish")
    private Boolean isClosed;

    @Validate(required = true, on = "finish")
    private Boolean keysNotReturned;

    @Validate(required = true, on = "finish")
    private Boolean keysAlreadyReturned;

    @Validate(required = true, on = "finish")
    private Boolean doorsAlreadyOpen;

    @Validate(required = true, on = "finish")
    private Boolean doorsAlreadyClosed;

    @Validate(required = true, on = "finish")
    private Boolean unableToCommunicate;

    @Validate
    private Boolean unlockOp;

    @Validate(on = "finish")
    private Boolean sendReport;

    private Boolean newDriverVersion = true;

    private Boolean newDriverVersionLastTrip = true;

    /**
     * Recent list resolution
     *
     * @return
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws CustomerNotFoundExceptionException
     * @throws com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException
     * @throws BookingNotFoundExceptionException
     */
    @DefaultHandler
    @DontValidate
    public Resolution main() throws RemoteException, UnsupportedEncodingException, CustomerNotFoundExceptionException,
            com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException {
        Resolution resolution = new ForwardResolution("/WEB-INF/trip/currentTrip.jsp");
        final BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext()
                        .getUser().getPassword()));

        this.current = bookingWSServiceStub.getCurrentTripDetails();

        if ((this.current == null) || (this.current.getLicensePlate() == null)
                || (this.current.getLicensePlate().length() == 0)) {
            bookingWSServiceStub._getServiceClient().addHeader(
                    AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this
                            .getContext().getUser().getPassword()));
            try {
                this.last = bookingWSServiceStub.getLastTripDetails();
            } catch (final BookingNotFoundExceptionException e) {
                // Do nothing
            }
            resolution = new ForwardResolution("/WEB-INF/trip/lastTrip.jsp");
        }
        // Send the user to damages report action
        else if (this.current.getCarState().equals(IN_USE) && current.getShowDamageReport()) {
            LOGGER.debug("State: " + this.current.getCarState());

            return new RedirectResolution(DamageReportActionBean.class);
        }

        // does the car have the CCOME driver? if yes, the webapp interface is different from the conventional.
        this.newDriverVersion = (this.current != null) && (this.current.getCarDTO() != null)
                && !Configuration.CCOM_CLASS.equals(this.current.getCarDTO().getDeviceDriverClass());
        this.newDriverVersionLastTrip = (this.last != null) && (this.last.getCar() != null)
                && !Configuration.CCOM_CLASS.equals(this.last.getCar().getDeviceDriverClass());

        return resolution;
    }

    /**
     * Unlock car resolution
     *
     * @return
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws CarLicensePlateNotFoundExceptionException
     * @throws com.criticalsoftware.mobics.proxy.car.CustomerNotFoundExceptionException
     */
    public Resolution unlockCar() throws RemoteException, UnsupportedEncodingException,
            CarLicensePlateNotFoundExceptionException,
            com.criticalsoftware.mobics.proxy.car.CustomerNotFoundExceptionException {

        final CarWSServiceStub carWSServiceStub = new CarWSServiceStub(Configuration.INSTANCE.getCarEndpoint());
        carWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext()
                        .getUser().getPassword()));

        this.getContext().getResponse().setHeader("Stripes-Success", "OK");
        return new JavaScriptResolution(carWSServiceStub.unlockCar(this.licensePlate));

    }

    /* Lock and End Trip are separate calls for CCOME */
    public Resolution lockCar() throws UnsupportedEncodingException, RemoteException,
            com.criticalsoftware.mobics.proxy.car.CustomerNotFoundExceptionException,
            CarLicensePlateNotFoundExceptionException {
        final CarWSServiceStub carWSServiceStub = new CarWSServiceStub(Configuration.INSTANCE.getCarEndpoint());
        carWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext()
                        .getUser().getPassword()));
        this.getContext().getResponse().setHeader("Stripes-Success", "OK");
        return new JavaScriptResolution(carWSServiceStub.lockCar(this.licensePlate));
    }

    public Resolution endTrip() throws UnsupportedEncodingException, RemoteException,
            com.criticalsoftware.mobics.proxy.car.CustomerNotFoundExceptionException,
            CarLicensePlateNotFoundExceptionException {

        final Resolution resolution = new RedirectResolution(this.getClass()).flash(this);

        final CarWSServiceStub carWSServiceStub = new CarWSServiceStub(Configuration.INSTANCE.getCarEndpoint());
        carWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext()
                        .getUser().getPassword()));
        this.getContext().getResponse().setHeader("Stripes-Success", "OK");
        carWSServiceStub.lockCar(this.licensePlate);

        return resolution;
    }

    /* On other drivers lock and end trip are performed on one call. */
    public Resolution lockEndTrip() throws UnsupportedEncodingException, RemoteException,
            com.criticalsoftware.mobics.proxy.car.CustomerNotFoundExceptionException,
            CarLicensePlateNotFoundExceptionException {
        final CarWSServiceStub carWSServiceStub = new CarWSServiceStub(Configuration.INSTANCE.getCarEndpoint());
        carWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext()
                        .getUser().getPassword()));
        this.getContext().getResponse().setHeader("Stripes-Success", "OK");
        return new JavaScriptResolution(carWSServiceStub.lockCar(this.licensePlate));
    }

    /**
     * Put messages after lock unlock
     *
     * @return
     * @throws BookingNotFoundExceptionException
     * @throws CustomerNotFoundExceptionException
     * @throws UnsupportedEncodingException
     * @throws RemoteException
     * @throws com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException
     */
    public Resolution finish() throws RemoteException, UnsupportedEncodingException,
            CustomerNotFoundExceptionException, BookingNotFoundExceptionException,
            com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException {
        Resolution resolution = new RedirectResolution(this.getClass()).flash(this);

        if (this.successOp) {
            if (this.unlockOp != null) {
                if (!this.isClosed) {
                    this.getContext().getMessages().add(new LocalizableMessage("current.trip.unlock.car.message"));
                } else {
                    this.getContext().getMessages().add(new LocalizableMessage("trip.detail.unlock_sucess"));
                }
            } else {
                if (!this.isClosed) {
                    this.getContext().getMessages().add(new LocalizableMessage("current.trip.end.trip.message"));
                } else {
                    this.getContext().getMessages().add(new LocalizableMessage("trip.detail.lock_sucess"));
                }
            }
        } else {
            if (this.unableToCommunicate != null && this.unableToCommunicate == true) {
                this.getContext().getValidationErrors()
                        .addGlobalError(new LocalizableError("current.trip.communication.message.error"));
            } else if (this.sendReport != null && this.sendReport == true) {
                this.getContext().getMessages()
                        .add(new LocalizableError("trip.detail.unlock_and_damage_report_sucess"));
            } else {
                if (this.unlockOp != null) {
                    if ((this.keysAlreadyReturned != null) && this.keysAlreadyReturned.booleanValue()) {
                        this.getContext().getValidationErrors()
                                .addGlobalError(new LocalizableError("current.trip.keys.already.returned"));
                    } else if ((this.doorsAlreadyOpen != null) && this.doorsAlreadyOpen.booleanValue()) {
                        this.getContext().getValidationErrors()
                                .addGlobalError(new LocalizableError("current.trip.doors.already.open"));
                    } else {
                        this.getContext().getValidationErrors()
                                .addGlobalError(new LocalizableError("current.trip.unlock.car.message.error"));
                    }
                } else {
                    if ((this.keysNotReturned != null) && this.keysNotReturned.booleanValue()) {
                        this.getContext().getValidationErrors()
                                .addGlobalError(new LocalizableError("current.trip.keys.not.returned"));
                    } else if ((this.doorsAlreadyClosed != null) && this.doorsAlreadyClosed.booleanValue()) {
                        this.getContext().getValidationErrors()
                                .addGlobalError(new LocalizableError("current.trip.doors.already.closed"));
                    } else {
                        this.getContext().getValidationErrors()
                                .addGlobalError(new LocalizableError("current.trip.end.trip.message.error"));
                    }
                }
            }

            resolution = this.main();
        }

        return resolution;
    }

    /**
     * End current trip resolution
     *
     * @return
     * @throws UnsupportedEncodingException
     * @throws RemoteException
     * @throws BookingNotFoundExceptionException
     * @throws CustomerNotFoundExceptionException
     * @throws com.criticalsoftware.mobics.proxy.car.CustomerNotFoundExceptionException
     * @throws com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException
     * @throws CarLicensePlateNotFoundExceptionException
     * @throws InterruptedException
     */
    public Resolution getCurrentTrip() throws UnsupportedEncodingException, RemoteException,
            BookingNotFoundExceptionException, CustomerNotFoundExceptionException,
            com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException {

        final BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext()
                        .getUser().getPassword()));
        this.getContext().getResponse().setHeader("Stripes-Success", "OK");

        final CurrentTripDTO currentTripDTO = bookingWSServiceStub.getCurrentTripDetails();

        return new JavaScriptResolution(currentTripDTO);
    }

    public Resolution getLastTrip() throws UnsupportedEncodingException, RemoteException,
            BookingNotFoundExceptionException, CustomerNotFoundExceptionException,
            com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException {

        final BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext()
                        .getUser().getPassword()));
        this.getContext().getResponse().setHeader("Stripes-Success", "OK");

        final TripDetailsDTO lastTripDTO = bookingWSServiceStub.getLastTripDetails();

        return new JavaScriptResolution(lastTripDTO);
    }

    /**
     * Extend trip
     *
     * @return
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws CustomerNotFoundExceptionException
     * @throws BookingNotFoundExceptionException
     * @throws com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException
     */
    public Resolution extend() throws RemoteException, UnsupportedEncodingException,
            CustomerNotFoundExceptionException, BookingNotFoundExceptionException,
            com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException {
        final BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext()
                        .getUser().getPassword()));

        Resolution resolution = new ForwardResolution("/WEB-INF/trip/extendTrip.jsp");

        try {
            this.current = bookingWSServiceStub.getCurrentTripDetails();
        } catch (final Exception e) {
            LOGGER.error("Error getting current trip details.", e);
        }
        if (this.current != null) {
            this.bookingCode = this.current.getBookingCode();
            this.endDate = this.current.getEndDate().getTime();

            final Calendar c = Calendar.getInstance();
            c.setTimeInMillis(bookingWSServiceStub.getNextAdvanceBooking(this.bookingCode));
            this.extendBookingDate = (c == null ? null : c.getTime());
        } else {
            resolution = new ForwardResolution(this.getClass());
        }

        return resolution;
    }

    public Resolution save() throws RemoteException, UnsupportedEncodingException, BookingValidationExceptionException,
            InvalidCustomerPinExceptionException, BookingNotFoundExceptionException,
            com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException {

        final BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext()
                        .getUser().getPassword()));

        final Calendar aux = Calendar.getInstance();
        aux.setTime(this.endDate);

        bookingWSServiceStub.extendAdvanceBooking(this.bookingCode, aux.getTimeInMillis());

        this.getContext().getMessages().add(new LocalizableMessage("current.trip.extend.trip.message"));
        return new RedirectResolution(this.getClass());
    }

    /**
     * Reverse geolocation string
     *
     * @return the address string
     */
    public String getLocation() {
        String location = new LocalizableMessage("application.value.not.available").getMessage(this.getContext()
                .getLocale());
        if ((this.current.getLatitude() != null) && (this.current.getLongitude() != null)) {
            location = GeolocationUtil.getAddressFromCoordinates(this.current.getLatitude().toString(), this.current
                    .getLongitude().toString());
        }
        return location;
    }

    /**
     * Get start location
     *
     * @return the adress string
     */
    public String getStartLocation() {
        String location = new LocalizableMessage("application.value.not.available").getMessage(this.getContext()
                .getLocale());
        if ((this.last.getStartLatitude() != null) && (this.last.getStartLongitude() != null)) {
            location = GeolocationUtil.getAddressFromCoordinates(this.last.getStartLatitude().toString(), this.last
                    .getStartLongitude().toString());
        }
        return location;
    }

    /**
     * Get end location
     *
     * @return the adress string
     */
    public String getEndLocation() {
        String location = new LocalizableMessage("application.value.not.available").getMessage(this.getContext()
                .getLocale());
        if ((this.last.getEndtLatitude() != null) && (this.last.getEndLongitude() != null)) {
            location = GeolocationUtil.getAddressFromCoordinates(this.last.getEndtLatitude().toString(), this.last
                    .getEndLongitude().toString());
        }
        return location;
    }

    /**
     * Get the car state
     *
     * @return boolean
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException
     */
    public Resolution getState() throws RemoteException, UnsupportedEncodingException,
            com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException {
        final BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext()
                        .getUser().getPassword()));

        try {
            this.current = bookingWSServiceStub.getCurrentTripDetails();
        } catch (final CustomerNotFoundExceptionException e) {
            LOGGER.error("Customer not found", e);
        }

        this.getContext().getResponse().setHeader("Stripes-Success", "OK");

        return new JavaScriptResolution(this.current == null ? null : this.current.getState());
    }

    /**
     * @return
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws CustomerNotFoundExceptionException
     */
    public Resolution showMessage() throws RemoteException, UnsupportedEncodingException,
            CustomerNotFoundExceptionException {
        this.getContext().getMessages().add(new LocalizableMessage("car.details.book.done"));
        return new RedirectResolution(this.getClass()).flash(this);
    }

    /**
     * @return the current
     */
    public CurrentTripDTO getCurrent() {
        return this.current;
    }

    /**
     * @return the last
     */
    public TripDetailsDTO getLast() {
        return this.last;
    }

    /**
     * @param licensePlate the licensePlate to set
     */
    public void setLicensePlate(final String licensePlate) {
        this.licensePlate = licensePlate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return this.endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @param bookingCode the bookingCode to set
     */
    public void setBookingCode(final String bookingCode) {
        this.bookingCode = bookingCode;
    }

    /**
     * @return the bookingCode
     */
    public String getBookingCode() {
        return this.bookingCode;
    }

    /**
     * @return the extendBookingDate
     */
    public Date getExtendBookingDate() {
        return this.extendBookingDate;
    }

    /**
     * @param successOp the successOp to set
     */
    public void setSuccessOp(final Boolean successOp) {
        this.successOp = successOp;
    }

    /**
     * @return the isClosed
     */
    public Boolean getIsClosed() {
        return this.isClosed;
    }

    /**
     * @param isClosed the isClosed to set
     */
    public void setIsClosed(final Boolean isClosed) {
        this.isClosed = isClosed;
    }

    /**
     * @param unlockOp the unlockOp to set
     */
    public void setUnlockOp(final Boolean unlockOp) {
        this.unlockOp = unlockOp;
    }

    public Boolean getNewDriverVersion() {
        return this.newDriverVersion;
    }

    public void setNewDriverVersion(final Boolean newDriverVersion) {
        this.newDriverVersion = newDriverVersion;
    }

    public Boolean getNewDriverVersionLastTrip() {
        return this.newDriverVersionLastTrip;
    }

    public void setNewDriverVersionLastTrip(final Boolean newDriverVersionLastTrip) {
        this.newDriverVersionLastTrip = newDriverVersionLastTrip;
    }

    public Boolean getKeysNotReturned() {
        return this.keysNotReturned;
    }

    public void setKeysNotReturned(final Boolean keysNotReturned) {
        this.keysNotReturned = keysNotReturned;
    }

    public Boolean getKeysAlreadyReturned() {
        return this.keysAlreadyReturned;
    }

    public void setKeysAlreadyReturned(final Boolean keysAlreadyReturned) {
        this.keysAlreadyReturned = keysAlreadyReturned;
    }

    /**
     * @return the doorsAlreadyOpen
     */
    public Boolean getDoorsAlreadyOpen() {
        return this.doorsAlreadyOpen;
    }

    /**
     * @param doorsAlreadyOpen the doorsAlreadyOpen to set
     */
    public void setDoorsAlreadyOpen(final Boolean doorsAlreadyOpen) {
        this.doorsAlreadyOpen = doorsAlreadyOpen;
    }

    /**
     * @return the doorsAlreadyClosed
     */
    public Boolean getDoorsAlreadyClosed() {
        return this.doorsAlreadyClosed;
    }

    /**
     * @param doorsAlreadyClosed the doorsAlreadyClosed to set
     */
    public void setDoorsAlreadyClosed(final Boolean doorsAlreadyClosed) {
        this.doorsAlreadyClosed = doorsAlreadyClosed;
    }

    /**
     * @return the sendReport
     */
    public Boolean getSendReport() {
        return this.sendReport;
    }

    /**
     * @param sendReport the sendReport to set
     */
    public void setSendReport(final Boolean sendReport) {
        this.sendReport = sendReport;
    }

    /**
     * @return the unableToCommunicate
     */
    public Boolean getUnableToCommunicate() {
        return unableToCommunicate;
    }

    /**
     * @param unableToCommunicate the unableToCommunicate to set
     */
    public void setUnableToCommunicate(Boolean unableToCommunicate) {
        this.unableToCommunicate = unableToCommunicate;
    }

}
