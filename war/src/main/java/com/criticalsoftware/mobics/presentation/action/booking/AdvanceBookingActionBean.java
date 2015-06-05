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
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

import com.criticalsoftware.mobics.carclub.LocationDTO;
import com.criticalsoftware.mobics.carclub.ZoneDTO;
import com.criticalsoftware.mobics.fleet.CarDTO;
import com.criticalsoftware.mobics.fleet.CarTypeEnum;
import com.criticalsoftware.mobics.fleet.NextAvailableCarDTO;
import com.criticalsoftware.mobics.fleet.ZoneWithPolygonDTO;
import com.criticalsoftware.mobics.presentation.action.recent.RecentActivitiesActionBean;
import com.criticalsoftware.mobics.presentation.extension.DatetimeTypeConverter;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.presentation.util.CoordinateZonesDTO;
import com.criticalsoftware.mobics.proxy.booking.BookingWSServiceStub;
import com.criticalsoftware.mobics.proxy.booking.CarNotAvailableForBookingExceptionException;
import com.criticalsoftware.mobics.proxy.booking.ForbiddenZoneExceptionException;
import com.criticalsoftware.mobics.proxy.booking.InvalidCarBookingExceptionException;
import com.criticalsoftware.mobics.proxy.booking.InvalidCustomerPinExceptionException;
import com.criticalsoftware.mobics.proxy.booking.OverlappedCarBookingExceptionException;
import com.criticalsoftware.mobics.proxy.booking.OverlappedCustomerBookingExceptionException;
import com.criticalsoftware.mobics.proxy.booking.UnauthorizedCustomerExceptionException;
import com.criticalsoftware.mobics.proxy.carclub.CarClubWSServiceStub;
import com.criticalsoftware.mobics.proxy.carclub.LocationCodeNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.fleet.CarClubCodeNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.fleet.CarLicensePlateNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.fleet.CarNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.fleet.CustomerNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.fleet.FleetWSServiceStub;
import com.criticalsoftware.mobics.proxy.fleet.IllegalDateExceptionException;

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

    @Validate(required = true, on = { "searchCarsAdvance", "licensePlateBookAdvance", "showPin" },
            converter = DatetimeTypeConverter.class)
    private Date startDate;

    @Validate(required = true, on = { "searchCarsAdvance", "licensePlateBookAdvance", "showPin" },
            converter = DatetimeTypeConverter.class)
    private Date endDate;

    private CarDTO[] cars;

    private NextAvailableCarDTO next;

    /**
     * Show the advance book search screen
     *
     * @return resolution
     */
    @DontValidate
    @DefaultHandler
    public Resolution main() {
        final Calendar aux = Calendar.getInstance();
        aux.add(Calendar.HOUR_OF_DAY, 1);
        // Uncomment the following line to set the minutes to 0
        // aux.set(Calendar.MINUTE, 0);
        aux.set(Calendar.MILLISECOND, 0);

        this.startDate = new Date(aux.getTimeInMillis());
        aux.add(Calendar.HOUR_OF_DAY, 1);
        this.endDate = new Date(aux.getTimeInMillis());

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
            CarClubCodeNotFoundExceptionException, UnsupportedEncodingException,
            com.criticalsoftware.mobics.proxy.fleet.LocationCodeNotFoundExceptionException {
        ForwardResolution resolution = new ForwardResolution("/WEB-INF/book/carListAdvance.jsp");
        final Calendar start = Calendar.getInstance(), end = Calendar.getInstance();
        start.setTime(this.startDate);
        end.setTime(this.endDate);
        final FleetWSServiceStub stub = new FleetWSServiceStub(Configuration.INSTANCE.getFleetEndpoint());
        stub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext()
                        .getUser().getPassword()));
        this.cars = stub.getCarsForAdvanceBooking(this.getContext().getUser().getCarClub().getCarClubCode(), this.zone,
                this.location, start.getTimeInMillis(), end.getTimeInMillis());

        if ((this.cars == null) || (this.cars.length == 0)) {
            this.next = stub.getNextAvailableCarForAdvanceBooking(this.getContext().getUser().getCarClub()
                    .getCarClubCode(), this.location, start.getTimeInMillis(), end.getTimeInMillis());
            if (this.next != null) {
                resolution = new ForwardResolution("/WEB-INF/book/searchAdvance.jsp").addParameter("next", true);
            }
        }

        return resolution;
    }

    public Resolution nextAvailableCar() throws RemoteException, UnsupportedEncodingException,
            IllegalDateExceptionException,
            com.criticalsoftware.mobics.proxy.fleet.LocationCodeNotFoundExceptionException,
            CarLicensePlateNotFoundExceptionException, CarClubCodeNotFoundExceptionException,
            CustomerNotFoundExceptionException {

        final Calendar start = Calendar.getInstance(), end = Calendar.getInstance();
        start.setTime(this.startDate);
        end.setTime(this.endDate);
        final FleetWSServiceStub stub = new FleetWSServiceStub(Configuration.INSTANCE.getFleetEndpoint());
        stub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext()
                        .getUser().getPassword()));
        this.next = stub.getNextAvailableCarForAdvanceBooking(
                this.getContext().getUser().getCarClub().getCarClubCode(), this.location, start.getTimeInMillis(),
                end.getTimeInMillis());

        return new ForwardResolution("/WEB-INF/book/nextAvailableCar.jsp");
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
    @Override
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
    @Override
    public Resolution book() throws RemoteException, UnsupportedEncodingException,
            InvalidCustomerPinExceptionException,
            com.criticalsoftware.mobics.proxy.booking.CarNotFoundExceptionException,
            CarNotAvailableForBookingExceptionException, ForbiddenZoneExceptionException,
            com.criticalsoftware.mobics.proxy.booking.IllegalDateExceptionException,
            UnauthorizedCustomerExceptionException,
    com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException,
            OverlappedCarBookingExceptionException, OverlappedCustomerBookingExceptionException,
            InvalidCarBookingExceptionException {
        final Calendar start = Calendar.getInstance(), end = Calendar.getInstance();
        start.setTimeInMillis(this.startDate.getTime());
        end.setTimeInMillis(this.endDate.getTime());

        final BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext()
                        .getUser().getPassword()));
        bookingWSServiceStub.createAdvanceBookingWithCustomerPin(this.licensePlate, String.valueOf(this.pin),
                start.getTimeInMillis(), end.getTimeInMillis());

        this.getContext().getMessages().add(new LocalizableMessage("car.details.advance.book.done"));
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
    @Override
    public Resolution carDetails() {
        return new ForwardResolution("/WEB-INF/car/carDetailsAdvance.jsp");
    }

    /**
     * Park location
     *
     * @return the map page
     */
    public Resolution parkLocation() {
        return new ForwardResolution("/WEB-INF/map/mapFullWidth.jsp").addParameter("licensePlate", this.licensePlate);
    }

    /**
     * Data for build maps
     *
     * @return a json object containing car data for display in map
     * @throws RemoteException a jax-b webservice exception
     */
    public Resolution parkData() throws RemoteException {

        // Get the first car
        final FleetWSServiceStub fleet = new FleetWSServiceStub(Configuration.INSTANCE.getFleetEndpoint());
        ZoneWithPolygonDTO[] zones = null;
        try {
            zones = fleet.getCarZonesWithPolygons(this.licensePlate);
            this.getContext().getResponse().setHeader("Stripes-Success", "OK");
        } catch (final com.criticalsoftware.mobics.proxy.fleet.CarLicensePlateNotFoundExceptionException e) {
            LOGGER.error("License plate not found", e);
        }

        return new JavaScriptResolution(new CoordinateZonesDTO(null, zones));
    }

    // --------------------------------------------------
    // Validation methods
    // --------------------------------------------------
    @ValidationMethod(on = { "licensePlateBook", "showPin", "carDetails" }, when = ValidationState.NO_ERRORS,
            priority = 2)
    public void validateCarType(final ValidationErrors errors) throws RemoteException,
    com.criticalsoftware.mobics.proxy.fleet.CarLicensePlateNotFoundExceptionException {
        if (!CarTypeEnum.SCHEDULE_BOOKING.equals(this.car.getCarType())) {
            errors.addGlobalError(new LocalizableError("car.details.validation.car.not.available"));
        }
    }

    /**
     * Get locations
     *
     * @return location array
     * @throws RemoteException
     */
    public Collection<LocationDTO> getLocations() throws RemoteException {
        final Map<String, LocationDTO> mapLocations = new HashMap<String, LocationDTO>();
        final LocationDTO[] locations = new CarClubWSServiceStub(Configuration.INSTANCE.getCarClubEndpoint())
        .getLocationsByCarClubCode(this.getContext().getUser().getCarClub().getCarClubCode());

        for (final LocationDTO l : locations) {
            if (!mapLocations.containsKey(l.getCode())) {
                mapLocations.put(l.getCode(), l);
            }
        }

        return mapLocations.values();
    }

    /**
     * Get zones by location code
     *
     * @return
     * @throws RemoteException
     */
    public Resolution getZones() throws RemoteException {
        ZoneDTO[] zones = null;
        final Set<String> fileredZones = new HashSet<String>();
        try {
            zones = new CarClubWSServiceStub(Configuration.INSTANCE.getCarClubEndpoint())
            .getParksByCarClubCodeAndLocationCode(this.getContext().getUser().getCarClub().getCarClubCode(),
                    this.location);

            for (final ZoneDTO zone : zones) {
                if (!fileredZones.contains(zone.getZone())) {
                    fileredZones.add(zone.getZone());
                }
            }

        } catch (final LocationCodeNotFoundExceptionException e) {
            LOGGER.warn("Zones not found due to location code.", e.getLocalizedMessage());
        } catch (final com.criticalsoftware.mobics.proxy.carclub.CarClubCodeNotFoundExceptionException e) {
            LOGGER.warn("Zones not found due to car clube code.", e.getLocalizedMessage());
        }
        this.getContext().getResponse().setHeader("Stripes-Success", "OK");
        return new JavaScriptResolution(fileredZones);
    }

    /**
     * @return the zone
     */
    public String getZone() {
        return this.zone;
    }

    /**
     * @param zone the zone to set
     */
    public void setZone(final String zone) {
        this.zone = zone;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(final String location) {
        this.location = location;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return this.startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
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
     * @return the cars
     */
    public CarDTO[] getCars() {
        return this.cars;
    }

    /**
     * @param cars the cars to set
     */
    public void setCars(final CarDTO[] cars) {
        this.cars = cars;
    }

    /**
     * Next available car
     *
     * @return next car
     */
    public NextAvailableCarDTO getNext() {
        return this.next;
    }
}
