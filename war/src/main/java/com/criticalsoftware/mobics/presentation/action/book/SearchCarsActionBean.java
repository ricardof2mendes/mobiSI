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
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.validation.EnumeratedTypeConverter;
import net.sourceforge.stripes.validation.Validate;

import com.criticalsoftware.mobics.fleet.CarDTO;
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
 * Search cars action bean
 * 
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class SearchCarsActionBean extends CarDetailsActionBean {

    @Validate(required=true, on="searchCars")
    private BigDecimal price;

    @Validate(required=true, on="searchCars")
    private BigDecimal distance;

    @Validate(required=true, on="searchCars", converter = EnumeratedTypeConverter.class)
    private CarClazz clazz;
    
    @Validate(required=true, on="searchCars", converter = EnumeratedTypeConverter.class)
    private OrderBy orderBy;

    private CarDTO[] cars;

    /**
     * Account page
     * 
     * @return the page resolution
     */
    @DefaultHandler
    @DontValidate
    public Resolution searchCars() {
        return new ForwardResolution("/WEB-INF/book/searchCars.jsp");
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
    @After(on = "searchCars", stages = LifecycleStage.BindingAndValidation)
    public void fillData() throws RemoteException, FuelTypeNotFoundExceptionException,
            CarClassNotFoundExceptionException, CarTypeNotFoundExceptionException, CarValidationExceptionException {
        cars = new FleetWSServiceStub(Configuration.FLEET_ENDPOINT).searchCars(price, distance,
                 clazz.getClazz(), FuelType.NOT_SPECIFIED.getType(), orderBy.name(),
                Configuration.MAX_RESULTS, latitude, longitude, CarType.NORMAL.name());
    }
    
    /**
     * Get the action bean
     * @return
     */
    public String getActionBean() {
        return this.getActionBean();
    }

    /**
     * @return the orderBy
     */
    public OrderBy getOrderBy() {
        return orderBy;
    }

    /**
     * @param orderBy the orderBy to set
     */
    public void setOrderBy(OrderBy orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the distance
     */
    public BigDecimal getDistance() {
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

    /**
     * @return the clazz
     */
    public CarClazz getClazz() {
        return clazz;
    }

    /**
     * @param clazz the clazz to set
     */
    public void setClazz(CarClazz clazz) {
        this.clazz = clazz;
    }

    /**
     * @return the cars
     */
    public CarDTO[] getCars() {
        return cars;
    }

}
