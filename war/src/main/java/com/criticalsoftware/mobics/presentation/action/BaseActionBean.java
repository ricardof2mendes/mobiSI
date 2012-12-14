/*
 * $Id: BaseActionBean.java,v 1.4 2012/05/04 14:35:36 ssantos Exp $
 *
 * Last changed on $Date: 2012/05/04 14:35:36 $
 * Last changed by $Author: ssantos $
 */
package com.criticalsoftware.mobics.presentation.action;

import java.rmi.RemoteException;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.StrictBinding;

import org.apache.axis2.AxisFault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.criticalsoftware.mobics.carclub.CarClubSimpleDTO;
import com.criticalsoftware.mobics.presentation.extension.MobiCSActionBeanContext;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.proxy.carclub.CarClubCodeNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.carclub.CarClubWSServiceStub;
import com.criticalsoftware.mobics.proxy.carclub.CarClubWebSiteURLNotFoundExceptionException;

/**
 * Base ActionBean class.
 * 
 * @author Samuel Santos
 * @version $Revision: 1.4 $
 */
@StrictBinding
public abstract class BaseActionBean implements ActionBean {

    private static final Logger LOG = LoggerFactory.getLogger(BaseActionBean.class);

    /**
     * The Stripes action bean context.
     */
    private MobiCSActionBeanContext context;

    /**
     * Gets the context.
     * 
     * @return the context
     */
    @Override
    public final MobiCSActionBeanContext getContext() {
        return context;
    }

    /**
     * Sets the context.
     * 
     * @param context the context to set
     */
    @Override
    public final void setContext(ActionBeanContext context) {
        this.context = (MobiCSActionBeanContext) context;
    }

    /**
     * Get the active menu
     * 
     * @return
     */
    public String getActiveMenu() {
        String name = getClass().getPackage().getName();
        return name.substring(name.lastIndexOf('.') + 1);
    }

    /**
     * Get the splash screen style
     * 
     * @return
     * @throws CarClubCodeNotFoundExceptionException
     * @throws CarClubWebSiteURLNotFoundExceptionException
     * @throws RemoteException
     * @throws AxisFault
     */
    public final String getSplashScreenStyle() throws AxisFault, RemoteException {
        CarClubSimpleDTO carClubSimpleDTO = null;
        StringBuilder builder = new StringBuilder(getContext().getRequest().isSecure() ? "https://"
                : "http://").append(getContext().getRequest().getServerName()).append(":")
                .append(getContext().getRequest().getServerPort()).append(getContext().getRequest().getContextPath());
        try {
            carClubSimpleDTO = new CarClubWSServiceStub(Configuration.INSTANCE.getCarClubEndpoint())
                    .getCarClubSimpleByURL(builder.toString());
        } catch (Exception e) {
            LOG.warn("Could not obtain theme style based on supplied url: {}", builder.toString());
        }

        String style = new StringBuilder((carClubSimpleDTO != null ? carClubSimpleDTO.getCarClubColorScheme()
                : Configuration.INSTANCE.getDefaultThemeStyle()).replaceAll(" ", ""))
                .append(" ")
                .append((carClubSimpleDTO != null ? carClubSimpleDTO.getCarClubTheme() : Configuration.INSTANCE
                        .getDefaultThemeColor()).replaceAll(" ", "")).toString();

        if (style.indexOf(Configuration.INSTANCE.getDefaultThemeWarmWord()) > 0) {
            style.replaceAll(" ", "");
        }

        return style.toLowerCase();
    }

    /**
     * Get the header style
     * 
     * @return
     */
    public final String getHeaderStyle() {
        String header = Configuration.INSTANCE.getDefaultThemeColor();
        if (getContext().getUser() != null) {
            header = getContext().getUser().getCarClub().getCarClubColorScheme();
        }
        return header.replaceAll(" ", "").toLowerCase();
    }

    /**
     * Check if action bean it has validation field errors
     * 
     * @return
     */
    public final boolean getFieldErrors() {
        return this.context.getValidationErrors().hasFieldErrors();
    }

}
