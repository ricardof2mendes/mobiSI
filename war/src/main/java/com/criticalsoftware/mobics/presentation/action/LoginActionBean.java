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
import net.sourceforge.stripes.validation.EmailTypeConverter;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang.StringUtils;

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

    @Validate(required = true, on = "authenticate", converter = EmailTypeConverter.class)
    private String username;

    @Validate(required = true, on = "authenticate")
    private String password;

    @DefaultHandler
    @DontValidate
    public Resolution login() {
        return new ForwardResolution("/WEB-INF/login.jsp");
    }

    public Resolution authenticate() throws UnsupportedEncodingException, RemoteException {
        Resolution resolution = new RedirectResolution(HomeActionBean.class);

        carClubWSServiceStub = new CarClubWSServiceStub(Configuration.CAR_CLUB_ENDPOINT);
        carClubWSServiceStub._getServiceClient().addHeader(AuthenticationUtil.getAuthenticationHeader(username,  password));
        
        try {
            String carClubName = carClubWSServiceStub.getCustomerCarClub().getCarClubName();
            if(StringUtils.isNotEmpty(carClubName)){
                this.getContext().setUser(new User(username, password, carClubName));
            }
        } catch (AxisFault e) {
            if(e.getMessage().startsWith(Configuration.AUTHENTICATION_FAILURE_STRING)) {
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
