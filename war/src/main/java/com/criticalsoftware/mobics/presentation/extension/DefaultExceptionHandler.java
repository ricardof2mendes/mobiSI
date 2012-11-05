/*
 * $Id: DefaultExceptionHandler.java,v 1.3 2012/05/04 14:35:36 ssantos Exp $
 *
 * Last changed on $Date: 2012/05/04 14:35:36 $
 * Last changed by $Author: ssantos $
 */
package com.criticalsoftware.mobics.presentation.extension;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.StripesConstants;
import net.sourceforge.stripes.exception.AutoExceptionHandler;
import net.sourceforge.stripes.exception.SourcePageNotFoundException;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.ValidationError;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.criticalsoftware.mobics.presentation.action.ErrorActionBean;

/**
 * Handles the exceptions thrown by the application.
 * 
 * @author Samuel Santos
 * @version $Revision: 1.3 $
 */
public class DefaultExceptionHandler implements AutoExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    private static ResourceBundle resources;

    /**
     * Send the user to the global error page.
     * 
     * @param throwable a generic Throwable throwable
     * @param request The HttpServletRequest
     * @param response The HttpServletResponse
     * @return A ForwardResolution
     */
    public Resolution handle(Throwable throwable, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.error(throwable.getMessage(), throwable);
        return new RedirectResolution(ErrorActionBean.class);
    }

    /**
     * Send the user to the global error page.
     * 
     * @param sourcePageNotFoundException when someone needs the source page resolution, but no source page was supplied
     *            in the request
     * @param request The HttpServletRequest
     * @param response The HttpServletResponse
     * @return A ForwardResolution
     */
    public Resolution handle(
            SourcePageNotFoundException sourcePageNotFoundException,
            HttpServletRequest request,
            HttpServletResponse response) {
        LOGGER.warn(sourcePageNotFoundException.getMessage());
        ActionBean bean = (ActionBean) request.getAttribute(StripesConstants.REQ_ATTR_ACTION_BEAN);

        if (bean != null && bean.getContext().getValidationErrors() != null) {
            for (Entry<String, List<ValidationError>> validationErrors : bean.getContext().getValidationErrors()
                    .entrySet()) {
                for (ValidationError validationError : validationErrors.getValue()) {
                    LOGGER.warn(validationError.getMessage(request.getLocale()));
                }
            }
        }

        return new ForwardResolution("/WEB-INF/error.jsp");
    }

    /**
     * If there's an ActionBean present, send the user back where they came from with a stern warning, otherwise send
     * them to the global error page.
     * 
     * @param exception A generic exception
     * @param request The HttpServletRequest
     * @param response The HttpServletResponse
     * @return A ForwardResolution
     */
    public Resolution handle(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.error(exception.getMessage(), exception);
        return insideJobToError("error.BusinessException", request, response);
    }
    
    /**
     * If there's an ActionBean present, send the user back where they came from with a stern warning, otherwise send
     * them to the global error page.
     * 
     * @param UnsupportedEncodingException A generic exception
     * @param request The HttpServletRequest
     * @param response The HttpServletResponse
     * @return A ForwardResolution
     */
    public Resolution handle(UnsupportedEncodingException exception, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.error(exception.getMessage(), exception);
        return insideJobToError("error.UnsupportedEncodingException", request, response);
    }

    /**
     * If there's an ActionBean present, send the user back where they came from with a stern warning, otherwise send
     * them to the global error page.
     * 
     * @param exception a remote exception
     * @param request The HttpServletRequest
     * @param response The HttpServletResponse
     * @return A ForwardResolution
     */
    public Resolution handle(RemoteException exception, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.error(exception.getMessage(), exception);
        return insideJobToError("error.RemoteException", request, response);
    }

    /**
     * If there's an ActionBean present, send the user back where they came from with a stern warning, otherwise send
     * them to the global error page.
     * 
     * @param exception a Car License Plate Not Found
     * @param request The HttpServletRequest
     * @param response The HttpServletResponse
     * @return A ForwardResolution
     */
    public Resolution handle(
            com.criticalsoftware.mobics.proxy.fleet.CarLicensePlateNotFoundExceptionException exception,
            HttpServletRequest request,
            HttpServletResponse response) {
        LOGGER.error(exception.getMessage(), exception);
        return insideJobToError("error.CarLicensePlateNotFoundExceptionException", request, response);
    }

    /**
     * If there's an ActionBean present, send the user back where they came from with a stern warning, otherwise send
     * them to the global error page.
     * 
     * @param exception a Fuel Type Not Found
     * @param request The HttpServletRequest
     * @param response The HttpServletResponse
     * @return A ForwardResolution
     */
    public Resolution handle(
            com.criticalsoftware.mobics.proxy.fleet.FuelTypeNotFoundExceptionException exception,
            HttpServletRequest request,
            HttpServletResponse response) {
        LOGGER.error(exception.getMessage(), exception);
        return insideJobToError("error.FuelTypeNotFoundExceptionException", request, response);
    }

    /**
     * If there's an ActionBean present, send the user back where they came from with a stern warning, otherwise send
     * them to the global error page.
     * 
     * @param exception a Car Class Not Found
     * @param request The HttpServletRequest
     * @param response The HttpServletResponse
     * @return A ForwardResolution
     */
    public Resolution handle(
            com.criticalsoftware.mobics.proxy.fleet.CarClassNotFoundExceptionException exception,
            HttpServletRequest request,
            HttpServletResponse response) {
        LOGGER.error(exception.getMessage(), exception);
        return insideJobToError("error.CarClassNotFoundExceptionException", request, response);
    }
    
    /**
     * Send them to the global error page.
     * 
     * @param exception a Car Type Not Found
     * @param request The HttpServletRequest
     * @param response The HttpServletResponse
     * @return A ForwardResolution
     */
    public Resolution handle(
            com.criticalsoftware.mobics.proxy.fleet.CarTypeNotFoundExceptionException exception,
            HttpServletRequest request,
            HttpServletResponse response) {
        LOGGER.error(exception.getMessage(), exception);
        return insideJobToError("error.CarTypeNotFoundExceptionException", request, response);
    }
    
    /* --------------------------------------------------------- Booking */
    
    
    /**
     * If there's an ActionBean present, send the user back where they came from with a stern warning, otherwise send
     * them to the global error page.
     * 
     * @param exception a Car Class Not Found
     * @param request The HttpServletRequest
     * @param response The HttpServletResponse
     * @return A ForwardResolution
     */
    public Resolution handle(
            com.criticalsoftware.mobics.proxy.booking.InvalidCustomerPinExceptionException exception,
            HttpServletRequest request,
            HttpServletResponse response) {
        LOGGER.error(exception.getMessage(), exception);
        return insideJob("error.InvalidCustomerPinExceptionException", request, response);
    }

    /**
     * Send them to the global error page.
     * 
     * @param exception a Car Type Not Found
     * @param request The HttpServletRequest
     * @param response The HttpServletResponse
     * @return A ForwardResolution
     */
    public Resolution handle(
            com.criticalsoftware.mobics.proxy.booking.CarNotFoundExceptionException exception,
            HttpServletRequest request,
            HttpServletResponse response) {
        LOGGER.error(exception.getMessage(), exception);
        return insideJobToError("error.CarNotFoundExceptionException", request, response);
    }

    /**
     * Send them to the global error page.
     * 
     * @param exception a Car Type Not Found
     * @param request The HttpServletRequest
     * @param response The HttpServletResponse
     * @return A ForwardResolution
     */
    public Resolution handle(
            com.criticalsoftware.mobics.proxy.booking.CarNotAvailableForBookingExceptionException exception,
            HttpServletRequest request,
            HttpServletResponse response) {
        LOGGER.error(exception.getMessage(), exception);
        return insideJob("error.CarNotAvailableForBookingExceptionException", request, response);
    }
    
    /**
     * Send them to the global error page.
     * 
     * @param exception a Car Type Not Found
     * @param request The HttpServletRequest
     * @param response The HttpServletResponse
     * @return A ForwardResolution
     */
    public Resolution handle(
            com.criticalsoftware.mobics.proxy.booking.ForbiddenZoneExceptionException exception,
            HttpServletRequest request,
            HttpServletResponse response) {
        LOGGER.error(exception.getMessage(), exception);
        return insideJob("error.ForbiddenZoneExceptionException", request, response);
    }
    
    /**
     * Send them to the global error page.
     * 
     * @param exception a Car Type Not Found
     * @param request The HttpServletRequest
     * @param response The HttpServletResponse
     * @return A ForwardResolution
     */
    public Resolution handle(
            com.criticalsoftware.mobics.proxy.booking.IllegalDateExceptionException exception,
            HttpServletRequest request,
            HttpServletResponse response) {
        LOGGER.error(exception.getMessage(), exception);
        return insideJob("error.IllegalDateExceptionException", request, response);
    }

    /**
     * Send them to the global error page.
     * 
     * @param exception a Car Type Not Found
     * @param request The HttpServletRequest
     * @param response The HttpServletResponse
     * @return A ForwardResolution
     */
    public Resolution handle(
            com.criticalsoftware.mobics.proxy.booking.UnauthorizedCustomerExceptionException exception,
            HttpServletRequest request,
            HttpServletResponse response) {
        LOGGER.error(exception.getMessage(), exception);
        return insideJob("error.UnauthorizedCustomerExceptionException", request, response);
    }

    /**
     * Send them to the global error page.
     * 
     * @param exception a Car Type Not Found
     * @param request The HttpServletRequest
     * @param response The HttpServletResponse
     * @return A ForwardResolution
     */
    public Resolution handle(
            com.criticalsoftware.mobics.proxy.booking.CarLicensePlateNotFoundExceptionException exception,
            HttpServletRequest request,
            HttpServletResponse response) {
        LOGGER.error(exception.getMessage(), exception);
        return insideJobToError("error.CarLicensePlateNotFoundExceptionException", request, response);
    }
    
    /**
     * Send them to the global error page.
     * 
     * @param exception a Car Type Not Found
     * @param request The HttpServletRequest
     * @param response The HttpServletResponse
     * @return A ForwardResolution
     */
    public Resolution handle(
            com.criticalsoftware.mobics.proxy.booking.BookingNotFoundExceptionException exception,
            HttpServletRequest request,
            HttpServletResponse response) {
        LOGGER.error(exception.getMessage(), exception);
        return insideJobToError("error.BookNotFoundExceptionException", request, response);
    }
    
    
    /**
     * Return the action bean page with errors displayed
     * 
     * @param resourceKey
     * @param request
     * @param response
     * @return
     */
    private Resolution insideJob(String resourceKey, HttpServletRequest request, HttpServletResponse response) {
        resources = ResourceBundle.getBundle("StripesResources", request.getLocale());
        ActionBean bean = (ActionBean) request.getAttribute(StripesConstants.REQ_ATTR_ACTION_BEAN);
        Resolution resolution;

        if (bean != null && StringUtils.isNotBlank(bean.getContext().getSourcePage())) {
            bean.getContext().getValidationErrors().addGlobalError(new LocalizableError(resourceKey));
            resolution = new ForwardResolution(bean.getContext().getSourcePage());
        } else {
            request.setAttribute("error", resources.getString(resourceKey));
            resolution = new ForwardResolution(ErrorActionBean.class);
        }

        return resolution;
    }

    /**
     * Return the error page with errors displayed
     * 
     * @param resourceKey
     * @param request
     * @param response
     * @return
     */
    private Resolution insideJobToError(String resourceKey, HttpServletRequest request, HttpServletResponse response) {
        resources = ResourceBundle.getBundle("StripesResources", request.getLocale());
        request.setAttribute("error", resources.getString(resourceKey));
        return new ForwardResolution(ErrorActionBean.class);
    }
}
