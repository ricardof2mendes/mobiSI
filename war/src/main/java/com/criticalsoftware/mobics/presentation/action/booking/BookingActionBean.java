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

import javax.activation.DataHandler;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.ajax.JavaScriptResolution;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;
import net.sourceforge.stripes.validation.ValidationState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.criticalsoftware.mobics.fleet.CarDTO;
import com.criticalsoftware.mobics.miscellaneous.ChargingStationDTO;
import com.criticalsoftware.mobics.miscellaneous.ChargingStationSimpleDTO;
import com.criticalsoftware.mobics.presentation.action.BaseActionBean;
import com.criticalsoftware.mobics.presentation.extension.ZoneDTOTypeConverter;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.proxy.fleet.FleetWSServiceStub;
import com.criticalsoftware.mobics.proxy.fleet.IOExceptionException;
import com.criticalsoftware.mobics.proxy.miscellaneous.MiscellaneousWSService;
import com.criticalsoftware.mobics.proxy.miscellaneous.MiscellaneousWSServiceStub;

/**
 * @author ltiago
 * @version $Revision: $
 */
public abstract class BookingActionBean extends BaseActionBean {

    protected final Logger LOG = LoggerFactory.getLogger(BookingActionBean.class);

    @ValidateNestedProperties({ @Validate(field = "licensePlate"), @Validate(field = "carBrandName"),
        @Validate(field = "carModelName"), @Validate(field = "fuelType.name"), @Validate(field = "range"),
        @Validate(field = "zones", converter = ZoneDTOTypeConverter.class) })
    protected CarDTO car;

    @Validate(required = true, on = { "getCarImage", "carLocation", "licensePlateBook", "licensePlateBookFromMessages",
            "showPin", "book", "licensePlateBookAdvance", "parkLocation", "carData", "parkData", "carDetails" })
    protected String licensePlate;

    @Validate(required = true, on = "nearestCarBook")
    protected String latitude;

    @Validate(required = true, on = "nearestCarBook")
    protected String longitude;

    @Validate(required = true, on = "book", minlength = 4, maxlength = 4)
    protected Integer pin;

    @Validate(required = true, on = "chargingStation")
    protected Integer id;

    @Validate
    protected Integer width = 58;

    @Validate
    protected Integer height = 58;

    protected ChargingStationDTO station;

