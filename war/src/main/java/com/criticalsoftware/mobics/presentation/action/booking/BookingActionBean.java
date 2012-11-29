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
import java.math.BigDecimal;
import java.rmi.RemoteException;

import javax.activation.DataHandler;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;
import net.sourceforge.stripes.validation.ValidationState;

import com.criticalsoftware.mobics.fleet.CarDTO;
import com.criticalsoftware.mobics.presentation.action.BaseActionBean;
import com.criticalsoftware.mobics.presentation.extension.ZoneDTOTypeConverter;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.fleet.FleetWSServiceStub;
import com.criticalsoftware.mobics.proxy.fleet.IOExceptionException;

/**
 * @author ltiago
 * @version $Revision: $
 */
public abstract class BookingActionBean extends BaseActionBean {

    @ValidateNestedProperties({ @Validate(field = "licensePlate"), @Validate(field = "carBrandName"),
            @Validate(field = "carModelName"), @Validate(field = "fuelType.name"), @Validate(field = "range"),
            @Validate(field = "zones", converter = ZoneDTOTypeConverter.class) })
    protected CarDTO car;

    @Validate(required = true, on = { "getCarImage", "carLocation", "licensePlateBook", "showPin", "book",
            "licensePlateBookAdvance", "parkLocation", "carData", "parkData", "carDetails" })
    protected String licensePlate;

    @Validate(required = true, on = { "nearestCarBook", "licensePlateBook", "searchCars" })
    protected String latitude;

    @Validate(required = true, on = { "nearestCarBook", "licensePlateBook", "searchCars" })
    protected String longitude;

    @Validate(required = true, on = "book", minlength = 4, maxlength = 4)
    protected Integer pin;

    /**
     * Show pin to user
     * 
     * @return resolution page
     */
    public abstract Resolution showPin();

    /**
     * Book the specified car
     * 
     * @return sucess or error page
     * @throws Exception
     */
    public abstract Resolution book() throws Exception;

    /**
     * See car details
     * 
     * @return
     * @throws Exception
     */
    public abstract Resolution carDetails() throws Exception;

    /**
     * Validate and load the car
     * 
     * @throws RemoteException
     * @throws CarValidationExceptionException
     * @throws CarLicensePlateNotFoundExceptionException
     * @throws com.criticalsoftware.mobics.proxy.fleet.CarLicensePlateNotFoundExceptionException
     */
    @ValidationMethod(
            on = { "licensePlateBook", "showPin", "carDetails", "licensePlateBookAdvance" },
            when = ValidationState.NO_ERRORS,
            priority = 1)
    public void validateLicensePlateCar(ValidationErrors errors) throws RemoteException,
            com.criticalsoftware.mobics.proxy.fleet.CarLicensePlateNotFoundExceptionException {
        car = new FleetWSServiceStub(Configuration.INSTANCE.getFleetEndpoint()).getCarDetails(licensePlate
                .toUpperCase(), latitude == null ? null : new BigDecimal(latitude), longitude == null ? null
                : new BigDecimal(longitude));
        if (car == null) {
            errors.addGlobalError(new LocalizableError("car.details.validation.car.not.available"));
        }
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
     * @return the licensePlate
     */
    public String getLicensePlate() {
        return licensePlate;
    }

    /**
     * @param licensePlate the licensePlate to set
     */
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    /**
     * @return the pin
     */
    public Integer getPin() {
        return pin;
    }

    /**
     * @param pin the pin to set
     */
    public void setPin(Integer pin) {
        this.pin = pin;
    }

    /**
     * @return the latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
