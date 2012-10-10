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

import java.math.BigDecimal;
import java.rmi.RemoteException;

import net.sourceforge.stripes.action.After;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.LifecycleStage;

import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.CarClazz;
import com.criticalsoftware.mobics.presentation.util.CarType;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.presentation.util.FuelType;
import com.criticalsoftware.mobics.presentation.util.OrderBy;
import com.criticalsoftware.mobics.proxy.fleet.CarClassNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.fleet.CarTypeNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.fleet.CarValidationExceptionException;
import com.criticalsoftware.mobics.proxy.fleet.FleetWSServiceStub;
import com.criticalsoftware.mobics.proxy.fleet.FuelTypeNotFoundExceptionException;

/**
 * Nearest car action bean
 * 
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class NearestCarActionBean extends CarDetailsActionBean {

    /**
     * Nearest car
     * 
     * @return the booking page resolution of the nearest car
     */
    @DefaultHandler
    public Resolution nearestCarBook() {
        return new ForwardResolution("/WEB-INF/book/nearestCarBook.jsp");
    }

    /**
     * Fill data
     * 
     * @throws RemoteException
     * @throws FuelTypeNotFoundExceptionException
     * @throws CarClassNotFoundExceptionException
     * @throws CarTypeNotFoundExceptionException
     * @throws CarValidationExceptionException
     */
    @After(on = "nearestCarBook", stages = LifecycleStage.BindingAndValidation)
    public void fillData() throws RemoteException, FuelTypeNotFoundExceptionException,
            CarClassNotFoundExceptionException, CarTypeNotFoundExceptionException, CarValidationExceptionException {
        car = new FleetWSServiceStub(Configuration.FLEET_ENDPOINT).searchCars(null, null,
                CarClazz.NOT_SPECIFIED.getClazz(), FuelType.NOT_SPECIFIED.getType(), OrderBy.DISTANCE.name(),
                Configuration.MIN_RESULTS, latitude, longitude, CarType.NORMAL.name())[0];
    }

    /**
     * Car location on map
     * 
     * @return the car location page resolution
     */
    public Resolution carLocation() {
        return new ForwardResolution("/WEB-INF/book/carLocation.jsp").addParameter("licensePlate", licensePlate);
    }

}
