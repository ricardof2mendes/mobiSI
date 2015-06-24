/*
 * $Id: DamageReportActionBean.java 562 16/04/2015 18:13:54 embarros $
 *
 * Copyright (c) Critical Software S.A., All Rights Reserved.
 * (www.criticalsoftware.com)
 *
 * This software is the proprietary information of Critical Software S.A.
 * Use is subject to license terms.
 *
 * Last changed on: $Date: 16/04/2015 18:13:54 $
 * Last changed by: $Author: embarros $
 */
package com.criticalsoftware.mobics.presentation.action.trip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.ajax.JavaScriptResolution;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;
import net.sourceforge.stripes.validation.ValidationState;

import org.apache.axiom.attachments.ByteArrayDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.criticalsoftware.mobics.booking.CurrentTripDTO;
import com.criticalsoftware.mobics.car.CarDamageDetailsListDTO;
import com.criticalsoftware.mobics.car.FileAttachmentDTO;
import com.criticalsoftware.mobics.car.FileAttachmentTypeEnum;
import com.criticalsoftware.mobics.fleet.CarDTO;
import com.criticalsoftware.mobics.presentation.action.account.AskPinActionBean;
import com.criticalsoftware.mobics.presentation.extension.DatetimeTypeConverter;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.proxy.booking.BookingNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.booking.BookingWSServiceStub;
import com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.booking.CustomerNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.car.BookingNotFoundForEventExceptionException;
import com.criticalsoftware.mobics.proxy.car.CarNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.car.CarWSServiceStub;
import com.criticalsoftware.mobics.proxy.car.EventInClosedStateExceptionException;
import com.criticalsoftware.mobics.proxy.car.EventValidationExceptionException;
import com.criticalsoftware.mobics.proxy.customer.CustomerWSServiceStub;
import com.criticalsoftware.mobics.proxy.fleet.FleetWSServiceStub;

/**
 * @author embarros
 * @version $Revision: 562 $
 */
