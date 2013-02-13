/*
 * $Id: LogoutActionBean.java,v 1.4 2012/05/04 14:35:36  Exp $
 *
 * Last changed on $Date: 2012/05/04 14:35:36 $
 * Last changed by $Author: ssantos $
 */
package com.criticalsoftware.mobics.presentation.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

/**
 * Sign-out action bean.
 * 
 * @author Samuel Santos
 * @version $Revision: 1.4 $
 */
public class LogoutActionBean extends BaseActionBean {

    /**
     * Logout resolution
     * 
     * @return resolution to login page
     */
    @DefaultHandler
    public Resolution logout() {
        getContext().logout();
        return new RedirectResolution(LoginActionBean.class);
    }
}
