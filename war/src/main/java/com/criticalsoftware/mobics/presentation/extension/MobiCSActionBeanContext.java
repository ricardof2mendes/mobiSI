/*
 * $Id: FrameworkActionBeanContext.java,v 1.2 2012/05/04 14:35:36 ssantos Exp $
 *
 * Last changed on $Date: 2012/05/04 14:35:36 $
 * Last changed by $Author: ssantos $
 */
package com.criticalsoftware.mobics.presentation.extension;

import net.sourceforge.stripes.action.ActionBeanContext;

import com.criticalsoftware.mobics.presentation.security.CarClubSimple;
import com.criticalsoftware.mobics.presentation.security.User;

/**
 * ActionBeanContext subclass for the application that manages where values like the logged in user are stored.
 * 
 * @author Samuel Santos
 * @version $Revision: 1.2 $
 */
public class MobiCSActionBeanContext extends ActionBeanContext {

    /** Gets the currently logged in user, or null if no-one is logged in. */
    public User getUser() {
        return (User) getRequest().getSession().getAttribute("user");
    }

    /** Sets the currently logged in user. */
    public void setUser(User currentUser) {
        getRequest().getSession().setAttribute("user", currentUser);
    }

    /** Logs the user out by invalidating the session. */
    public void logout() {
        getRequest().getSession().invalidate();
    }

    /** Sets the retina display. */
    public void setRetina(boolean retina) {
        getRequest().getSession().setAttribute("retina", retina);
    }

    /** Gets the retina display. */
    public boolean getRetina() {
        return (Boolean) getRequest().getSession().getAttribute("retina");
    }

    /** Sets the car club simple info. */
    public void setCarClub(CarClubSimple carClubSimple) {
        getRequest().getSession().setAttribute("carClub", carClubSimple);
    }

    public CarClubSimple getCarClub() {
        return (CarClubSimple) getRequest().getSession().getAttribute("carClub");
    }
}
