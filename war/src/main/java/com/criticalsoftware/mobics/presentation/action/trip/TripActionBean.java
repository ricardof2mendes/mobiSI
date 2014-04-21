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

import com.criticalsoftware.mobics.booking.BookingErrorCodeEnum;
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
    
    private Boolean keysNotReturned;
   
    @Validate
    private Boolean unlockOp;
    
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
    public Resolution main() throws RemoteException, UnsupportedEncodingException, CustomerNotFoundExceptionException, com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException {
        Resolution resolution = new ForwardResolution("/WEB-INF/trip/currentTrip.jsp");
        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));

        current = bookingWSServiceStub.getCurrentTripDetails();

        if (current == null || current.getLicensePlate() == null || current.getLicensePlate().length() == 0) {
            bookingWSServiceStub._getServiceClient().addHeader(
                    AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                            .getPassword()));
            try {
                last = bookingWSServiceStub.getLastTripDetails();
            } catch (BookingNotFoundExceptionException e) {
                //Do nothing
            }
            resolution = new ForwardResolution("/WEB-INF/trip/lastTrip.jsp");
        }

        //does the car have the CCOME driver? if yes, the webapp interface is different from the conventional.
        //newDriverVersion = current != null && current.getCarDTO() != null && Configuration.CCOME_CLASS.equals(current.getCarDTO().getDeviceDriverClass());
        //newDriverVersionLastTrip = last != null && last.getCar() != null && Configuration.CCOME_CLASS.equals(last.getCar().getDeviceDriverClass());
        
        if(Configuration.CCOME_MODE_ACTIVATED == false){
            newDriverVersion = false;
            newDriverVersionLastTrip = false;
        }
  
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

        CarWSServiceStub carWSServiceStub = new CarWSServiceStub(Configuration.INSTANCE.getCarEndpoint());
        carWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));

        getContext().getResponse().setHeader("Stripes-Success", "OK");
        return new JavaScriptResolution(carWSServiceStub.unlockCar(licensePlate));
    }


    /* Lock and End Trip are separate calls for CCOME */
    public Resolution lockCar() throws UnsupportedEncodingException, RemoteException,
            com.criticalsoftware.mobics.proxy.car.CustomerNotFoundExceptionException,
            CarLicensePlateNotFoundExceptionException {
        CarWSServiceStub carWSServiceStub = new CarWSServiceStub(Configuration.INSTANCE.getCarEndpoint());
        carWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));
        getContext().getResponse().setHeader("Stripes-Success", "OK");
        return new JavaScriptResolution(carWSServiceStub.lockCar(licensePlate));
    }
    
    public Resolution endTrip()throws UnsupportedEncodingException, RemoteException,
    com.criticalsoftware.mobics.proxy.car.CustomerNotFoundExceptionException,
    CarLicensePlateNotFoundExceptionException {

        Resolution resolution = new RedirectResolution(this.getClass()).flash(this);

        CarWSServiceStub carWSServiceStub = new CarWSServiceStub(Configuration.INSTANCE.getCarEndpoint());
        carWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));
        getContext().getResponse().setHeader("Stripes-Success", "OK");
        carWSServiceStub.lockCar(licensePlate);

        return resolution;
    }
    
    public Resolution endTripWithPooling() throws UnsupportedEncodingException, RemoteException,
            com.criticalsoftware.mobics.proxy.car.CustomerNotFoundExceptionException,
            CarLicensePlateNotFoundExceptionException {
        CarWSServiceStub carWSServiceStub = new CarWSServiceStub(Configuration.INSTANCE.getCarEndpoint());
        carWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));
        getContext().getResponse().setHeader("Stripes-Success", "OK");
        return new JavaScriptResolution(carWSServiceStub.lockCar(licensePlate));
    }


    /* On other drivers lock and end trip are performed on one call. */
    public Resolution lockEndTrip() throws UnsupportedEncodingException, RemoteException,
            com.criticalsoftware.mobics.proxy.car.CustomerNotFoundExceptionException,
            CarLicensePlateNotFoundExceptionException {
        CarWSServiceStub carWSServiceStub = new CarWSServiceStub(Configuration.INSTANCE.getCarEndpoint());
        carWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));
        getContext().getResponse().setHeader("Stripes-Success", "OK");
        return new JavaScriptResolution(carWSServiceStub.lockCar(licensePlate));
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
            CustomerNotFoundExceptionException, BookingNotFoundExceptionException, com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException {
        Resolution resolution = new RedirectResolution(this.getClass()).flash(this);
        if (successOp) {
            if (unlockOp != null) {
                getContext().getMessages().add(new LocalizableMessage("current.trip.unlock.car.message"));
            } else {
                getContext().getMessages().add(new LocalizableMessage("current.trip.end.trip.message"));          
            }
        } else {
            if (unlockOp != null) {
                getContext().getValidationErrors().addGlobalError(
                        new LocalizableError("current.trip.unlock.car.message.error"));
            } else {
                if(this.keysNotReturned){
                    getContext().getValidationErrors().addGlobalError(
                            new LocalizableError("current.trip.keys.not.returned"));
                }else{
                    getContext().getValidationErrors().addGlobalError(new LocalizableError("current.trip.end.trip.message.error"));
                }      
            }
            resolution = main();
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
            BookingNotFoundExceptionException, CustomerNotFoundExceptionException, com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException {

        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));
        getContext().getResponse().setHeader("Stripes-Success", "OK");
        
        CurrentTripDTO currentTripDTO = bookingWSServiceStub.getCurrentTripDetails();
        
        return new JavaScriptResolution(currentTripDTO);
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
            CustomerNotFoundExceptionException, BookingNotFoundExceptionException, com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException {
        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));

        Resolution resolution = new ForwardResolution("/WEB-INF/trip/extendTrip.jsp");

        try {
            current = bookingWSServiceStub.getCurrentTripDetails();
        } catch (Exception e) {
            LOGGER.error("Error getting current trip details.", e);
        }
        if (current != null) {
            bookingCode = current.getBookingCode();
            endDate = current.getEndDate().getTime();

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(bookingWSServiceStub.getNextAdvanceBooking(bookingCode));
            extendBookingDate = (c == null ? null : c.getTime());
        } else {
            resolution = new ForwardResolution(this.getClass());
        }

        return resolution;
    }

    public Resolution save() throws RemoteException, UnsupportedEncodingException, BookingValidationExceptionException,
            InvalidCustomerPinExceptionException, BookingNotFoundExceptionException,
            com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException {

        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));

        Calendar aux = Calendar.getInstance();
        aux.setTime(endDate);

        bookingWSServiceStub.extendAdvanceBooking(bookingCode, aux.getTimeInMillis());

        getContext().getMessages().add(new LocalizableMessage("current.trip.extend.trip.message"));
        return new RedirectResolution(this.getClass());
    }

    /**
     * Reverse geolocation string
     * 
     * @return the address string
     */
    public String getLocation() {
        String location = new LocalizableMessage("application.value.not.available")
                .getMessage(getContext().getLocale());
        if (current.getLatitude() != null && current.getLongitude() != null) {
            location = GeolocationUtil.getAddressFromCoordinates(current.getLatitude().toString(), current
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
        String location = new LocalizableMessage("application.value.not.available")
                .getMessage(getContext().getLocale());
        if (last.getStartLatitude() != null && last.getStartLongitude() != null) {
            location = GeolocationUtil.getAddressFromCoordinates(last.getStartLatitude().toString(), last
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
        String location = new LocalizableMessage("application.value.not.available")
                .getMessage(getContext().getLocale());
        if (last.getEndtLatitude() != null && last.getEndLongitude() != null) {
            location = GeolocationUtil.getAddressFromCoordinates(last.getEndtLatitude().toString(), last
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
    public Resolution getState() throws RemoteException, UnsupportedEncodingException, com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException {
        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));

        try {
            current = bookingWSServiceStub.getCurrentTripDetails();
        } catch (CustomerNotFoundExceptionException e) {
            LOGGER.error("Customer not found", e);
        }

        getContext().getResponse().setHeader("Stripes-Success", "OK");

        return new JavaScriptResolution(current == null ? null : current.getState());
    }

    /**
     * @return
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws CustomerNotFoundExceptionException
     */
    public Resolution showMessage() throws RemoteException, UnsupportedEncodingException,
            CustomerNotFoundExceptionException {
        getContext().getMessages().add(new LocalizableMessage("car.details.book.done"));
        return new RedirectResolution(this.getClass()).flash(this);
    }

    /**
     * @return the current
     */
    public CurrentTripDTO getCurrent() {
        return current;
    }

    /**
     * @return the last
     */
    public TripDetailsDTO getLast() {
        return last;
    }

    /**
     * @param licensePlate the licensePlate to set
     */
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
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
     * @param bookingCode the bookingCode to set
     */
    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    /**
     * @return the bookingCode
     */
    public String getBookingCode() {
        return bookingCode;
    }

    /**
     * @return the extendBookingDate
     */
    public Date getExtendBookingDate() {
        return extendBookingDate;
    }

    /**
     * @param successOp the successOp to set
     */
    public void setSuccessOp(Boolean successOp) {
        this.successOp = successOp;
    }

    /**
     * @param unlockOp the unlockOp to set
     */
    public void setUnlockOp(Boolean unlockOp) {
        this.unlockOp = unlockOp;
    }

    public Boolean getNewDriverVersion() {
        return newDriverVersion;
    }

    public void setNewDriverVersion(Boolean newDriverVersion) {
        this.newDriverVersion = newDriverVersion;
    }

    public Boolean getNewDriverVersionLastTrip() {
        return newDriverVersionLastTrip;
    }

    public void setNewDriverVersionLastTrip(Boolean newDriverVersionLastTrip) {
        this.newDriverVersionLastTrip = newDriverVersionLastTrip;
    }

    public Boolean getKeysNotReturned() {
        return keysNotReturned;
    }

    public void setKeysNotReturned(Boolean keysNotReturned) {
        this.keysNotReturned = keysNotReturned;
    }
}
