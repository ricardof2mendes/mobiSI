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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.activation.DataHandler;

import net.sourceforge.stripes.action.After;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.ajax.JavaScriptResolution;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.validation.EnumeratedTypeConverter;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;
import net.sourceforge.stripes.validation.ValidationState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.criticalsoftware.mobics.fleet.CarDTO;
import com.criticalsoftware.mobics.fleet.CarTypeEnum;
import com.criticalsoftware.mobics.fleet.CoordinateDTO;
import com.criticalsoftware.mobics.fleet.ZoneWithPolygonDTO;
import com.criticalsoftware.mobics.presentation.action.BaseActionBean;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.CarClazz;
import com.criticalsoftware.mobics.presentation.util.CarState;
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
import com.criticalsoftware.mobics.proxy.fleet.CarValidationExceptionException;
import com.criticalsoftware.mobics.proxy.fleet.FleetWSServiceStub;
import com.criticalsoftware.mobics.proxy.fleet.FuelTypeNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.fleet.IOExceptionException;

/**
 * Booking action bean
 * 
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class ImmediateBookingActionBean extends BaseActionBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImmediateBookingActionBean.class);

    @Validate(required = true, on = { "getCarImage", "carLocation", "licensePlateBook", "showPin", "book" })
    protected String licensePlate;

    @Validate(required = true, on = { "getData", "ajax" })
    protected String q;

    @Validate(required = true, on = { "nearestCarBook", "licensePlateBook", "searchCars" })
    protected String latitude;

    @Validate(required = true, on = { "nearestCarBook", "licensePlateBook", "searchCars" })
    protected String longitude;

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

    @Validate(required = true, on = "book")
    private String pin;

    @ValidateNestedProperties({ @Validate(field = "licensePlate"), @Validate(field = "carBrandName"),
            @Validate(field = "carModelName"), @Validate(field = "fuelType"), @Validate(field = "range") })
    protected CarDTO car;

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
        return new ForwardResolution("/WEB-INF/book/licensePlate.jsp");
    }

    /**
     * Car booking by license plate
     * 
     * @return the page resolution
     */
    public Resolution licensePlateBook() {
        return new ForwardResolution("/WEB-INF/book/carBook.jsp").addParameter("title", 1);
    }

    /**
     * Autocomplete list for search by license plate
     * 
     * @return the page resolution
     * @throws RemoteException a jax-b webservice exception
     */
    public Resolution licensePlateAutocomplete() throws RemoteException {
        if (q != null) {
            try {
                cars = new FleetWSServiceStub(Configuration.INSTANCE.getFleetEndpoint()).getCarsByLicensePlate(q
                        .toUpperCase(), CarTypeEnum.NORMAL.getValue(), new BigDecimal(latitude), new BigDecimal(
                        longitude));
                getContext().getResponse().setHeader("Stripes-Success", "OK");
            } catch (CarValidationExceptionException e) {
                LOGGER.error("Car validation exception.", e);
            }
        }
        return new ForwardResolution("/WEB-INF/book/carList.jsp");
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
        return new ForwardResolution("/WEB-INF/book/carBook.jsp").addParameter("title", 2);
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
            CarClassNotFoundExceptionException, CarTypeNotFoundExceptionException, CarValidationExceptionException {
        CarDTO[] dtos = new FleetWSServiceStub(Configuration.INSTANCE.getFleetEndpoint()).searchCars(null, null,
                CarClazz.NOT_SPECIFIED.getClazz(), FuelType.NOT_SPECIFIED.getType(), OrderBy.DISTANCE.name(),
                Configuration.INSTANCE.getMinResults(), new BigDecimal(latitude), new BigDecimal(longitude),
                CarTypeEnum.NORMAL.getValue());
        // FIXME service should have car state on queries
        for (CarDTO carDto : dtos) {
            if (CarState.AVAILABLE.name().equals(carDto.getState())) {
                car = carDto;
                break;
            }
        }
        if (car == null) {
            errors.addGlobalError(new LocalizableError("car.details.validation.car.not.found"));
        }
    }

    // ---------------------------------------------------------
    // Search by criterias (price, distance, clazz) Resolutions
    // ---------------------------------------------------------

    /**
     * Search cars resolution
     * 
     * @return the page resolution
     */
    public Resolution searchCars() {
        return new ForwardResolution("/WEB-INF/book/searchCars.jsp");
    }

    /**
     * Fill data for search cars
     * 
     * @throws RemoteException
     * @throws FuelTypeNotFoundExceptionException
     * @throws CarClassNotFoundExceptionException
     * @throws CarTypeNotFoundExceptionException
     * @throws CarValidationExceptionException
     */
    @After(on = "searchCars", stages = LifecycleStage.BindingAndValidation)
    public void fillSearchData() throws RemoteException, FuelTypeNotFoundExceptionException,
            CarClassNotFoundExceptionException, CarTypeNotFoundExceptionException, CarValidationExceptionException {
        cars = new FleetWSServiceStub(Configuration.INSTANCE.getFleetEndpoint()).searchCars(price, distance,
                clazz.getClazz(), fuel.getType(), orderBy.name(), Configuration.INSTANCE.getMaxResults(),
                new BigDecimal(latitude), new BigDecimal(longitude), CarTypeEnum.NORMAL.getValue());
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
        return new ForwardResolution("/WEB-INF/book/pin.jsp");
    }

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
        bookingWSServiceStub.createImmediateBookingWithCustomerPin(licensePlate, pin);
        getContext().getMessages().add(new LocalizableMessage("car.details.book.done"));

        return new RedirectResolution(RecentHistoryActionBean.class).addParameter("_eventName", "current").flash(this);
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
        return new ForwardResolution("/WEB-INF/book/carDetails.jsp");
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
     * Get the car image
     * 
     * @return the image stream resolution
     * @throws RemoteException a jax-b webservice exception
     * @throws IOException a exception with car validation
     * @throws IOExceptionException a exception with car validation
     */
    public Resolution getCarImage() throws RemoteException, IOException, IOExceptionException {
        DataHandler handler = new FleetWSServiceStub(Configuration.INSTANCE.getFleetEndpoint()).getCarThumbnail(
                licensePlate, Configuration.INSTANCE.getCarThumbnailWidth(),
                Configuration.INSTANCE.getCarThumbnailHeight());
        return new StreamingResolution(handler.getContentType(), handler.getInputStream());
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
            coordinate = fleet.getCarCoordinatesByLicensePlate(q);
            zones = fleet.getCarZonesWithPolygons(q);
            getContext().getResponse().setHeader("Stripes-Success", "OK");
        } catch (CarNotFoundExceptionException e) {
            LOGGER.error("Car not found", e);
        } catch (CarLicensePlateNotFoundExceptionException e) {
            LOGGER.error("License plate not found", e);
        }

        return new JavaScriptResolution(new CoordinateZonesDTO(coordinate, zones));
    }

    // --------------------------------------------------
    // Validation Methods
    // --------------------------------------------------

    /**
     * Validate the car
     * 
     * @throws RemoteException
     * @throws CarValidationExceptionException
     * @throws CarLicensePlateNotFoundExceptionException
     */
    @ValidationMethod(on = { "licensePlateBook", "showPin" }, when = ValidationState.NO_ERRORS)
    public void validateLicensePlateCar(ValidationErrors errors) throws RemoteException,
            CarLicensePlateNotFoundExceptionException {
        car = new FleetWSServiceStub(Configuration.INSTANCE.getFleetEndpoint()).getCarDetails(
                licensePlate.toUpperCase(), new BigDecimal(latitude), new BigDecimal(longitude));
        if (car == null) {
            errors.addGlobalError(new LocalizableError("car.details.validation.car.not.available"));
        }
    }

    /**
     * Reverse geolocation string
     * 
     * @return the address string
     */
    public String getLocation() {
        return GeolocationUtil.getAddressFromCoordinates(car.getLatitude().toString(), car.getLongitude().toString());
    }

    /**
     * @param q the q to set
     */
    public void setQ(String q) {
        this.q = q;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * @return the longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * @param licensePlate the licensePlate to set
     */
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    /**
     * @return the car
     */
    public CarDTO getCar() {
        return car;
    }

    /**
     * @param car the car to set
     */
    public void setCar(CarDTO car) {
        this.car = car;
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

    /**
     * @return the pin
     */
    public String getPin() {
        return pin;
    }

    /**
     * @param pin the pin to set
     */
    public void setPin(String customerPin) {
        this.pin = customerPin;
    }

    /**
     * @return the licensePlate
     */
    public String getLicensePlate() {
        return licensePlate;
    }

}
