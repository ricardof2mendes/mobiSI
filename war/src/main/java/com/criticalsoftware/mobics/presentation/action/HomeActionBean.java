/*
 * $Id: HomeActionBean.java,v 1.3 2012/05/04 14:35:36 ssantos Exp $
 *
 * Last changed on $Date: 2012/05/04 14:35:36 $
 * Last changed by $Author: ssantos $
 */
package com.criticalsoftware.mobics.presentation.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import com.criticalsoftware.mobics.presentation.action.booking.AdvanceBookingActionBean;
import com.criticalsoftware.mobics.presentation.action.booking.FindCarForLaterActionBean;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;

/**
 * Home action bean.
 * 
 * @author Samuel Santos
 * @version $Revision: 1.3 $
 */
@MobiCSSecure
public class HomeActionBean extends BaseActionBean {

    @DefaultHandler
    @DontValidate
    public Resolution main() {
        return new ForwardResolution("/WEB-INF/home.jsp");
    }

    @DontValidate
    public Resolution bookCarForNow() {
        return new ForwardResolution("/WEB-INF/book/bookCarForNow.jsp");
    }

    @DontValidate
    public Resolution findCarForLater() {
        return new ForwardResolution(FindCarForLaterActionBean.class);
    }

    @DontValidate
    public Resolution bookInAdvance() {
        return new ForwardResolution(AdvanceBookingActionBean.class);
    }
    
    @Override
    public String getActiveMenu(){
        return "booking";
    }
}
