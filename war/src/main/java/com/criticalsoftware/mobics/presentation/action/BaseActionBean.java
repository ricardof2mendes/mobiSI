/*
 * $Id: BaseActionBean.java,v 1.4 2012/05/04 14:35:36 ssantos Exp $
 *
 * Last changed on $Date: 2012/05/04 14:35:36 $
 * Last changed by $Author: ssantos $
 */
package com.criticalsoftware.mobics.presentation.action;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.activation.DataHandler;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.StrictBinding;

import org.apache.axis2.AxisFault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.criticalsoftware.mobics.carclub.CarClubSimpleDTO;
import com.criticalsoftware.mobics.miscellaneous.CarClassDTO;
import com.criticalsoftware.mobics.presentation.extension.MobiCSActionBeanContext;
import com.criticalsoftware.mobics.presentation.security.CarClubSimple;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.proxy.carclub.CarClubCodeNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.carclub.CarClubWSService;
import com.criticalsoftware.mobics.proxy.carclub.CarClubWSServiceStub;
import com.criticalsoftware.mobics.proxy.carclub.CarClubWebSiteURLNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.miscellaneous.MiscellaneousWSServiceStub;

/**
 * Base ActionBean class.
 * 
 * @author Samuel Santos
 * @version $Revision: 1.4 $
 */
@StrictBinding
public abstract class BaseActionBean implements ActionBean {

    private static final Logger LOG = LoggerFactory.getLogger(BaseActionBean.class);

    private static final String SECURE_PROTOCOL = "https://";

    private static final String NONSECURE_PROTOCOL = "http://";

    private static final int[] LOGO_DIMENSIONS = { 250, 60 };

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
        StringBuilder builder = new StringBuilder(getContext().getRequest().isSecure() ? SECURE_PROTOCOL
                : NONSECURE_PROTOCOL).append(getContext().getRequest().getServerName()).append(":")
                .append(getContext().getRequest().getServerPort()).append(getContext().getRequest().getContextPath());
        try {
            carClubSimpleDTO = new CarClubWSServiceStub(Configuration.INSTANCE.getCarClubEndpoint())
                    .getCarClubSimpleByURL(builder.toString());
            if (getContext().getCarClub() == null) {
                getContext().setCarClub(
                        new CarClubSimple(carClubSimpleDTO.getCarClubName(), carClubSimpleDTO.getCarClubContactPhone(),
                                carClubSimpleDTO.getCarClubContactEmail()));
            }

        } catch (Exception e) {
            LOG.warn("Could not obtain car club theme style based on supplied url: {}", builder.toString());
        }

        String style = new StringBuilder((carClubSimpleDTO != null ? carClubSimpleDTO.getCarClubColorScheme()
                : Configuration.INSTANCE.getDefaultThemeStyle()).replaceAll("_", ""))
                .append(" ")
                .append((carClubSimpleDTO != null ? carClubSimpleDTO.getCarClubTheme() : Configuration.INSTANCE
                        .getDefaultThemeColor()).replaceAll("_", "")).toString();

        if (style.indexOf(Configuration.INSTANCE.getDefaultThemeWarmWord()) > 0) {
            style.replaceAll(" ", "");
        }

        return style.toLowerCase();
    }

    public final Resolution getSplashScreenImage() throws IOException {
        CarClubSimpleDTO carClubSimpleDTO = null;
        DataHandler handler = null;
        Resolution resolution = null;

        StringBuilder builder = new StringBuilder(getContext().getRequest().isSecure() ? SECURE_PROTOCOL
                : NONSECURE_PROTOCOL).append(getContext().getRequest().getServerName()).append(":")
                .append(getContext().getRequest().getServerPort()).append(getContext().getRequest().getContextPath());
        try {
            CarClubWSService carClubService = new CarClubWSServiceStub(Configuration.INSTANCE.getCarClubEndpoint());
            carClubSimpleDTO = carClubService.getCarClubSimpleByURL(builder.toString());

            if (getContext().getCarClub() == null) {
                getContext().setCarClub(
                        new CarClubSimple(carClubSimpleDTO.getCarClubName(), carClubSimpleDTO.getCarClubContactPhone(),
                                carClubSimpleDTO.getCarClubContactEmail()));
            }

            handler = carClubService.getCarClubThumbnailByCarClubCode(carClubSimpleDTO.getCarClubCode(),
                    LOGO_DIMENSIONS[0], LOGO_DIMENSIONS[1]);
            resolution = new StreamingResolution(handler.getContentType(), handler.getInputStream());
        } catch (Exception e) {
            LOG.warn("Could not obtain car club image based on supplied url: {}", builder.toString());
        }

        return resolution;
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
        return header.replaceAll("_", "").toLowerCase();
    }

    /**
     * @return the carClasses
     */
    public CarClassDTO[] getCarClasses() throws RemoteException {
        return new MiscellaneousWSServiceStub(Configuration.INSTANCE.getMiscellaneousEnpoint())
                .getAllCarClasses(getContext().getLocale().getCountry());
    }

    /**
     * Get the contacts image style
     * 
     * @return
     * @throws CarClubCodeNotFoundExceptionException
     * @throws CarClubWebSiteURLNotFoundExceptionException
     * @throws RemoteException
     * @throws AxisFault
     */
    public final String getContactImageStyle() throws AxisFault, RemoteException {
        return getSplashScreenStyle().replaceAll(" ", "");
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
