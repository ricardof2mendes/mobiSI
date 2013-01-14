/*
 * $Id: $
 *
 * Copyright (c) Critical Software S.A., All Rights Reserved.
 * (www.criticalsoftware.com)
 *
 * This software is the proprietary information of Critical Software S.A.
 * Use is subject to license terms.
 *
 * Last changed on: $Date: $
 * Last changed by: $Author: $
 */
package com.criticalsoftware.mobics.presentation.action.account;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.proxy.customer.CustomerNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.customer.CustomerWSServiceStub;
import com.criticalsoftware.mobics.proxy.customer.InvalidLoginExceptionException;

/**
 * Account action bean
 * 
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class EditPasswordActionBean extends AskPinActionBean {

    @Validate(required = true, on = "saveData")
    private String password;

    @Validate(required = true, on = "saveData", expression = "this == password")
    private String checkPassword;

    /**
     * Account page
     * 
     * @return the page resolution
     */
    @DefaultHandler
    @DontValidate
    public Resolution main() {
        return new ForwardResolution("/WEB-INF/account/askPin.jsp");
    }

    /**
     * Information page
     * 
     * @return the page resolution
     * @throws RemoteException
     * @throws CustomerNotFoundExceptionException
     * @throws UnsupportedEncodingException
     */
    public Resolution data() throws RemoteException {
        return new ForwardResolution("/WEB-INF/account/editPassword.jsp");
    }

    /**
     * Save Data
     * 
     * @throws InvalidLoginExceptionException
     * @throws UnsupportedEncodingException
     */
    public Resolution saveData() throws RemoteException, InvalidLoginExceptionException, UnsupportedEncodingException {

        CustomerWSServiceStub customerWSServiceStub = new CustomerWSServiceStub(
                Configuration.INSTANCE.getCustomerEndpoint());
        customerWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));
        customerWSServiceStub.updateCustomerPassword(getContext().getUser().getUsername(),
                AuthenticationUtil.encriptSHA(getContext().getUser().getPassword()),
                AuthenticationUtil.encriptSHA(password));
        // also update password in context
        getContext().getUser().setPassword(password);

        getContext().getMessages().add(new LocalizableMessage("account.authentication.edit.email.password.success"));
        return new RedirectResolution(AccountActionBean.class).flash(this);
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
     * @return the checkPassword
     */
    public String getCheckPassword() {
        return checkPassword;
    }

    /**
     * @param checkPassword the checkPassword to set
     */
    public void setCheckPassword(String checkPassword) {
        this.checkPassword = checkPassword;
    }

}
