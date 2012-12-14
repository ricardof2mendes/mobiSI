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

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import com.criticalsoftware.mobics.booking.CurrentTripDTO;
import com.criticalsoftware.mobics.booking.TripDetailsDTO;
import com.criticalsoftware.mobics.presentation.action.BaseActionBean;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.presentation.util.GeolocationUtil;
import com.criticalsoftware.mobics.proxy.booking.BookingNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.booking.BookingWSServiceStub;
import com.criticalsoftware.mobics.proxy.booking.CustomerNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.car.CarLicensePlateNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.car.CarWSServiceStub;

/**
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class TripActionBean extends BaseActionBean {

    private TripDetailsDTO last;

    private CurrentTripDTO current;

    @Validate(required = true, on = { "lockCar", "unlockCar", "signal" })
    private String licensePlate;

    /**
     * Recent list resolution
     * @return
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws CustomerNotFoundExceptionException
     * @throws BookingNotFoundExceptionException
     */
    @DefaultHandler
    @DontValidate
    public Resolution main() throws RemoteException, UnsupportedEncodingException, CustomerNotFoundExceptionException, BookingNotFoundExceptionException {
        ForwardResolution resolution = new ForwardResolution("/WEB-INF/trip/currentTrip.jsp");
        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));

        current = bookingWSServiceStub.getCurrentTripDetails();

        if (current == null || current.getLicensePlate() == null || current.getLicensePlate().length() == 0) {
            resolution = new ForwardResolution("/WEB-INF/trip/lastTrip.jsp");
            last = bookingWSServiceStub.getLastTripDetails();
        }

        return resolution;
    }

    /**
     * Lock car resolution
     * 
     * @return
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws CarLicensePlateNotFoundExceptionException
     * @throws com.criticalsoftware.mobics.proxy.car.CustomerNotFoundExceptionException
     */
    public Resolution lockCar() throws RemoteException, UnsupportedEncodingException,
            CarLicensePlateNotFoundExceptionException, com.criticalsoftware.mobics.proxy.car.CustomerNotFoundExceptionException {

        CarWSServiceStub carWSServiceStub = new CarWSServiceStub(Configuration.INSTANCE.getCarEndpoint());
        carWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));

        carWSServiceStub.lockCar(licensePlate);

        getContext().getMessages().add(new LocalizableMessage("current.trip.lock.car.message"));
        return new RedirectResolution(this.getClass()).flash(this);
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
            CarLicensePlateNotFoundExceptionException, com.criticalsoftware.mobics.proxy.car.CustomerNotFoundExceptionException {

        CarWSServiceStub carWSServiceStub = new CarWSServiceStub(Configuration.INSTANCE.getCarEndpoint());
        carWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));

        carWSServiceStub.unlockCar(licensePlate);
        getContext().getMessages().add(new LocalizableMessage("current.trip.unlock.car.message"));
        return new RedirectResolution(this.getClass()).flash(this);
    }

    /**
     * End current trip resolution
     * 
     * @return
     * @throws UnsupportedEncodingException
     * @throws RemoteException
     * @throws BookingNotFoundExceptionException
     */
    public Resolution endTrip() throws UnsupportedEncodingException, RemoteException, BookingNotFoundExceptionException {

        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));

        if (bookingWSServiceStub.isCustomerInOngoingTrip()) {
            bookingWSServiceStub.closeActiveBooking();
            getContext().getMessages().add(new LocalizableMessage("current.trip.end.trip.message"));
        }
        return new RedirectResolution(this.getClass()).flash(this);
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

}
