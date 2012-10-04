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

import java.rmi.RemoteException;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.ajax.JavaScriptResolution;
import net.sourceforge.stripes.validation.Validate;

import com.criticalsoftware.mobics.fleet.CarDTO;
import com.criticalsoftware.mobics.fleet.CoordinateDTO;
import com.criticalsoftware.mobics.fleet.ZoneWithPolygonDTO;
import com.criticalsoftware.mobics.presentation.action.BaseActionBean;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.presentation.util.CoordinateZonesDTO;
import com.criticalsoftware.mobics.presentation.util.GeolocationUtil;
import com.criticalsoftware.mobics.proxy.fleet.CarLicensePlateNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.fleet.FleetWSServiceStub;

/**
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class CarDetailsActionBean extends BaseActionBean {
    
    @Validate(required = true, on = {"carLocation", "licensePlateBook"})
    protected String licensePlate;
    
    @Validate(required=true, on="getData")
    protected String q;
    
    @Validate(required = true, on = {"nearestCarBook", "licensePlateBook"})
    protected String latitude;

    @Validate(required = true, on = {"nearestCarBook", "licensePlateBook"})
    protected String longitude;
    
    protected CarDTO car;
    
    
    public Resolution ajax() throws RemoteException {

            // Get the first car
        FleetWSServiceStub fleet = new FleetWSServiceStub(Configuration.FLEET_ENDPOINT);
        CoordinateDTO coordinate = null;
        ZoneWithPolygonDTO[] zones = null;
        try {
            coordinate = fleet.getCarCoordinatesByLicensePlate(q);
//            zones = fleet.getCarZonesWithPolygons(q);
//        } catch (CarNotFoundExceptionException e) {
//            // TODO ltiago
//            e.printStackTrace();
        } catch (CarLicensePlateNotFoundExceptionException e) {
            // TODO ltiago
            e.printStackTrace();
        }

        return new JavaScriptResolution(new CoordinateZonesDTO(coordinate, zones));
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
    
}
