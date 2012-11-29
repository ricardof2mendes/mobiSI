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
package com.criticalsoftware.mobics.presentation.action.contacts;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;

import javax.activation.DataHandler;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.validation.BooleanTypeConverter;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;
import net.sourceforge.stripes.validation.ValidationState;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.criticalsoftware.mobics.booking.CurrentTripDTO;
import com.criticalsoftware.mobics.fleet.CarDTO;
import com.criticalsoftware.mobics.presentation.action.BaseActionBean;
import com.criticalsoftware.mobics.presentation.extension.DatetimeTypeConverter;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.proxy.booking.BookingWSServiceStub;
import com.criticalsoftware.mobics.proxy.booking.CustomerNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.car.BookingNotFoundForEventExceptionException;
import com.criticalsoftware.mobics.proxy.car.CarWSServiceStub;
import com.criticalsoftware.mobics.proxy.car.EventInClosedStateExceptionException;
import com.criticalsoftware.mobics.proxy.car.EventValidationExceptionException;
import com.criticalsoftware.mobics.proxy.carclub.CarClubWSServiceStub;
import com.criticalsoftware.mobics.proxy.carclub.IOExceptionException;
import com.criticalsoftware.mobics.proxy.fleet.CarLicensePlateNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.fleet.FleetWSServiceStub;

/**
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class ContactsAndDamageReportActionBean extends BaseActionBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(ContactsAndDamageReportActionBean.class);

    private static final int LOGO_WIDTH = 44;
    
    private static final int LOGO_HEIGHT = 44;

    @Validate(required = true, on = "saveDamageReport")
    private String type;

    @Validate(required = true, on = { "licensePlateSearch", "saveDamageReport" })
    private String licensePlate;

    @Validate(required = true, on = "saveDamageReport", converter = DatetimeTypeConverter.class)
    private Date date;

    @Validate(required = true, on = "saveDamageReport", converter = BooleanTypeConverter.class)
    private boolean assume;

    @Validate(required = true, on = "saveDamageReport", maxlength = 1024)
    private String description;

    @ValidateNestedProperties({ @Validate(field = "licensePlate"), @Validate(field = "carBrandName"),
            @Validate(field = "carModelName"), @Validate(field = "fuelType.name") })
    private CarDTO car;

    /**
     * Default handler
     * 
     * @return contacts page
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     */
    @DontValidate
    @DefaultHandler
    public Resolution main() throws RemoteException, UnsupportedEncodingException {
        return new ForwardResolution("/WEB-INF/contacts/contacts.jsp");
    }

    /**
     * Get the car club image
     * 
     * @return a streaming resolution
     * @throws IOException
     * @throws IOExceptionException
     */
    @DontValidate
    public Resolution carClubImage() throws IOException, IOExceptionException {
        StreamingResolution resolution = null;
        CarClubWSServiceStub carClubWSServiceStub = new CarClubWSServiceStub(
                Configuration.INSTANCE.getCarClubEndpoint());
        DataHandler data = carClubWSServiceStub.getCarClubThumbnailByCarClubCode(getContext().getUser().getCarClub()
                .getCarClubCode(), LOGO_WIDTH, LOGO_HEIGHT);
        if (data != null) {
            resolution = new StreamingResolution(data.getContentType(), data.getInputStream());
        }
        // FIXME to be removed just for testing porpuses
        else {
            HttpClient http = new HttpClient();
            GetMethod method = new GetMethod();
            method.setPath("http://api.ning.com/files/6Gvj9JpKpavtOgbeWj6W9I8tzplrhsDUtJqd1ARnuSu485ej*IpVjkePIrS0Y6nEdPDJjtDSz6y7ywqzvFB5LGineyHCUVbj/663883059.jpeg?xgip=1075%3A59%3A1351%3A1351%3B%3B&width=64&height=64&crop=1%3A1");
            int status = http.executeMethod(method);
            if (status == HttpStatus.SC_OK) {
                resolution = new StreamingResolution(method.getResponseHeader("Content-Type").getValue(),
                        new ByteArrayInputStream(method.getResponseBody()));
            }
        }
        return resolution;
    }

    /**
     * Loads the damage report page, and the car in use
     * 
     * @return
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws CustomerNotFoundExceptionException
     * @throws CarLicensePlateNotFoundExceptionException
     */
    @DontValidate
    public Resolution damageReport() throws RemoteException, UnsupportedEncodingException,
            CustomerNotFoundExceptionException, CarLicensePlateNotFoundExceptionException {
        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));

        CurrentTripDTO current = bookingWSServiceStub.getCurrentTripDetails();
        if (current != null && current.getLicensePlate() != null) {
            licensePlate = current.getLicensePlate();
            car = new FleetWSServiceStub(Configuration.INSTANCE.getFleetEndpoint()).getCarDetails(licensePlate, null,
                    null);
        }
        return new ForwardResolution("/WEB-INF/contacts/damageReport.jsp");
    }

    /**
     * Ajax license plate search
     * 
     * @return
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     */
    public Resolution licensePlateSearch() throws RemoteException, UnsupportedEncodingException {
        try {
            car = new FleetWSServiceStub(Configuration.INSTANCE.getFleetEndpoint()).getCarDetails(
                    licensePlate.toUpperCase(), null, null);
        } catch (CarLicensePlateNotFoundExceptionException e) {
            LOGGER.debug("Car with license plate {} not found", licensePlate);
        }
        getContext().getResponse().setHeader("Stripes-Success", "OK");

        return new ForwardResolution("/WEB-INF/contacts/carComponent.jsp");
    }

    /**
     * Save damage report
     * 
     * @return
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws EventValidationExceptionException
     * @throws BookingNotFoundForEventExceptionException
     * @throws EventInClosedStateExceptionException
     * @throws com.criticalsoftware.mobics.proxy.car.CustomerNotFoundExceptionException
     */
    public Resolution saveDamageReport() throws RemoteException, UnsupportedEncodingException,
            EventValidationExceptionException, BookingNotFoundForEventExceptionException,
            EventInClosedStateExceptionException,
            com.criticalsoftware.mobics.proxy.car.CustomerNotFoundExceptionException {

        Calendar aux = Calendar.getInstance();
        aux.setTime(date);

        CarWSServiceStub carWSServiceStub = new CarWSServiceStub(Configuration.INSTANCE.getCarEndpoint());
        carWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));
        carWSServiceStub.reportNewIncident(aux, car.getLicensePlate(), assume, type, description);

        getContext().getMessages().add(new LocalizableMessage("damage.report.save.success"));
        return new RedirectResolution(this.getClass()).flash(this);
    }

    @ValidationMethod(on = "saveDamageReport", when = ValidationState.NO_ERRORS)
    public void validate(ValidationErrors errors) {
        if (car == null || car.getLicensePlate() == null) {
            errors.addGlobalError(new LocalizableError("damage.report.no.results.found"));
        }
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @param licensePlate the licensePlate to set
     */
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @param assume the assume to set
     */
    public void setAssume(boolean assume) {
        this.assume = assume;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the licensePlate
     */
    public String getLicensePlate() {
        return licensePlate;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return the assume
     */
    public boolean getAssume() {
        return assume;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
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
}
