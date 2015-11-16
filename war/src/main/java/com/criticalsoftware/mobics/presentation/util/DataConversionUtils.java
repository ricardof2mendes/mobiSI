/*
 * $Id$
 *
 * Copyright (c) Critical Software S.A., All Rights Reserved.
 * (www.criticalsoftware.com)
 *
 * This software is the proprietary information of Critical Software S.A.
 * Use is subject to license terms.
 *
 * Last changed on: $Date$
 * Last changed by: $Author$
 */
package com.criticalsoftware.mobics.presentation.util;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.criticalsoftware.mobics.presentation.extension.MobiCSActionBeanContext;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.proxy.carclub.CarClubWSServiceStub;

/**
 * @author embarros
 * @version $Revision$
 */
@SuppressWarnings(value = { "all" })
public class DataConversionUtils {


    MobiCSActionBeanContext context;

    /**
     *
     */
    public DataConversionUtils(final MobiCSActionBeanContext context) {
        this.context = context;
    }



    private  com.criticalsoftware.www.mobios.country.configuration.CountryConfiguration getCountryConfiguration() throws UnsupportedEncodingException, RemoteException {
        CarClubWSServiceStub carClubWSServiceStub = new CarClubWSServiceStub(
                Configuration.INSTANCE.getCarClubEndpoint());

        carClubWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));

        return carClubWSServiceStub.getCountryConfigurationByCarClubCode(getContext().getCarClub().getCode());

    }

    private com.criticalsoftware.www.mobios.country.configuration.CountryConfiguration getCountryConfiguration(final String carClubCode) throws UnsupportedEncodingException, RemoteException {
        CarClubWSServiceStub carClubWSServiceStub = new CarClubWSServiceStub(
                Configuration.INSTANCE.getCarClubEndpoint());

        carClubWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));

        return carClubWSServiceStub.getCountryConfigurationByCarClubCode(getContext().getCarClub().getCode());

    }

    /*
     * toUTC = true -> convert to UTC
     * toUTC = false -> convert from UTC
     * carClubCode != null -> uses principal to get carClubCode
     * carClubCode == null -> uses the carClubCode passed in parameter
     * */
    private Date convertUTC(Date date, boolean toUTC, String carClubCode) throws UnsupportedEncodingException, RemoteException {
        Date originalDate = date;
        Date dateConverted = originalDate;

        TimeZone timeZone;
        if (carClubCode != null) {
            timeZone = getCountryTimeZone(carClubCode);
        } else {
            timeZone = getCountryTimeZone();
        }

        if (date != null && timeZone != null) {
            // Get offset of the Car Club.
            int offsetOfTimezome = timeZone.getOffset(originalDate.getTime());

            // Subtract/add the offset to the date on control
            Calendar aux = Calendar.getInstance();
            aux.setTimeInMillis(originalDate.getTime());
            if (toUTC) {
                aux.add(Calendar.MILLISECOND, -offsetOfTimezome);
            } else {
                aux.add(Calendar.MILLISECOND, offsetOfTimezome);
            }

            // Set the converted time
            dateConverted = new Date(aux.getTimeInMillis());
        }
        return dateConverted;
    }

    /**
     * Gets the country TimeZone.
     *
     * @return The country TimeZone.
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     */
    public  TimeZone getCountryTimeZone() throws UnsupportedEncodingException, RemoteException {
        return TimeZone.getTimeZone(getCountryConfiguration().getTimeZone());
    }

    public  TimeZone getCountryTimeZone(String carClubCode) throws UnsupportedEncodingException, RemoteException {
        return TimeZone.getTimeZone(getCountryConfiguration(carClubCode).getTimeZone());
    }

    public  Date toUTC(Date date, String carClubCode) throws UnsupportedEncodingException, RemoteException {
        return convertUTC(date, true, carClubCode);
    }

    public  Date fromUTC(Date date, String carClubCode) throws UnsupportedEncodingException, RemoteException {
        return convertUTC(date, false, carClubCode);
    }

    public  Date toUTC(Date date) throws UnsupportedEncodingException, RemoteException {
        return toUTC(date, null);
    }

    public  Date fromUTC(Date date) throws UnsupportedEncodingException, RemoteException {
        return fromUTC(date, null);
    }



    /**
     * @return the context
     */
    public  MobiCSActionBeanContext getContext() {
        return context;
    }



    /**
     * @param context the context to set
     */
    public  void setContext(MobiCSActionBeanContext context) {
        context = context;
    }





}
