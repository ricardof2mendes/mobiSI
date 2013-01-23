/*
 * $Id: HomeActionBean.java,v 1.3 2012/05/04 14:35:36 ssantos Exp $
 *
 * Last changed on $Date: 2012/05/04 14:35:36 $
 * Last changed by $Author: ssantos $
 */
package com.criticalsoftware.mobics.presentation.action;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import com.criticalsoftware.mobics.miscellaneous.CarClassDTO;
import com.criticalsoftware.mobics.presentation.action.booking.AdvanceBookingActionBean;
import com.criticalsoftware.mobics.presentation.action.booking.CreateBookingInterestActionBean;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.proxy.miscellaneous.MiscellaneousWSServiceStub;

/**
 * Home action bean.
 * 
 * @author Samuel Santos
 * @version $Revision: 1.3 $
 */
@MobiCSSecure
public class HomeActionBean extends BaseActionBean {

    private static final String ACTIVE_MENU = "booking";

    @DefaultHandler
    @DontValidate
    public Resolution main() {
        return new ForwardResolution("/WEB-INF/home.jsp");
    }

    @DontValidate
    public Resolution bookCarForNow() {
        // added parameter distance to request, fill the form with user preferences
        return new ForwardResolution("/WEB-INF/book/bookCarForNow.jsp").addParameter("distance", new BigDecimal(
                getContext().getUser().getCustomerPreferencesDTO().getSearchRadius()));
    }

    @DontValidate
    public Resolution findCarForLater() {
        return new ForwardResolution(CreateBookingInterestActionBean.class);
    }

    @DontValidate
    public Resolution bookInAdvance() {
        return new ForwardResolution(AdvanceBookingActionBean.class);
    }

    @Override
    public String getActiveMenu() {
        return ACTIVE_MENU;
    }
    
    /**
     * @return the carClasses
     */
    public CarClassDTO[] getCarClasses() throws RemoteException {
        return new MiscellaneousWSServiceStub(Configuration.INSTANCE.getMiscellaneousEnpoint()).getAllCarClasses();
    }
}
