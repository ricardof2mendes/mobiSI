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

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import com.criticalsoftware.mobics.fleet.CarDTO;
import com.criticalsoftware.mobics.presentation.action.BaseActionBean;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.CarType;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.presentation.util.GeolocationUtil;
import com.criticalsoftware.mobics.presentation.util.OrderBy;
import com.criticalsoftware.mobics.proxy.fleet.CarClassNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.fleet.CarTypeNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.fleet.CarValidationExceptionException;
import com.criticalsoftware.mobics.proxy.fleet.FleetWSServiceStub;
import com.criticalsoftware.mobics.proxy.fleet.FuelTypeNotFoundExceptionException;

/**
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class NearestCarActionBean extends CarDetailsActionBean {

    private static final int MAX_RESULTS = 1;
    

    @DefaultHandler
    public Resolution nearestCarBook() throws RemoteException, FuelTypeNotFoundExceptionException,
            CarClassNotFoundExceptionException, CarTypeNotFoundExceptionException, CarValidationExceptionException {

        // Get the first car
        car = new FleetWSServiceStub(Configuration.FLEET_ENDPOINT).searchCars(null, null, "", "",
                OrderBy.DISTANCE.name(), MAX_RESULTS, new BigDecimal(latitude), new BigDecimal(longitude), CarType.NORMAL.name())[0];

        return new ForwardResolution("/WEB-INF/book/nearestCarBook.jsp");
    }

    public Resolution carLocation() {
        return new ForwardResolution("/WEB-INF/book/carLocation.jsp").addParameter("licensePlate", licensePlate);
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return GeolocationUtil.getAddressFromCoordinates(car.getLatitude().toString(), car.getLongitude().toString());
    }

}
