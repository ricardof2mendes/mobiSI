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
package com.criticalsoftware.mobics.presentation.action.book;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.activation.DataHandler;

import net.sourceforge.stripes.action.After;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.controller.LifecycleStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.criticalsoftware.mobics.fleet.CarDTO;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.CarType;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.proxy.fleet.CarValidationExceptionException;
import com.criticalsoftware.mobics.proxy.fleet.FleetWSServiceStub;
import com.criticalsoftware.mobics.proxy.fleet.IOExceptionException;

/**
 * License plate action bean
 * 
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class LicensePlateActionBean extends CarDetailsActionBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(LicensePlateActionBean.class);

    private CarDTO[] cars;

    /**
     * Search cars by license plate
     * @return the page resolution
     */
    @DefaultHandler
    @DontValidate
    public Resolution licensePlateSearch() {
        return new ForwardResolution("/WEB-INF/book/licensePlate.jsp");
    }

    /**
     * Car booking
     * 
     * @return the page resolution
     */
    public Resolution licensePlateBook() {
        return new ForwardResolution("/WEB-INF/book/licensePlateBook.jsp");
    }
    
    /**
     * Fill data
     * @throws RemoteException
     * @throws CarValidationExceptionException
     */
    @After(on="licensePlateBook", stages = LifecycleStage.BindingAndValidation)
    public void fillData()  throws RemoteException, CarValidationExceptionException {
        car = new FleetWSServiceStub(Configuration.FLEET_ENDPOINT).getCarsByLicensePlate(licensePlate.toUpperCase(),
                CarType.NORMAL.name(), latitude, longitude)[0];
    }

    /**
     * Autocomplete list
     * 
     * @return the page resolution
     * @throws RemoteException a jax-b webservice exception
     */
    public Resolution autocomplete() throws RemoteException {
        if (q != null) {
            try {
                cars = new FleetWSServiceStub(Configuration.FLEET_ENDPOINT).getCarsByLicensePlate(q.toUpperCase(),
                        CarType.NORMAL.name(), latitude, longitude);
                getContext().getResponse().setHeader("Stripes-Success", "OK");
            } catch (CarValidationExceptionException e) {
                LOGGER.error("Car validation exception.", e);
            }
        }
        return new ForwardResolution("/WEB-INF/book/carList.jsp");
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
        DataHandler handler = new FleetWSServiceStub(Configuration.FLEET_ENDPOINT).getCarThumbnail(licensePlate,
                Configuration.CAR_THUMBNAIL_WITDH, Configuration.CAR_THUMBNAIL_HEIGHT);
        return new StreamingResolution(handler.getContentType(), handler.getInputStream());
    }

    /**
     * Get the action bean
     * @return
     */
    public String getActionBean() {
        return this.getClass().getName();
    }
    
    /**
     * @return the cars
     */
    public CarDTO[] getCars() {
        return cars;
    }
}
