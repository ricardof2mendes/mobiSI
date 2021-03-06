/*
 * $Id: LoginActionBean.java,v 1.3 2012/05/04 14:35:36 ssantos Exp $
 *
 * Last changed on $Date: 2012/05/04 14:35:36 $
 * Last changed by $Author: ssantos $
 */
package com.criticalsoftware.mobics.presentation.action;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;

import com.criticalsoftware.mobics.carclub.CarClubSimpleDTO;
import com.criticalsoftware.mobics.presentation.action.trip.TripActionBean;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.User;
import com.criticalsoftware.mobics.presentation.util.CarClubSimple;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.proxy.booking.BookingWSServiceStub;
import com.criticalsoftware.mobics.proxy.carclub.CarClubWSServiceStub;
import com.criticalsoftware.mobics.proxy.customer.CustomerNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.customer.CustomerWSServiceStub;
import com.criticalsoftware.www.mobios.country.configuration.CountryConfiguration;

/**
 * Sign-in action bean.
 * 
 * @author Samuel Santos
 * @version $Revision: 1.3 $
 */
public class LoginActionBean extends BaseActionBean {

    @Validate
    private String username;

    @Validate
    private String password;

    @Validate
    private boolean retina;
    
    @Validate
    private String ccCode;

    @DefaultHandler
    @DontValidate
    public Resolution login() {
        return new ForwardResolution("/WEB-INF/login.jsp");
    }

    @ValidationMethod(on = "authenticate")
    public void validate(ValidationErrors errors) {
        if (username == null || password == null) {
            errors.addGlobalError(new LocalizableError("login.error"));
        }
    }
    
    private boolean customerHasOngoingTrip() throws UnsupportedEncodingException, RemoteException{
       BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
               Configuration.INSTANCE.getBookingEndpoint());
       bookingWSServiceStub._getServiceClient().addHeader(
               AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                       .getPassword()));
       return bookingWSServiceStub.isCustomerInOngoingTrip();
    }

    public Resolution authenticate() throws UnsupportedEncodingException, RemoteException,
    CustomerNotFoundExceptionException {
        Resolution resolution = new RedirectResolution(HomeActionBean.class);

        OMElement authHeader = AuthenticationUtil.getAuthenticationHeader(username, password);

        CarClubWSServiceStub carClubWSServiceStub = new CarClubWSServiceStub(
                Configuration.INSTANCE.getCarClubEndpoint());
        carClubWSServiceStub._getServiceClient().addHeader(authHeader);

        String errorResolution = "/WEB-INF/login.jsp?CC=" + ccCode;
        
        try {
            CarClubSimpleDTO carClubDTO = carClubWSServiceStub.getCustomerCarClub();
            if (carClubDTO != null) {
                CustomerWSServiceStub customerWSServiceStub = new CustomerWSServiceStub(
                        Configuration.INSTANCE.getCustomerEndpoint());
                customerWSServiceStub._getServiceClient().addHeader(authHeader);

                this.getContext().setCarClub(new CarClubSimple(carClubDTO.getCarClubName(), carClubDTO.getCarClubCode(),
                        carClubDTO.getCarClubContactPhone(), carClubDTO.getCarClubContactEmail()));
                this.getContext().setUser(
                        new User(username, password, carClubDTO, customerWSServiceStub.getCustomerPreferences()));

                CountryConfiguration patterns = carClubWSServiceStub.getCountryConfigurationByCarClubCode(
                        carClubDTO.getCarClubCode());

                Configuration config = (Configuration) this.getContext().getServletContext().getAttribute("configuration");
                config.setCarClubConfiguration(patterns);
                this.getContext().getServletContext().setAttribute("carClubConfiguration", config.getCarClubConfiguration());
                this.getContext().getServletContext().setAttribute("carClubCode", carClubDTO.getCarClubCode());

                this.getContext().setRetina(retina);
                
                //MOBICSMAINT-142
                if(customerHasOngoingTrip()){
                    resolution = new RedirectResolution(TripActionBean.class);
                }
                
            } else {
                this.getContext().getValidationErrors().addGlobalError(new LocalizableError("login.carclub.error"));
                resolution = new ForwardResolution(errorResolution);
            }
        } catch (AxisFault e) {
            if (e.getMessage().startsWith(Configuration.INSTANCE.getAuthenticationFailureString())) {
                this.getContext().getValidationErrors().addGlobalError(new LocalizableError("login.error"));
                resolution = new ForwardResolution(errorResolution);
            } else {
                throw e;
            }
        }
        return resolution;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the retina
     */
    public boolean isRetina() {
        return retina;
    }

    /**
     * @param retina the retina to set
     */
    public void setRetina(boolean retina) {
        this.retina = retina;
    }

    public String getCcCode() {
        return ccCode;
    }

    public void setCcCode(String ccCode) {
        this.ccCode = ccCode;
    }

}
