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
package com.criticalsoftware.mobics.presentation.action.bonus;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.criticalsoftware.mobics.billing.BonusAccountBalanceListDTO;
import com.criticalsoftware.mobics.billing.BonusAccountStatementListDTO;
import com.criticalsoftware.mobics.customer.CreditPurchaseDetailsDTO;
import com.criticalsoftware.mobics.customer.CreditPurchasePaymentInfoDTO;
import com.criticalsoftware.mobics.customer.CreditPurchasePaymentMethodEnum;
import com.criticalsoftware.mobics.customer.PaymentTypeEnum;
import com.criticalsoftware.mobics.presentation.action.BaseActionBean;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.presentation.util.OrderBy;
import com.criticalsoftware.mobics.proxy.billing.BillingWSServiceStub;
import com.criticalsoftware.mobics.proxy.billing.CustomerNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.customer.CreditPurchaseExceptionException;
import com.criticalsoftware.mobics.proxy.customer.CustomerWSServiceStub;
import com.criticalsoftware.mobics.proxy.customer.EntityPaymentMethodNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.customer.OrganizationNotFoundExceptionException;

/**
 * Account action bean
 *
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class BonusAccountActionBean extends BaseActionBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(BonusAccountActionBean.class);

    private String orderBy;

    private BonusAccountBalanceListDTO balanceList;

    private BonusAccountStatementListDTO[] detailedList;

    @Validate(required = true, on = "invoiceDetails")
    private String activityCode;

    private CreditPurchaseDetailsDTO credit;

    private boolean isPrepaid;
    private CreditPurchasePaymentInfoDTO creditPurchase;

    /**
     * Account page
     *
     * @return the page resolution
     * @throws com.criticalsoftware.mobics.proxy.customer.CustomerNotFoundExceptionException
     */
    @DefaultHandler
    @DontValidate
    public Resolution main() throws RemoteException, UnsupportedEncodingException, CustomerNotFoundExceptionException,
            com.criticalsoftware.mobics.proxy.customer.CustomerNotFoundExceptionException {
        this.orderBy = OrderBy.BALANCE.name();

        final BillingWSServiceStub stub = new BillingWSServiceStub(Configuration.INSTANCE.getBillingEndpoint());
        stub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext()
                        .getUser().getPassword()));
        this.balanceList = stub.getBonusAccount();

        this.getUserPaymentType();

        return new ForwardResolution("/WEB-INF/bonus/bonusAccount.jsp");
    }

    @DontValidate
    public Resolution transactions() throws RemoteException, UnsupportedEncodingException,
    CustomerNotFoundExceptionException {
        this.orderBy = OrderBy.TRANSACTIONS.toString();

        final BillingWSServiceStub stub = new BillingWSServiceStub(Configuration.INSTANCE.getBillingEndpoint());
        stub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext()
                        .getUser().getPassword()));

        // TODO add to configuration
        final Calendar thisYear = Calendar.getInstance();
        final Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);

        this.detailedList = stub.getClientBonusAccountStatement(lastYear.getTimeInMillis(), thisYear.getTimeInMillis(),
                0, Integer.MAX_VALUE);

        return new ForwardResolution("/WEB-INF/bonus/bonusAccount.jsp");
    }

    @DontValidate
    public Resolution creditDetails() throws RemoteException, UnsupportedEncodingException,
    CreditPurchaseExceptionException,
    com.criticalsoftware.mobics.proxy.customer.CustomerNotFoundExceptionException {
        final CustomerWSServiceStub customerWSServiceStub = new CustomerWSServiceStub(
                Configuration.INSTANCE.getCustomerEndpoint());
        customerWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(this.getContext().getUser().getUsername(), this.getContext()
                        .getUser().getPassword()));

        this.credit = customerWSServiceStub.getCreditDetailsByCdrCode(this.activityCode);

        return new ForwardResolution("/WEB-INF/recent/creditDetails.jsp");
    }

    /**
     * Create a manual payment reference to charge the customer account.
     *
     * @return the page resolution
     * @throws UnsupportedEncodingException
     * @throws RemoteException
     * @throws OrganizationNotFoundExceptionException
     * @throws CustomerNotFoundExceptionException
     * @throws CreditPurchaseExceptionException
     * @throws EntityPaymentMethodNotFoundExceptionException
     * @throws com.criticalsoftware.mobics.proxy.customer.CustomerNotFoundExceptionException
     */
    @DontValidate
    public Resolution createPaymentReference() throws UnsupportedEncodingException, RemoteException,
            OrganizationNotFoundExceptionException, CustomerNotFoundExceptionException,
            CreditPurchaseExceptionException, EntityPaymentMethodNotFoundExceptionException,
    com.criticalsoftware.mobics.proxy.customer.CustomerNotFoundExceptionException {

        final CustomerWSServiceStub customerWSServiceStub = this.getUserPaymentType();

        this.creditPurchase = customerWSServiceStub
                .createPaymentReferenceData(CreditPurchasePaymentMethodEnum.MULTIBANCO);

        if (this.creditPurchase != null) {
            this.getContext().getMessages().add(new LocalizableMessage("credit.send.reference.success"));
        } else {
            this.getContext().getValidationErrors().addGlobalError(new LocalizableError("credit.send.reference.error"));
        }

        return new ForwardResolution("/WEB-INF/bonus/bonusAccount.jsp");
    }

    /**
     * Check the user payment type.
     *
     * @return customer web service stub
     * @throws RemoteException
     * @throws com.criticalsoftware.mobics.proxy.customer.CustomerNotFoundExceptionException
     * @throws UnsupportedEncodingException
     */
    private CustomerWSServiceStub getUserPaymentType() throws RemoteException,
    com.criticalsoftware.mobics.proxy.customer.CustomerNotFoundExceptionException, UnsupportedEncodingException {

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
        return customerWSServiceStub;
    }

    public String getOrderBy() {
        return this.orderBy;
    }

    public BonusAccountBalanceListDTO getBalanceList() {
        return this.balanceList;
    }

    public BonusAccountStatementListDTO[] getDetailedList() {
        return this.detailedList;
    }

    public Date getToday() {
        return new Date();
    }

    public void setActivityCode(final String activityCode) {
        this.activityCode = activityCode;
    }

    public CreditPurchaseDetailsDTO getCredit() {
        return this.credit;
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