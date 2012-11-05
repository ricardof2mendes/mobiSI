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
package com.criticalsoftware.mobics.presentation.action.recent;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.Calendar;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import com.criticalsoftware.mobics.booking.CustomerActivityEnum;
import com.criticalsoftware.mobics.booking.CustomerActivityListDTO;
import com.criticalsoftware.mobics.presentation.action.BaseActionBean;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.proxy.booking.BookingWSServiceStub;
import com.criticalsoftware.mobics.proxy.booking.CustomerNotFoundExceptionException;

/**
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class RecentActivitiesActionBean extends BaseActionBean {

    private CustomerActivityListDTO[] recents;

    @Validate(required = true, on = "details")
    private String activityCode;

    @DefaultHandler
    @DontValidate
    public Resolution main() throws RemoteException, UnsupportedEncodingException, CustomerNotFoundExceptionException {

        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));
        Calendar begin = Calendar.getInstance();
        begin.set(Calendar.YEAR, 1900);

        recents = bookingWSServiceStub.getRecentActivities(begin, Calendar.getInstance(),
                CustomerActivityEnum.ALL.getValue());

        return new ForwardResolution("/WEB-INF/recent/recentActivities.jsp");
    }
    
    public Resolution details() {
        return new ForwardResolution("/WEB-INF/recent/activityDetails.jsp");
    }

    /**
     * @return the recents
     */
    public CustomerActivityListDTO[] getRecents() {
        return recents;
    }

}
