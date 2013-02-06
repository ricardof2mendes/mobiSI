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

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.Wizard;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;
import net.sourceforge.stripes.validation.ValidationState;

import com.criticalsoftware.mobics.presentation.action.BaseActionBean;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.proxy.customer.CustomerNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.customer.CustomerWSServiceStub;

/**
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
@Wizard(startEvents = "main")
public abstract class AskPinActionBean extends BaseActionBean {

    @Validate(required = true, on = { "data", "saveData" }, minlength = 4, maxlength = 4)
    private Integer pin;

    /**
     * Main method
     * 
     * @return Resolution
     */
    public abstract Resolution main();

    /**
     * Presents data
     * 
     * @return Resolution
     * @throws Exception a exception
     */
    public abstract Resolution data() throws Exception;

    /**
     * Save data
     * @return Resolution
     * @throws Exception a exception
     */
    public abstract Resolution saveData() throws Exception;

    /**
     * Validation method for PIN
     * 
     * @param errors
     * @throws RemoteException
     * @throws CustomerNotFoundExceptionException
     * @throws UnsupportedEncodingException
     */
    @ValidationMethod(when = ValidationState.NO_ERRORS, on = "data")
    public void validation(ValidationErrors errors) throws RemoteException, CustomerNotFoundExceptionException,
            UnsupportedEncodingException {

        CustomerWSServiceStub customerWSServiceStub = new CustomerWSServiceStub(
                Configuration.INSTANCE.getCustomerEndpoint());
        customerWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));
        if (!customerWSServiceStub.isValidCustomerPin(pin.toString())) {
            errors.add("pin", new LocalizableError("account.security.check.pin.invalid"));
        }
    }

    /**
     * @return the pin
     */
    public Integer getPin() {
        return pin;
    }

    /**
     * @param pin the pin to set
     */
    public void setPin(Integer pin) {
        this.pin = pin;
    }
}