    protected String stationAddress;

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
     * @throws com.criticalsoftware.mobics.proxy.fleet.CarLicensePlateNotFoundExceptionException
     * @throws UnsupportedEncodingException
     */
    @ValidationMethod(on = { "licensePlateBook", "licensePlateBookFromMessages", "showPin", "carDetails",
    "licensePlateBookAdvance" }, when = ValidationState.NO_ERRORS, priority = 1)
    public void validateLicensePlateCar(final ValidationErrors errors) throws RemoteException,
    com.criticalsoftware.mobics.proxy.fleet.CarLicensePlateNotFoundExceptionException,
    UnsupportedEncodingException {
        final FleetWSServiceStub fleetWSServiceStub = new FleetWSServiceStub(Configuration.INSTANCE.getFleetEndpoint());
        fleetWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext().getUser()
                        .getPassword()));

        this.car = fleetWSServiceStub.getCarDetails(this.licensePlate.toUpperCase(), this.latitude == null ? null : new BigDecimal(
                this.latitude), this.longitude == null ? null : new BigDecimal(this.longitude));
        if (this.car == null) {
            errors.addGlobalError(new LocalizableError("car.details.validation.car.not.available"));
        }
    }

    /**
     * Get the car image
     *
     * @return the image stream resolution
     * @throws RemoteException a jax-b webservice exception
     * @throws IOExceptionException a exception with car validation
     */
    public Resolution getCarImage() {
        Resolution resolution = null;
        try {
            final int w = this.getContext().getRetina() ? 2 * this.width : this.width;
            final int h = this.getContext().getRetina() ? 2 * this.height : this.height;

            final DataHandler handler = new FleetWSServiceStub(Configuration.INSTANCE.getFleetEndpoint()).getCarThumbnail(
                    this.licensePlate, w, h);
            resolution = new StreamingResolution(handler.getContentType(), handler.getInputStream());
        } catch (final Exception e) {
            this.LOG.warn("Could not load image", e.getMessage());
        }
        return resolution;
    }

    /**
     * Get the car intenal image
     *
     * @return the image stream resolution
     * @throws RemoteException a jax-b webservice exception
     * @throws IOExceptionException a exception with car validation
     */
    public Resolution getCarInternalImage() {
        Resolution resolution = null;
        try {
            final int w = this.getContext().getRetina() ? 2 * this.width : this.width;
            final int h = this.getContext().getRetina() ? 2 * this.height : this.height;

            final DataHandler handler = new FleetWSServiceStub(Configuration.INSTANCE.getFleetEndpoint())
                    .getCarInternalSketch(this.licensePlate, w, h);
            resolution = new StreamingResolution(handler.getContentType(), handler.getInputStream());
        } catch (final Exception e) {
            this.LOG.warn("Could not load image", e.getMessage());
        }
        return resolution;
    }

    /**
     * Get the car intenal image
     *
     * @return the image stream resolution
     * @throws RemoteException a jax-b webservice exception
     * @throws IOExceptionException a exception with car validation
     */
    public Resolution getCarExternalImage() {
        Resolution resolution = null;
        try {
            final int w = this.getContext().getRetina() ? 2 * this.width : this.width;
            final int h = this.getContext().getRetina() ? 2 * this.height : this.height;

            final DataHandler handler = new FleetWSServiceStub(Configuration.INSTANCE.getFleetEndpoint())
                    .getCarExternalSketch(this.licensePlate, w, h);
            resolution = new StreamingResolution(handler.getContentType(), handler.getInputStream());
        } catch (final Exception e) {
            this.LOG.warn("Could not load image", e.getMessage());
        }
        return resolution;
    }

    public Resolution basePrice(){
        return new ForwardResolution("/WEB-INF/common/price.jsp");
    }

    private String getFullAdress(){
        String streetNumber = "";
        if((this.station.getAddress().getNumber() != null) && (this.station.getAddress().getNumber().equals("-") == false) ){
            streetNumber = this.station.getAddress().getNumber() + ", ";
        }

        String postalCode = "";
        if((this.station.getAddress().getPostalCode() != null) && (this.station.getAddress().getPostalCode().equals("0000-000") == false)){
            postalCode = this.station.getAddress().getPostalCode() + ", ";
        }

        final String fullAddress = this.station.getAddress().getStreet() + ", " + streetNumber + postalCode + this.station.getAddress().getCity();
        return fullAddress;
    }

    public Resolution chargingStation() throws RemoteException {
        final MiscellaneousWSService mix = new MiscellaneousWSServiceStub(Configuration.INSTANCE.getMiscellaneousEnpoint());
        this.station = mix.getMobiEChargingStationDetails(this.id);
        this.stationAddress = this.getFullAdress();
        return new ForwardResolution("/WEB-INF/common/chargingStations.jsp");
    }

    /**
     * Data for build maps
     *
     * @return a json object containing charging stations for display in map
     * @throws RemoteException a jax-b webservice exception
     */
    public Resolution chargingStationsData() throws RemoteException {
        final MiscellaneousWSService mix = new MiscellaneousWSServiceStub(Configuration.INSTANCE.getMiscellaneousEnpoint());
        final ChargingStationSimpleDTO[] stations = mix.getMobiEChargingStations();
        this.getContext().getResponse().setHeader("Stripes-Success", "OK");
        return new JavaScriptResolution(stations);
    }

    /**
     * @return the car
     */
    public CarDTO getCar() {
        return this.car;
    }

    /**
     * @param car the car to set
     */
    public void setCar(final CarDTO car) {
        this.car = car;
    }

    /**
     * @return the licensePlate
     */
    public String getLicensePlate() {
        return this.licensePlate;
    }

    /**
     * @param licensePlate the licensePlate to set
     */
    public void setLicensePlate(final String licensePlate) {
        this.licensePlate = licensePlate;
    }

    /**
     * @return the pin
     */
    public Integer getPin() {
        return this.pin;
    }

    /**
     * @param pin the pin to set
     */
    public void setPin(final Integer pin) {
        this.pin = pin;
    }

    /**
     * @return the latitude
     */
    public String getLatitude() {
        return this.latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(final String latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public String getLongitude() {
        return this.longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(final String longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the width
     */
    public Integer getWidth() {
        return this.width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(final Integer width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public Integer getHeight() {
        return this.height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(final Integer height) {
        this.height = height;
    }

    /**
     * Get the id
     * @return   id
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Sets the id
     * @param id
     */
    public void setId(final Integer id) {
        this.id = id;
    }

    public ChargingStationDTO getStation() {
        return this.station;
    }

    public String getStationAddress() {
        return this.stationAddress;
    }

    public void setStationAddress(final String stationAddress) {
        this.stationAddress = stationAddress;
    }
}