@MobiCSSecure
public class DamageReportActionBean extends AskPinActionBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(DamageReportActionBean.class);

    private static final String READY = "READY";

    @Validate(on = { "submitDamageReport" })
    private String licensePlate;

    @Validate(on = "submitDamageReport", converter = DatetimeTypeConverter.class)
    private Date date;

    @Validate(on = "submitDamageReport", maxlength = 1024)
    private String description;

    @Validate(on = { "submitDamageReport", "changeIncidentCode" })
    private String incidentCode;

    @Validate(on = { "submitDamageReport", "changeIncidentType" })
    private String incidentType;

    @Validate(on = { "submitDamageReport" })
    private String rowCoord;

    @Validate(on = { "submitDamageReport" })
    private String colCoord;

    @ValidateNestedProperties({ @Validate(field = "licensePlate"), @Validate(field = "carBrandName"),
        @Validate(field = "carModelName"), @Validate(field = "fuelType.name") })
    private CarDTO car;

    private List<FileBean> files;

    private FileAttachmentDTO[] imageFiles;

    private CarDamageDetailsListDTO[] carDamages;

    private final List<String> lines_exterior = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K");
    private final List<String> lines_interior = Arrays.asList("L", "M", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V");

    @Validate(required = true, on = { "data", "saveData" }, minlength = 4, maxlength = 4)
    private Integer pin;

    /**
     * Default handler
     *
     * @return contacts page
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws CustomerNotFoundExceptionException
     * @throws CarNotFoundExceptionException
     * @throws com.criticalsoftware.mobics.proxy.booking.CustomerNotFoundExceptionException
     * @throws CarLicensePlateNotFoundExceptionException
     * @throws com.criticalsoftware.mobics.proxy.car.CarLicensePlateNotFoundExceptionException
     * @throws com.criticalsoftware.mobics.proxy.fleet.CarLicensePlateNotFoundExceptionException
     */
    @DontValidate
    @DefaultHandler
    public Resolution getCurrent() throws RemoteException, UnsupportedEncodingException, CarNotFoundExceptionException,
    CustomerNotFoundExceptionException, CarLicensePlateNotFoundExceptionException,
    com.criticalsoftware.mobics.proxy.car.CarLicensePlateNotFoundExceptionException,
    com.criticalsoftware.mobics.proxy.fleet.CarLicensePlateNotFoundExceptionException {

        final BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext()
                        .getUser().getPassword()));

        final CurrentTripDTO current = bookingWSServiceStub.getCurrentTripDetails();

        if (current.getCarState().equals(READY) || !current.getShowDamageReport()) {
            return new RedirectResolution(TripActionBean.class);
        }

        if ((current != null) && (current.getLicensePlate() != null)) {
            this.licensePlate = current.getLicensePlate();
            final FleetWSServiceStub fleetWSServiceStub = new FleetWSServiceStub(
                    Configuration.INSTANCE.getFleetEndpoint());
            fleetWSServiceStub._getServiceClient().addHeader(
                    AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this
                            .getContext().getUser().getPassword()));

            final CarWSServiceStub carWSServiceStub = new CarWSServiceStub(Configuration.INSTANCE.getCarEndpoint());
            carWSServiceStub._getServiceClient().addHeader(
                    AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this
                            .getContext().getUser().getPassword()));

            this.carDamages = carWSServiceStub.getCarDamagesList(this.licensePlate);

            if (this.carDamages != null) {
                for (final CarDamageDetailsListDTO damages : this.carDamages) {
                    if (this.lines_exterior.contains(damages.getRow())) {
                        damages.setRow(String.valueOf(this.lines_exterior.indexOf(damages.getRow())));
                    } else if (this.lines_interior.contains(damages.getRow())) {
                        damages.setRow(String.valueOf(11 + this.lines_interior.indexOf(damages.getRow())));
                    }
                }
            }

            this.car = fleetWSServiceStub.getCarDetails(this.licensePlate, null, null);
            this.date = new Date();
            this.incidentCode = "INTERNAL_DAMAGE"; // INTERNAL_DAMAGE (default) or EXTERNAL_DAMAGE
            this.incidentType = "SCRATCHED"; // SCRATCHED(default) or SMASHED
        }
        return new ForwardResolution("/WEB-INF/trip/damageReport.jsp");

    }

    /**
     * Save damage report
     *
     * @return the page resolution
     * @throws EventValidationExceptionException
     * @throws BookingNotFoundForEventExceptionException
     * @throws EventInClosedStateExceptionException
     * @throws com.criticalsoftware.mobics.proxy.car.CustomerNotFoundExceptionException
     * @throws CarLicensePlateNotFoundExceptionException
     * @throws CarNotFoundExceptionException
     * @throws CustomerNotFoundExceptionException
     * @throws com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException
     * @throws com.criticalsoftware.mobics.proxy.fleet.CarLicensePlateNotFoundExceptionException
     * @throws IOException
     * @throws com.criticalsoftware.mobics.proxy.car.CarLicensePlateNotFoundExceptionException
     */
    public Resolution submitDamageReport() throws EventValidationExceptionException,
    BookingNotFoundForEventExceptionException, EventInClosedStateExceptionException,
    com.criticalsoftware.mobics.proxy.car.CustomerNotFoundExceptionException, CarNotFoundExceptionException,
    CarLicensePlateNotFoundExceptionException, CustomerNotFoundExceptionException,
    com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException,
    com.criticalsoftware.mobics.proxy.fleet.CarLicensePlateNotFoundExceptionException, IOException,
    com.criticalsoftware.mobics.proxy.car.CarLicensePlateNotFoundExceptionException {

        final BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext()
                        .getUser().getPassword()));

        final CurrentTripDTO current = bookingWSServiceStub.getCurrentTripDetails();
        if ((current != null) && (current.getLicensePlate() != null)) {
            this.licensePlate = current.getLicensePlate();
            final FleetWSServiceStub fleetWSServiceStub = new FleetWSServiceStub(
                    Configuration.INSTANCE.getFleetEndpoint());
            fleetWSServiceStub._getServiceClient().addHeader(
                    AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this
                            .getContext().getUser().getPassword()));
            this.car = fleetWSServiceStub.getCarDetails(this.licensePlate, null, null);
            this.date = new Date();
        }

        final CarWSServiceStub carWSServiceStub = new CarWSServiceStub(Configuration.INSTANCE.getCarEndpoint());
        carWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext()
                        .getUser().getPassword()));

        // Get the images if exists
        if (this.files != null) {
            this.files.removeAll(Collections.singleton(null));
            int fileCount = 0;
            this.imageFiles = new FileAttachmentDTO[this.files.size()];
            for (final FileBean file : this.files) {
                final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                final InputStream inputStream = file.getInputStream();
                final byte[] buffer = new byte[1024];
                int read;
                while ((read = inputStream.read(buffer)) >= 0) {
                    outputStream.write(buffer, 0, read);
                }
                final byte[] contentAsByteArray = outputStream.toByteArray();
                final ByteArrayDataSource rawData = new ByteArrayDataSource(contentAsByteArray);

                final DataHandler data = new DataHandler(rawData);

                final FileAttachmentDTO attFile = new FileAttachmentDTO();

                attFile.setFileName(this.licensePlate + "_" + fileCount + "." + file.getContentType().split("/")[1]);
                attFile.setMimeType(file.getContentType());

                attFile.setFileContents(data);

                attFile.setType(FileAttachmentTypeEnum.CAR_IMAGE);

                file.delete();
                this.imageFiles[fileCount++] = attFile;
            }
        }

        final String[] rows = this.rowCoord.split(",");
        final String[] cols = this.colCoord.split(",");
        final String[] incTypes = this.incidentType.split(",");

        for (int i = 0; i < rows.length; i++) {
            // Set the line coordinate
            if (Integer.parseInt(rows[i].trim()) >= 11) {
                rows[i] = this.lines_interior.get(Integer.parseInt(rows[i].trim()) - 11);
            } else {
                rows[i] = this.lines_exterior.get(Integer.parseInt(rows[i].trim()));
            }

            // Clear the cols string
            cols[i] = cols[i].trim();

            // Clear the incident type string
            incTypes[i] = incTypes[i].trim();
        }

        // Default Incident Code (Car Damage)
        this.incidentCode = "EXTERNAL_DAMAGE";

        final boolean result = carWSServiceStub.reportNewDamage(this.licensePlate, rows, cols,
                this.getDate().getTime(), this.imageFiles, this.description, this.getContext().getUser().getUsername(),
                this.incidentCode, incTypes);

        LOGGER.info("The user " + this.description, this.getContext().getUser().getUsername() + " is reporting "
                + rows.length + " damages on veichle " + this.licensePlate);

        if (result) {
            this.getContext().getMessages().add(new LocalizableMessage("damage.report.save.success"));
            this.getContext().getResponse().setHeader("licensePlate", this.licensePlate);
        } else {
            this.getContext().getMessages().add(new LocalizableMessage("damage.report.save.error"));

        }
        return this.showPin();
    }

    /**
     * Cancel Damage Report
     *
     * @return the page resolution
     * @throws UnsupportedEncodingException
     * @throws RemoteException
     * @throws com.criticalsoftware.mobics.proxy.car.CustomerNotFoundExceptionException
     * @throws com.criticalsoftware.mobics.proxy.car.CarLicensePlateNotFoundExceptionException
     * @throws CustomerNotFoundExceptionException
     * @throws CarLicensePlateNotFoundExceptionException
     * @throws com.criticalsoftware.mobics.proxy.fleet.CarLicensePlateNotFoundExceptionException
     * @throws CarNotFoundExceptionException
     */
    public Resolution continueToTrip() throws UnsupportedEncodingException, RemoteException,
    com.criticalsoftware.mobics.proxy.car.CustomerNotFoundExceptionException,
    com.criticalsoftware.mobics.proxy.car.CarLicensePlateNotFoundExceptionException,
    CustomerNotFoundExceptionException, CarLicensePlateNotFoundExceptionException,
    com.criticalsoftware.mobics.proxy.fleet.CarLicensePlateNotFoundExceptionException,
    CarNotFoundExceptionException {
        return this.showPin();
    }

    /**
     * @param errors
     */
    @ValidationMethod(on = "submitDamageReport", when = ValidationState.NO_ERRORS)
    public void validate(final ValidationErrors errors) {

        if ((this.licensePlate == null) || (this.rowCoord == null) || (this.colCoord == null)
                || (this.getContext().getUser() == null) || (this.incidentCode == null) || (this.incidentType == null)) {
            errors.addGlobalError(new LocalizableError("damage.report.no.results.found"));
        }

        if (this.date == null) {
            this.date = new Date();
        } else if (this.date.after(new Date())) {
            errors.addGlobalError(new LocalizableError("damage.report.date.not.allowed"));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resolution main() {
        LOGGER.debug("MAIN");
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resolution data() throws Exception {
        return new RedirectResolution(DamageReportActionBean.class).flash(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resolution saveData() throws Exception {
        LOGGER.debug("SAVE DATA");
        return null;
    }

    /**
     * Validation method for PIN
     *
     * @param errors
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     */
    @ValidationMethod(when = ValidationState.NO_ERRORS, on = "data")
    public Resolution validation() throws RemoteException, UnsupportedEncodingException {

        final CustomerWSServiceStub customerWSServiceStub = new CustomerWSServiceStub(
                Configuration.INSTANCE.getCustomerEndpoint());
        customerWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext()
                        .getUser().getPassword()));
        if (!customerWSServiceStub.isValidCustomerPin(this.pin.toString())) {
            this.getContext().getValidationErrors()
            .addGlobalError(new LocalizableError("account.security.check.pin.invalid"));

        } else {

            final CarWSServiceStub carWSServiceStub = new CarWSServiceStub(Configuration.INSTANCE.getCarEndpoint());
            carWSServiceStub._getServiceClient().addHeader(
                    AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this
                            .getContext().getUser().getPassword()));
            try {
                carWSServiceStub.imobilizerOff(this.licensePlate);
                this.getContext().getMessages().add(new LocalizableMessage("trip.detail.unlock_sucess"));
            } catch (final com.criticalsoftware.mobics.proxy.car.CustomerNotFoundExceptionException e) {
                LOGGER.error("Customer Not Found");
            } catch (final com.criticalsoftware.mobics.proxy.car.CarLicensePlateNotFoundExceptionException e) {
                LOGGER.error("Car License Plate Not Found");
            }
            return new ForwardResolution("/WEB-INF/trip/currentTrip.jsp");
            // return new RedirectResolution(TripActionBean.class);
        }
        return new RedirectResolution(DamageReportActionBean.class).flash(this);
    }

    public Resolution getCarState() throws UnsupportedEncodingException, RemoteException,
    BookingNotFoundExceptionException, CustomerNotFoundExceptionException,
    com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException {

        final BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext()
                        .getUser().getPassword()));
        this.getContext().getResponse().setHeader("Stripes-Success", "OK");

        final CurrentTripDTO currentTripDTO = bookingWSServiceStub.getCurrentTripDetails();

        return new JavaScriptResolution(currentTripDTO.getCarState());
    }

    /**
     * Show PIN resolution
     *
     * @return the pin page resolution
     * @throws com.criticalsoftware.mobics.proxy.fleet.CarLicensePlateNotFoundExceptionException
     * @throws com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException
     * @throws CarLicensePlateNotFoundExceptionException
     * @throws CustomerNotFoundExceptionException
     * @throws UnsupportedEncodingException
     * @throws RemoteException
     * @throws CarNotFoundExceptionException
     * @throws com.criticalsoftware.mobics.proxy.car.CarLicensePlateNotFoundExceptionException
     */
    public Resolution showPin() throws RemoteException, UnsupportedEncodingException,
    CustomerNotFoundExceptionException, CarLicensePlateNotFoundExceptionException,
    com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException,
    com.criticalsoftware.mobics.proxy.fleet.CarLicensePlateNotFoundExceptionException,
    CarNotFoundExceptionException,
    com.criticalsoftware.mobics.proxy.car.CarLicensePlateNotFoundExceptionException {
        this.getCurrent();
        return new ForwardResolution("/WEB-INF/trip/pinUnlockCar.jsp");
    }

    /**
     * @return the licensePlate
     */
    public final String getLicensePlate() {
        return this.licensePlate;
    }

    /**
     * @param licensePlate the licensePlate to set
     */
    public final void setLicensePlate(final String licensePlate) {
        this.licensePlate = licensePlate;
    }

    /**
     * @return the date
     */
    public final Date getDate() {
        return this.date;
    }

    /**
     * @param date the date to set
     */
    public final void setDate(final Date date) {
        this.date = date;
    }

    /**
     * @return the description
     */
    public final String getDescription() {
        return this.description;
    }

    /**
     * @param description the description to set
     */
    public final void setDescription(final String description) {
        this.description = description;
    }

    /**
     * @return the incidentCode
     */
    public final String getIncidentCode() {
        return this.incidentCode;
    }

    /**
     * @param incidentCode the incidentCode to set
     */
    public final void setIncidentCode(final String incidentCode) {
        this.incidentCode = incidentCode;
    }

    /**
     * @return the rowCoord
     */
    public final String getRowCoord() {
        return this.rowCoord;
    }

    /**
     * @param rowCoord the rowCoord to set
     */
    public final void setRowCoord(final String rowCoord) {
        this.rowCoord = rowCoord;
    }

    /**
     * @return the colCoord
     */
    public final String getColCoord() {
        return this.colCoord;
    }

    /**
     * @param colCoord the colCoord to set
     */
    public final void setColCoord(final String colCoord) {
        this.colCoord = colCoord;
    }

    /**
     * @return the car
     */
    public final CarDTO getCar() {
        return this.car;
    }

    /**
     * @param car the car to set
     */
    public final void setCar(final CarDTO car) {
        this.car = car;
    }

    /**
     * @return the incidentType
     */
    public final String getIncidentType() {
        return this.incidentType;
    }

    /**
     * @param incidentType the incidentType to set
     */
    public final void setIncidentType(final String incidentType) {
        this.incidentType = incidentType;
    }

    /**
     * @return the files
     */
    public List<FileBean> getFiles() {
        return this.files;
    }

    /**
     * @param files the files to set
     */
    public void setFiles(final List<FileBean> files) {
        this.files = files;
    }

    /**
     * @return the imageFiles
     */
    public final FileAttachmentDTO[] getImageFiles() {
        return this.imageFiles;
    }

    /**
     * @param imageFiles the imageFiles to set
     */
    public final void setImageFiles(final FileAttachmentDTO[] imageFiles) {
        this.imageFiles = imageFiles;
    }

    /**
     * @return the carDamages
     */
    public CarDamageDetailsListDTO[] getCarDamages() {
        return this.carDamages;
    }

    /**
     * @param carDamages the carDamages to set
     */
    public void setCarDamages(final CarDamageDetailsListDTO[] carDamages) {
        this.carDamages = carDamages;
    }

    /**
     * @return the pin
     */
    @Override
    public Integer getPin() {
        return this.pin;
    }

    /**
     * @param pin the pin to set
     */
    @Override
    public void setPin(final Integer pin) {
        this.pin = pin;
    }

}