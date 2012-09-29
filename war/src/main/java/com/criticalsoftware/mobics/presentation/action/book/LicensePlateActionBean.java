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

import org.apache.axis2.AxisFault;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.validation.Validate;

import com.criticalsoftware.mobics.fleet.CarDTO;
import com.criticalsoftware.mobics.presentation.action.BaseActionBean;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.proxy.fleet.FleetWSServiceStub;

/**
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class LicensePlateActionBean extends BaseActionBean {

    private CarDTO[] cars;

    @Validate
    private String licensePlate;

    @Validate
    private String q;

    private CarDTO car;

    @DefaultHandler
    public Resolution main() {
        return new ForwardResolution("/WEB-INF/book/licensePlate.jsp");
    }

    public Resolution carDetails() throws RemoteException {
        car = new FleetWSServiceStub(Configuration.FLEET_ENDPOINT).getCarsByLicensePlate(licensePlate.toUpperCase())[0];
        return new ForwardResolution("/WEB-INF/book/licensePlateDetails.jsp");
    }

    public Resolution autocomplete() throws RemoteException {
        StringBuilder results = new StringBuilder();
        q = q.toUpperCase();
        if (q != null) {
            cars = new FleetWSServiceStub(Configuration.FLEET_ENDPOINT).getCarsByLicensePlate(q);
            System.out.println(cars);
            if (cars != null) {
                for (int i = 0; i < cars.length; i++) {
                    if (cars[i].getLicensePlate().indexOf(q) != -1) {
                        results.append(cars[i].getLicensePlate()).append("\n");
                    }
                }
            }
        }
        System.out.println(results.toString());
        return new StreamingResolution("text/plain", results.toString());
    }

    /**
     * @return the car
     */
    public CarDTO getCar() {
        return car;
    }

    /**
     * @param licensePlate the licensePlate to set
     */
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    /**
     * @param q the q to set
     */
    public void setQ(String q) {
        this.q = q;
    }

}
