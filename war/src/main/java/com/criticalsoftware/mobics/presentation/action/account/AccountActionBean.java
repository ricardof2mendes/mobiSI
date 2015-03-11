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
import net.sourceforge.stripes.action.Resolution;

import com.criticalsoftware.mobics.customer.PaymentTypeEnum;
import com.criticalsoftware.mobics.presentation.action.BaseActionBean;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.proxy.customer.CustomerNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.customer.CustomerWSServiceStub;
import com.criticalsoftware.mobics.proxy.customer.InvalidEmailTokenExceptionException;

/**
 * Account action bean
 *
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class AccountActionBean extends BaseActionBean {

    private boolean isPrepaid;

    /**
     * Account page
     *
     * @return the page resolution
     * @throws UnsupportedEncodingException
     * @throws CustomerNotFoundExceptionException
     * @throws RemoteException
     */
    @DefaultHandler
    @DontValidate
    public Resolution main() throws UnsupportedEncodingException, RemoteException, CustomerNotFoundExceptionException {
        final CustomerWSServiceStub customerWSServiceStub = new CustomerWSServiceStub(
                Configuration.INSTANCE.getCustomerEndpoint());
        customerWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext()
                        .getUser().getPassword()));
        final PaymentTypeEnum paymentType = customerWSServiceStub.getCustomerDetails(
                this.getContext().getLocale().getLanguage()).getPaymentType();

        if (paymentType.equals(PaymentTypeEnum.POSTPAID_PENDING) || paymentType.equals(PaymentTypeEnum.PREPAID)) {
            this.setIsPrepaid(true);
        } else {
            this.setIsPrepaid(false);
        }

        return new ForwardResolution("/WEB-INF/account/account.jsp");
    }

    /**
     * Authentication page
     *
     * @return the page resolution
     * @throws RemoteException
     * @throws CustomerNotFoundExceptionException
     * @throws UnsupportedEncodingException
     */
    @DontValidate
    public Resolution authentication() {
        return new ForwardResolution("/WEB-INF/account/authentication.jsp");
    }

    /**
     * Check email validation to show message
     *
     * @return true if email is validated
     * @throws RemoteException
     * @throws CustomerNotFoundExceptionException
     * @throws UnsupportedEncodingException
     * @throws InvalidEmailTokenExceptionException
     */
    public boolean getMessage() throws RemoteException, CustomerNotFoundExceptionException,
    UnsupportedEncodingException, InvalidEmailTokenExceptionException {

        final CustomerWSServiceStub customerWSServiceStub = new CustomerWSServiceStub(
                Configuration.INSTANCE.getCustomerEndpoint());
        customerWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext()
                        .getUser().getPassword()));

        return customerWSServiceStub.checkCustomerHasEmailToken();
    }

    /**
     * @return the isPrepaid
     */
    public boolean getIsPrepaid() {
        return this.isPrepaid;
    }

    /**
     * @param isPrepaid the isPrepaid to set
     */
    public void setIsPrepaid(final boolean isPrepaid) {
        this.isPrepaid = isPrepaid;
    }

}