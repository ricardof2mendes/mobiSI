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

import org.apache.axis2.AxisFault;

import com.criticalsoftware.mobics.carclub.CarClubSimpleDTO;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.User;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.proxy.carclub.CarClubWSServiceStub;

/**
 * Sign-in action bean.
 * 
 * @author Samuel Santos
 * @version $Revision: 1.3 $
 */
public class LoginActionBean extends BaseActionBean {

    private CarClubWSServiceStub carClubWSServiceStub;

    @Validate
    private String username;

    @Validate
    private String password;

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

    public Resolution authenticate() throws UnsupportedEncodingException, RemoteException {
        Resolution resolution = new RedirectResolution(HomeActionBean.class);

        carClubWSServiceStub = new CarClubWSServiceStub(Configuration.INSTANCE.getCarClubEndpoint());
        carClubWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(username, password));

        try {
            CarClubSimpleDTO carClubDTO = carClubWSServiceStub.getCustomerCarClub();

            if (carClubDTO != null) {
                this.getContext().setUser(
                        new User(username, password, carClubDTO.getCarClubName(), carClubDTO.getCarClubCode(),
                                carClubDTO.getCarClubColorScheme(), carClubDTO.getCarClubTheme()));
            }
        } catch (AxisFault e) {
            if (e.getMessage().startsWith(Configuration.INSTANCE.getAuthenticationFailureString())) {
                this.getContext().getValidationErrors().addGlobalError(new LocalizableError("login.error"));
                resolution = new ForwardResolution("/WEB-INF/login.jsp");
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

}
