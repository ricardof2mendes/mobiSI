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
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.ajax.JavaScriptResolution;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;
import net.sourceforge.stripes.validation.ValidationState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.criticalsoftware.mobics.booking.CarDTO;
import com.criticalsoftware.mobics.carclub.LocationDTO;
import com.criticalsoftware.mobics.carclub.ZoneDTO;
import com.criticalsoftware.mobics.fleet.CarTypeEnum;
import com.criticalsoftware.mobics.fleet.ZoneWithPolygonDTO;
import com.criticalsoftware.mobics.presentation.action.recent.RecentActivitiesActionBean;
import com.criticalsoftware.mobics.presentation.extension.DatetimeTypeConverter;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.presentation.util.CoordinateZonesDTO;
import com.criticalsoftware.mobics.proxy.booking.BookingWSServiceStub;
import com.criticalsoftware.mobics.proxy.booking.CarClubCodeNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.booking.CarNotAvailableForBookingExceptionException;
import com.criticalsoftware.mobics.proxy.booking.CarNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.booking.CustomerNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.booking.ForbiddenZoneExceptionException;
import com.criticalsoftware.mobics.proxy.booking.IllegalDateExceptionException;
import com.criticalsoftware.mobics.proxy.booking.InvalidCarBookingExceptionException;
import com.criticalsoftware.mobics.proxy.booking.InvalidCustomerPinExceptionException;
import com.criticalsoftware.mobics.proxy.booking.OverlappedCarBookingExceptionException;
import com.criticalsoftware.mobics.proxy.booking.OverlappedCustomerBookingExceptionException;
import com.criticalsoftware.mobics.proxy.booking.UnauthorizedCustomerExceptionException;
import com.criticalsoftware.mobics.proxy.carclub.CarClubWSServiceStub;
import com.criticalsoftware.mobics.proxy.carclub.LocationCodeNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.fleet.FleetWSServiceStub;

