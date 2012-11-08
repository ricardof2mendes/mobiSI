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

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.ajax.JavaScriptResolution;
import net.sourceforge.stripes.validation.EnumeratedTypeConverter;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;
import net.sourceforge.stripes.validation.ValidationState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.criticalsoftware.mobics.fleet.CarDTO;
import com.criticalsoftware.mobics.fleet.CarTypeEnum;
import com.criticalsoftware.mobics.fleet.CoordinateDTO;
import com.criticalsoftware.mobics.fleet.ZoneWithPolygonDTO;
import com.criticalsoftware.mobics.presentation.action.trip.TripActionBean;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.CarClazz;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.presentation.util.CoordinateZonesDTO;
import com.criticalsoftware.mobics.presentation.util.FuelType;
import com.criticalsoftware.mobics.presentation.util.GeolocationUtil;
import com.criticalsoftware.mobics.presentation.util.OrderBy;
import com.criticalsoftware.mobics.proxy.booking.BookingWSServiceStub;
import com.criticalsoftware.mobics.proxy.booking.CarNotAvailableForBookingExceptionException;
import com.criticalsoftware.mobics.proxy.booking.ForbiddenZoneExceptionException;
import com.criticalsoftware.mobics.proxy.booking.InvalidCustomerPinExceptionException;
import com.criticalsoftware.mobics.proxy.booking.UnauthorizedCustomerExceptionException;
import com.criticalsoftware.mobics.proxy.fleet.CarClassNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.fleet.CarLicensePlateNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.fleet.CarNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.fleet.CarTypeNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.fleet.FleetWSServiceStub;
import com.criticalsoftware.mobics.proxy.fleet.FuelTypeNotFoundExceptionException;

