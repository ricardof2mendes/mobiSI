/*
 * $Id: codetemplates.xml 562 2012-08-01 15:04:39Z fa-vieira $
 *
 * Copyright (c) Critical Software S.A., All Rights Reserved.
 * (www.criticalsoftware.com)
 *
 * This software is the proprietary information of Critical Software S.A.
 * Use is subject to license terms.
 *
 * Last changed on: $Date: 2012-08-01 16:04:39 +0100 (qua, 01 ago 2012) $
 * Last changed by: $Author: fa-vieira $
 */
package com.criticalsoftware.mobics.presentation.action.account;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.LocalizableError;

import com.criticalsoftware.mobics.customer.CreditPurchasePaymentInfoDTO;
import com.criticalsoftware.mobics.customer.CreditPurchasePaymentMethodEnum;
import com.criticalsoftware.mobics.presentation.action.BaseActionBean;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.proxy.customer.CreditPurchaseExceptionException;
import com.criticalsoftware.mobics.proxy.customer.CustomerNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.customer.CustomerWSServiceStub;
import com.criticalsoftware.mobics.proxy.customer.OrganizationNotFoundExceptionException;

/**
 * Payment Reference Bean
 *
 * @author embarros
 * @version $Revision: 562 $
 */
@MobiCSSecure
public class PaymentReferenceActionBean extends BaseActionBean {

    private CreditPurchasePaymentInfoDTO creditPurchase;

    /**
     * @return
     * @throws UnsupportedEncodingException
     * @throws RemoteException
     * @throws OrganizationNotFoundExceptionException
     * @throws CustomerNotFoundExceptionException
     * @throws CreditPurchaseExceptionException
     */
    @DefaultHandler
    @DontValidate
    public Resolution createPaymentReference() throws UnsupportedEncodingException, RemoteException,
            OrganizationNotFoundExceptionException, CustomerNotFoundExceptionException,
            CreditPurchaseExceptionException {

        final CustomerWSServiceStub customerWSServiceStub = new CustomerWSServiceStub(
                Configuration.INSTANCE.getCustomerEndpoint());
        customerWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext()
                        .getUser().getPassword()));

        this.creditPurchase = customerWSServiceStub
                .createPaymentReferenceData(CreditPurchasePaymentMethodEnum.MULTIBANCO);

        if (this.creditPurchase != null) {
            this.getContext().getMessages().add(new LocalizableMessage("credit.send.reference.success"));
        } else {
            this.getContext().getValidationErrors().addGlobalError(new LocalizableError("credit.send.reference.error"));
        }

        return new ForwardResolution("/WEB-INF/account/account.jsp");
    }

}
