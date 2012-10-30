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

/**
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class TripActionBean extends BaseActionBean {
    
    private TripDetailsDTO last; 
    
    private CurrentTripDTO current; 

    @DefaultHandler
    @DontValidate
    public Resolution main() throws RemoteException, UnsupportedEncodingException, CustomerNotFoundExceptionException, BookingNotFoundExceptionException {
        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));
        
        current = bookingWSServiceStub.getCurrentTripDetails();
        ForwardResolution resolution = new ForwardResolution("/WEB-INF/trip/currentTrip.jsp");
        
        if(current == null || current.getLicensePlate() == null || current.getLicensePlate().length() == 0) {
            last = bookingWSServiceStub.getLastTripDetails();
            resolution = new ForwardResolution("/WEB-INF/trip/lastTrip.jsp");
        }

        return resolution;
    }
    
    public Resolution lockCar() {
        getContext().getMessages().add(new LocalizableMessage("current.trip.lock.car.message"));
        return new RedirectResolution(this.getClass()).flash(this);
    }
    
    public Resolution unlockCar() {
        getContext().getMessages().add(new LocalizableMessage("current.trip.unlock.car.message"));
        return new RedirectResolution(this.getClass()).flash(this);
    }
    
    public Resolution signal() {
        getContext().getMessages().add(new LocalizableMessage("current.trip.car.signaling"));
        return new RedirectResolution(this.getClass()).flash(this);
    }
    
    public Resolution endTrip() throws UnsupportedEncodingException, RemoteException, BookingNotFoundExceptionException {
        
        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));
        
        if(bookingWSServiceStub.isCustomerInOngoingTrip()){
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
        return GeolocationUtil.getAddressFromCoordinates(current.getLatitude().toString(), current.getLongitude().toString());
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
    
    
}
