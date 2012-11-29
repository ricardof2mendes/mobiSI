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
public class EditPinActionBean extends AskPinActionBean {

    @Validate(required = true, on = "saveData", minlength = 4, maxlength = 4)
    private String newPin;

    @Validate(required = true, on = "saveData", minlength = 4, maxlength = 4, expression = "this == newPin")
    private String pinConfirm;

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
    public Resolution data() throws RemoteException, CustomerNotFoundExceptionException, UnsupportedEncodingException {
        return new ForwardResolution("/WEB-INF/account/editPin.jsp");
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
        customerWSServiceStub.updateCustomerPin(getContext().getUser().getUsername(), getContext().getUser()
                .getPassword(), newPin);

        getContext().getMessages().add(new LocalizableMessage("account.authentication.edit.email.pin.success"));
        return new RedirectResolution(AccountActionBean.class).flash(this);
    }

    /**
     * @return the newPin
     */
    public String getNewPin() {
        return newPin;
    }

    /**
     * @param newPin the newPin to set
     */
    public void setNewPin(String email) {
        this.newPin = email;
    }

    /**
     * @return the pinConfirm
     */
    public String getPinConfirm() {
        return pinConfirm;
    }

    /**
     * @param pinConfirm the pinConfirm to set
     */
    public void setPinConfirm(String emailConfirm) {
        this.pinConfirm = emailConfirm;
    }

}