/**
 * Booking action bean
 * 
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class ImmediateBookingActionBean extends BookingActionBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImmediateBookingActionBean.class);

    @Validate
    private BigDecimal price;

    @Validate
    private BigDecimal distance;

    @Validate(required = true, on = "searchCars", converter = EnumeratedTypeConverter.class)
    private CarClazz clazz;

    @Validate(required = true, on = "searchCars", converter = EnumeratedTypeConverter.class)
    private FuelType fuel;

    @Validate(required = true, on = "searchCars", converter = EnumeratedTypeConverter.class)
    private OrderBy orderBy;

    private CarDTO[] cars;

    /**
     * Main resolution just to avoid errors when no method name on parameter
     * 
     * @return Resolution to home page
     */
    @DontValidate
    @DefaultHandler
    public Resolution main() {
        return new ForwardResolution("/WEB-INF/home.jsp");
    }

    // --------------------------------------------------
    // License Plate Resolutions
    // --------------------------------------------------

    /**
     * Search cars by license plate
     * 
     * @return the page resolution
     */
    @DontValidate
    public Resolution licensePlateSearch() {
        return new ForwardResolution("/WEB-INF/book/searchLicensePlate.jsp");
    }

    /**
     * Car booking by license plate
     * 
     * @return the page resolution
     */
    public Resolution licensePlateBook() {
        return new ForwardResolution("/WEB-INF/book/carBookImmediate.jsp").addParameter("title", 1);
    }

    /**
     * Autocomplete list for search by license plate
     * 
     * @return the page resolution
     * @throws RemoteException a jax-b webservice exception
     * @throws CarTypeNotFoundExceptionException when car type not found
     */
    public Resolution licensePlateAutocomplete() throws RemoteException, CarTypeNotFoundExceptionException {
        if (licensePlate != null) {
            cars = new FleetWSServiceStub(Configuration.INSTANCE.getFleetEndpoint()).getCarsByLicensePlate(
                    licensePlate, CarTypeEnum.NORMAL.getValue(), new BigDecimal(latitude),
                    new BigDecimal(longitude));
            getContext().getResponse().setHeader("Stripes-Success", "OK");
        
        }
        return new ForwardResolution("/WEB-INF/book/carListImmediate.jsp");
    }

    // --------------------------------------------------
    // Nearest Car Resolutions
    // --------------------------------------------------

    /**
     * Booking page of the nearest car
     * 
     * @return the booking page resolution
     */
    public Resolution nearestCarBook() {
        return new ForwardResolution("/WEB-INF/book/carBookImmediate.jsp").addParameter("title", 2);
    }

    /**
     * Search and validate the car for booking
     * 
     * @throws RemoteException
     * @throws FuelTypeNotFoundExceptionException
     * @throws CarClassNotFoundExceptionException
     * @throws CarTypeNotFoundExceptionException
     * @throws CarValidationExceptionException
     */
    @ValidationMethod(on = "nearestCarBook", when = ValidationState.NO_ERRORS)
    public void validateNearestCar(ValidationErrors errors) throws RemoteException, FuelTypeNotFoundExceptionException,
            CarClassNotFoundExceptionException, CarTypeNotFoundExceptionException {
        CarDTO[] dtos = new FleetWSServiceStub(Configuration.INSTANCE.getFleetEndpoint()).searchCars(null, null,
                CarClazz.NOT_SPECIFIED.getClazz(), FuelType.NOT_SPECIFIED.getType(), OrderBy.DISTANCE.name(),
                Configuration.INSTANCE.getMinResults(), new BigDecimal(latitude), new BigDecimal(longitude),
                CarTypeEnum.NORMAL.getValue());
        if (dtos != null) {
            car = dtos[0];
            if (car == null) {
                errors.addGlobalError(new LocalizableError("car.details.validation.car.not.found"));
            }
        }
    }

    // ---------------------------------------------------------
    // Search by criterias (price, distance, clazz) Resolutions
    // ---------------------------------------------------------

    /**
     * Search cars resolution
     * 
     * @throws RemoteException
     * @throws FuelTypeNotFoundExceptionException
     * @throws CarClassNotFoundExceptionException
     * @throws CarTypeNotFoundExceptionException
     * @throws CarValidationExceptionException
     * @return the page resolution
     */
    public Resolution searchCars() throws RemoteException, FuelTypeNotFoundExceptionException,
            CarClassNotFoundExceptionException, CarTypeNotFoundExceptionException {
        cars = new FleetWSServiceStub(Configuration.INSTANCE.getFleetEndpoint()).searchCars(price, distance,
                clazz.getClazz(), fuel.getType(), orderBy.name(), Configuration.INSTANCE.getMaxResults(),
                new BigDecimal(latitude), new BigDecimal(longitude), CarTypeEnum.NORMAL.getValue());
        return new ForwardResolution("/WEB-INF/book/searchImmediate.jsp");
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
        return new ForwardResolution("/WEB-INF/book/pinImmediate.jsp");
    }

    /**
     * Booking resolution
     * 
     * @return
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws InvalidCustomerPinExceptionException
     * @throws com.criticalsoftware.mobics.proxy.booking.CarNotFoundExceptionException
     * @throws CarNotAvailableForBookingExceptionException
     * @throws UnauthorizedCustomerExceptionException
     * @throws com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException
     * @throws ForbiddenZoneExceptionException
     */
    public Resolution book() throws RemoteException, UnsupportedEncodingException,
            InvalidCustomerPinExceptionException,
            com.criticalsoftware.mobics.proxy.booking.CarNotFoundExceptionException,
            CarNotAvailableForBookingExceptionException, UnauthorizedCustomerExceptionException,
            com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException,
            ForbiddenZoneExceptionException {

        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));
        bookingWSServiceStub.createImmediateBookingWithCustomerPin(licensePlate, String.valueOf(pin));
        getContext().getMessages().add(new LocalizableMessage("car.details.book.done"));

        return new RedirectResolution(TripActionBean.class).flash(this);
    }

    // --------------------------------------------------
    // Shared Resolutions
    // --------------------------------------------------

    /**
     * Car details
     * 
     * @return the car detail page
     */
    public Resolution carDetails() {
        return new ForwardResolution("/WEB-INF/car/carDetails.jsp");
    }

    /**
     * Car location on map
     * 
     * @return the car location page resolution
     */
    public Resolution carLocation() {
        return new ForwardResolution("/WEB-INF/book/carLocation.jsp").addParameter("licensePlate", licensePlate);
    }

    /**
     * Data for build maps
     * 
     * @return a json object containing car data for display in map
     * @throws RemoteException a jax-b webservice exception
     */
    public Resolution carData() throws RemoteException {

        // Get the first car
        FleetWSServiceStub fleet = new FleetWSServiceStub(Configuration.INSTANCE.getFleetEndpoint());
        CoordinateDTO coordinate = null;
        ZoneWithPolygonDTO[] zones = null;
        try {
            coordinate = fleet.getCarCoordinatesByLicensePlate(licensePlate);
            zones = fleet.getCarZonesWithPolygons(licensePlate);
            getContext().getResponse().setHeader("Stripes-Success", "OK");
        } catch (CarNotFoundExceptionException e) {
            LOGGER.error("Car not found", e);
        } catch (CarLicensePlateNotFoundExceptionException e) {
            LOGGER.error("License plate not found", e);
        }

        return new JavaScriptResolution(new CoordinateZonesDTO(coordinate, zones));
    }

    // Validation Methods
    @ValidationMethod(
            on = { "licensePlateBook", "showPin", "carDetails" },
            when = ValidationState.NO_ERRORS,
            priority = 2)
    public void validateCarType(ValidationErrors errors) throws RemoteException,
            com.criticalsoftware.mobics.proxy.fleet.CarLicensePlateNotFoundExceptionException {
        if (!CarTypeEnum.NORMAL.equals(car.getCarType())) {
            errors.addGlobalError(new LocalizableError("car.details.validation.car.not.available"));
        }
    }

    /**
     * Reverse geolocation string
     * 
     * @return the address string
     */
    public String getLocation() {
        String location = new LocalizableMessage("application.value.not.available").getMessage(getContext().getLocale());
        if (car.getLatitude() != null && car.getLongitude() != null) {
            location = GeolocationUtil.getAddressFromCoordinates(car.getLatitude().toString(), car.getLongitude()
                    .toString());
        }
        return location;
    }

    /**
     * @return the cars
     */
    public CarDTO[] getCars() {
        return cars;
    }

    /**
     * @return the orderBy
     */
    public OrderBy getOrderBy() {
        return orderBy;
    }

    /**
     * @param orderBy the orderBy to set
     */
    public void setOrderBy(OrderBy orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the distance
     */
    public BigDecimal getDistance() {
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

    /**
     * @return the clazz
     */
    public CarClazz getClazz() {
        return clazz;
    }

    /**
     * @param clazz the clazz to set
     */
    public void setClazz(CarClazz clazz) {
        this.clazz = clazz;
    }

    /**
     * @return the fuel
     */
    public FuelType getFuel() {
        return fuel;
    }

    /**
     * @param fuel the fuel to set
     */
    public void setFuel(FuelType fuel) {
        this.fuel = fuel;
    }

}