/**
 * Booking action bean
 * 
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class AdvanceBookingActionBean extends BookingActionBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdvanceBookingActionBean.class);

    @Validate(required = true, on = { "searchCarsAdvance", "getZones" })
    private String location;

    @Validate
    private String zone;

    @Validate(
            required = true,
            on = { "searchCarsAdvance", "licensePlateBookAdvance", "showPin" },
            converter = DatetimeTypeConverter.class)
    private Date startDate;

    @Validate(
            required = true,
            on = { "searchCarsAdvance", "licensePlateBookAdvance", "showPin" },
            converter = DatetimeTypeConverter.class)
    private Date endDate;

    private CarDTO[] cars;

    /**
     * Show the advance book search screen
     * 
     * @return resolution
     */
    @DontValidate
    @DefaultHandler
    public Resolution main() {
        Calendar aux = Calendar.getInstance();
        aux.add(Calendar.HOUR_OF_DAY, 1);
        aux.set(Calendar.MINUTE, 0);
        aux.set(Calendar.MILLISECOND, 0);

        startDate = new Date(aux.getTimeInMillis());
        aux.add(Calendar.HOUR_OF_DAY, 1);
        endDate = new Date(aux.getTimeInMillis());

        return new ForwardResolution("/WEB-INF/book/searchAdvance.jsp");
    }

    // --------------------------------------------------
    // Search Resolutions
    // --------------------------------------------------
    /**
     * Search cars for advance booking
     * 
     * @return the car list page
     * @throws RemoteException
     * @throws CustomerNotFoundExceptionException
     * @throws IllegalDateExceptionException
     * @throws CarLicensePlateNotFoundExceptionException
     * @throws CarClubCodeNotFoundExceptionException
     */
    public Resolution searchCarsAdvance() throws RemoteException, CustomerNotFoundExceptionException,
            IllegalDateExceptionException, CarLicensePlateNotFoundExceptionException,
            CarClubCodeNotFoundExceptionException, UnsupportedEncodingException {

        Calendar start = Calendar.getInstance(), end = Calendar.getInstance();
        start.setTime(startDate);
        end.setTime(endDate);
        BookingWSServiceStub stub = new BookingWSServiceStub(Configuration.INSTANCE.getBookingEndpoint());
        stub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext()
                        .getUser().getPassword()));
        cars = stub.getCarsForAdvanceBooking(
                getContext().getUser().getCarClub().getCarClubCode(), zone, location, start, end);

        return new ForwardResolution("/WEB-INF/book/carListAdvance.jsp");
    }

    /**
     * Search Result List
     * 
     * @return the page resolution
     * @throws RemoteException
     * @throws com.criticalsoftware.mobics.proxy.fleet.CarLicensePlateNotFoundExceptionException
     */
    public Resolution licensePlateBookAdvance() throws RemoteException,
            com.criticalsoftware.mobics.proxy.fleet.CarLicensePlateNotFoundExceptionException {
        return new ForwardResolution("/WEB-INF/book/carBookAdvance.jsp");
    }

    // --------------------------------------------------
    // Bookin Resolutions
    // --------------------------------------------------

    /**
     * Show PIN resolution
     * 
     * @return the pin page resolution
     */
    public Resolution showPin() {
        return new ForwardResolution("/WEB-INF/book/pinAdvance.jsp");
    }

    /**
     * Booking
     * 
     * @return
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws InvalidCustomerPinExceptionException
     * @throws CarNotFoundExceptionException
     * @throws CarNotAvailableForBookingExceptionException
     * @throws ForbiddenZoneExceptionException
     * @throws IllegalDateExceptionException
     * @throws UnauthorizedCustomerExceptionException
     * @throws CarLicensePlateNotFoundExceptionException
     * @throws InvalidCarBookingExceptionException 
     */
    public Resolution book() throws RemoteException, UnsupportedEncodingException,
            InvalidCustomerPinExceptionException, CarNotFoundExceptionException,
            CarNotAvailableForBookingExceptionException, ForbiddenZoneExceptionException,
            IllegalDateExceptionException, UnauthorizedCustomerExceptionException,
            CarLicensePlateNotFoundExceptionException, OverlappedCarBookingExceptionException,
            OverlappedCustomerBookingExceptionException, InvalidCarBookingExceptionException {
        Calendar start = Calendar.getInstance(), end = Calendar.getInstance();
        start.setTimeInMillis(startDate.getTime());
        end.setTimeInMillis(endDate.getTime());

        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));
        bookingWSServiceStub.createAdvanceBookingWithCustomerPin(licensePlate, String.valueOf(pin), start, end);

        getContext().getMessages().add(new LocalizableMessage("car.details.advance.book.done"));
        return new RedirectResolution(RecentActivitiesActionBean.class).flash(this);
    }

    // --------------------------------------------------
    // Detail Resolutions
    // --------------------------------------------------
    /**
     * Car details
     * 
     * @return the car detail page
     */
    public Resolution carDetails() {
        return new ForwardResolution("/WEB-INF/car/carDetailsAdvance.jsp");
    }

    /**
     * Park location
     * 
     * @return the map page
     */
    public Resolution parkLocation() {
        return new ForwardResolution("/WEB-INF/map/mapFullWidth.jsp").addParameter("licensePlate", licensePlate);
    }

    /**
     * Data for build maps
     * 
     * @return a json object containing car data for display in map
     * @throws RemoteException a jax-b webservice exception
     */
    public Resolution parkData() throws RemoteException {

        // Get the first car
        FleetWSServiceStub fleet = new FleetWSServiceStub(Configuration.INSTANCE.getFleetEndpoint());
        ZoneWithPolygonDTO[] zones = null;
        try {
            zones = fleet.getCarZonesWithPolygons(licensePlate);
            getContext().getResponse().setHeader("Stripes-Success", "OK");
        } catch (com.criticalsoftware.mobics.proxy.fleet.CarLicensePlateNotFoundExceptionException e) {
            LOGGER.error("License plate not found", e);
        }

        return new JavaScriptResolution(new CoordinateZonesDTO(null, zones));
    }

    // --------------------------------------------------
    // Validation methods
    // --------------------------------------------------
    @ValidationMethod(
            on = { "licensePlateBook", "showPin", "carDetails" },
            when = ValidationState.NO_ERRORS,
            priority = 2)
    public void validateCarType(ValidationErrors errors) throws RemoteException,
            com.criticalsoftware.mobics.proxy.fleet.CarLicensePlateNotFoundExceptionException {
        if (!CarTypeEnum.SCHEDULE_BOOKING.equals(car.getCarType())) {
            errors.addGlobalError(new LocalizableError("car.details.validation.car.not.available"));
        }
    }

    /**
     * Get locations
     * 
     * @return location array
     * @throws RemoteException
     */
    public LocationDTO[] getLocations() throws RemoteException {
        return new CarClubWSServiceStub(Configuration.INSTANCE.getCarClubEndpoint())
                .getLocationsByCarClubCode(getContext().getUser().getCarClub().getCarClubCode());
    }

    /**
     * Get zones by location code
     * 
     * @return
     * @throws RemoteException
     */
    public Resolution getZones() throws RemoteException {
        ZoneDTO[] zones = null;
        try {
            zones = new CarClubWSServiceStub(Configuration.INSTANCE.getCarClubEndpoint())
                    .getParksByCarClubCodeAndLocationCode(getContext().getUser().getCarClub().getCarClubCode(),
                            location);
        } catch (LocationCodeNotFoundExceptionException e) {
            LOGGER.warn("Zones not found due to location code.", e.getLocalizedMessage());
        } catch (com.criticalsoftware.mobics.proxy.carclub.CarClubCodeNotFoundExceptionException e) {
            LOGGER.warn("Zones not found due to car clube code.", e.getLocalizedMessage());
        }
        getContext().getResponse().setHeader("Stripes-Success", "OK");
        return new JavaScriptResolution(zones);
    }

    /**
     * @return the zone
     */
    public String getZone() {
        return zone;
    }

    /**
     * @param zone the zone to set
     */
    public void setZone(String zone) {
        this.zone = zone;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
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
     * @return the cars
     */
    public CarDTO[] getCars() {
        return cars;
    }

    /**
     * @param cars the cars to set
     */
    public void setCars(CarDTO[] cars) {
        this.cars = cars;
    }

}
