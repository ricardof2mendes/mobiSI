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
import java.math.BigDecimal;
import java.rmi.RemoteException;

import javax.activation.DataHandler;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;

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
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class LicensePlateActionBean extends CarDetailsActionBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(LicensePlateActionBean.class);

    private CarDTO[] cars;

    @DefaultHandler
    @DontValidate
    public Resolution main() {
        return new ForwardResolution("/WEB-INF/book/licensePlate.jsp");
    }

    public Resolution licensePlateBook() throws RemoteException, CarValidationExceptionException {
        car = new FleetWSServiceStub(Configuration.FLEET_ENDPOINT).getCarsByLicensePlate(licensePlate.toUpperCase(),
                CarType.NORMAL.name(), new BigDecimal(latitude), new BigDecimal(longitude))[0];

        return new ForwardResolution("/WEB-INF/book/licensePlateBook.jsp");
    }

    public Resolution autocomplete() throws RemoteException {
        if (q != null) {
            try {
                cars = new FleetWSServiceStub(Configuration.FLEET_ENDPOINT).getCarsByLicensePlate(q.toUpperCase(),
                        CarType.NORMAL.name(), new BigDecimal(latitude), new BigDecimal(longitude));
                getContext().getResponse().setHeader("Stripes-Success", "OK");
            } catch (CarValidationExceptionException e) {
                LOGGER.error("Car validation exception.", e);
            }
        }
        return new ForwardResolution("/WEB-INF/book/carList.jsp");
    }

    public Resolution getCarImage() throws RemoteException, IOException, IOExceptionException {
        DataHandler handler = new FleetWSServiceStub(Configuration.FLEET_ENDPOINT)
                .getCarThumbnail(licensePlate, 58, 58);
        return new StreamingResolution(handler.getContentType(), handler.getInputStream());

    }

    /**
     * @return the cars
     */
    public CarDTO[] getCars() {
        return cars;
    }
}
