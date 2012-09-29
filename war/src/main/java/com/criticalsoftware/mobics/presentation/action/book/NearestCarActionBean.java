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
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.presentation.util.GeolocationUtil;
import com.criticalsoftware.mobics.presentation.util.OrderBy;
import com.criticalsoftware.mobics.proxy.fleet.CarClassNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.fleet.CarValidationExceptionException;
import com.criticalsoftware.mobics.proxy.fleet.FleetWSServiceStub;
import com.criticalsoftware.mobics.proxy.fleet.FuelTypeNotFoundExceptionException;

/**
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class NearestCarActionBean extends BaseActionBean {

    private static final int MAX_RESULTS = 1;

    @Validate
    private String latitude;

    @Validate
    private String longitude;

    private CarDTO car;

    private String location;

    private FleetWSServiceStub fleet;

    @DefaultHandler
    public Resolution main() throws RemoteException {

        fleet = new FleetWSServiceStub(Configuration.FLEET_ENDPOINT);
        try {
            // Get the first car
            car = fleet.searchCars(null, null, "", "", OrderBy.DISTANCE.name(), MAX_RESULTS, new BigDecimal(latitude),
                    new BigDecimal(longitude))[0];
            // Get the location
            location = GeolocationUtil.getAddressFromCoordinates(car.getLatitude().toString(), car.getLongitude()
                    .toString());

        } catch (CarValidationExceptionException e) {
            // TODO ltiago
            e.printStackTrace();
        } catch (FuelTypeNotFoundExceptionException e) {
            // TODO ltiago: Auto-generated catch block. This code MUST be changed to the appropriate statements in order
            // to handle the exception.
            e.printStackTrace();
        } catch (CarClassNotFoundExceptionException e) {
            // TODO ltiago: Auto-generated catch block. This code MUST be changed to the appropriate statements in order
            // to handle the exception.
            e.printStackTrace();
        }

        return new ForwardResolution("/WEB-INF/book/nearestCarDetails.jsp");
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
     * @return the car
     */
    public CarDTO getCar() {
        return car;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

}
