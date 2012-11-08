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
package com.criticalsoftware.mobics.presentation.action.booking;

import java.rmi.RemoteException;
import java.util.Date;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import com.criticalsoftware.mobics.presentation.action.BaseActionBean;
import com.criticalsoftware.mobics.presentation.extension.DatetimeTypeConverter;

/**
 * @author ltiago
 * @version $Revision: $
 */
public class FindCarForLaterActionBean extends BaseActionBean {

    @Validate(required = true, on = "createBookingInterest", converter = DatetimeTypeConverter.class)
    private Date startDate;

    @Validate(required = true, on = "createBookingInterest")
    private String mapLocation;

    @Validate(required = true, on = "createBookingInterest")
    private Integer distance;

    @Validate(required = true, on = "createBookingInterest")
    private String carClazz;

    @Validate(required = true, on = "createBookingInterest")
    private Boolean fromMyCarClub;

    @Validate(required = true, on = "createBookingInterest")
    private Integer startSending;

    @Validate(required = true, on = "createBookingInterest")
    private Integer stopSending;

    @Validate(required = true, on = "createBookingInterest")
    private Integer maxMessages;

    @DontValidate
    @DefaultHandler
    public Resolution main() throws RemoteException {
        return new ForwardResolution("/WEB-INF/book/findCarForLater.jsp");
    }

    public Resolution createBookingInterest() throws RemoteException {

        return new ForwardResolution("/WEB-INF/book/findCarForLater.jsp");
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the mapLocation
     */
    public String getMapLocation() {
        return mapLocation;
    }

    /**
     * @param mapLocation the mapLocation to set
     */
    public void setMapLocation(String location) {
        this.mapLocation = location;
    }

    /**
     * @return the distance
     */
    public Integer getDistance() {
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    /**
     * @return the carClazz
     */
    public String getCarClazz() {
        return carClazz;
    }

    /**
     * @param carClazz the carClazz to set
     */
    public void setCarClazz(String carClazz) {
        this.carClazz = carClazz;
    }

    /**
     * @return the fromMyCarClub
     */
    public Boolean getFromMyCarClub() {
        return fromMyCarClub;
    }

    /**
     * @param fromMyCarClub the fromMyCarClub to set
     */
    public void setFromMyCarClub(Boolean fromMyCarClub) {
        this.fromMyCarClub = fromMyCarClub;
    }

    /**
     * @return the startSending
     */
    public Integer getStartSending() {
        return startSending;
    }

    /**
     * @param startSending the startSending to set
     */
    public void setStartSending(Integer startSending) {
        this.startSending = startSending;
    }

    /**
     * @return the stopSending
     */
    public Integer getStopSending() {
        return stopSending;
    }

    /**
     * @param stopSending the stopSending to set
     */
    public void setStopSending(Integer stopSending) {
        this.stopSending = stopSending;
    }

    /**
     * @return the maxMessages
     */
    public Integer getMaxMessages() {
        return maxMessages;
    }

    /**
     * @param maxMessages the maxMessages to set
     */
    public void setMaxMessages(Integer maxMessages) {
        this.maxMessages = maxMessages;
    }
}
