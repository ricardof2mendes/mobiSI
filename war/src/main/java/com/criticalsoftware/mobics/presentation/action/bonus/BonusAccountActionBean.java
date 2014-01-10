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

import com.criticalsoftware.mobics.billing.BillingDetailedListDTO;
import com.criticalsoftware.mobics.billing.BonusAccountBalanceListDTO;
import com.criticalsoftware.mobics.billing.BonusAccountStatementListDTO;
import com.criticalsoftware.mobics.customer.CreditPurchaseDetailsDTO;
import com.criticalsoftware.mobics.presentation.action.BaseActionBean;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.presentation.util.OrderBy;
import com.criticalsoftware.mobics.proxy.billing.BillingWSServiceStub;
import com.criticalsoftware.mobics.proxy.billing.CustomerNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.billing.InvoiceNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.billing.InvoicePdfNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.customer.CreditPurchaseExceptionException;
import com.criticalsoftware.mobics.proxy.customer.CustomerWSServiceStub;
import com.criticalsoftware.www.mobios.services.accounting.dto.FileAttachmentDTO;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.apache.axis2.AxisFault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;

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

    /**
     * Account page
     *
     * @return the page resolution
     */
    @DefaultHandler
    @DontValidate
    public Resolution main() throws RemoteException, UnsupportedEncodingException, CustomerNotFoundExceptionException {
        orderBy = OrderBy.BALANCE.name();

        BillingWSServiceStub stub = new BillingWSServiceStub(Configuration.INSTANCE.getBillingEndpoint());
        stub._getServiceClient().addHeader(AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                .getPassword()));
        balanceList = stub.getBonusAccount();

        return new ForwardResolution("/WEB-INF/bonus/bonusAccount.jsp");
    }

    @DontValidate
    public Resolution transactions() throws RemoteException, UnsupportedEncodingException, CustomerNotFoundExceptionException {
        orderBy = OrderBy.TRANSACTIONS.toString();

        BillingWSServiceStub stub = new BillingWSServiceStub(Configuration.INSTANCE.getBillingEndpoint());
        stub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser().getPassword()));

        // TODO add to configuration
        Calendar thisYear = Calendar.getInstance();
        Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);

        detailedList = stub.getClientBonusAccountStatement(lastYear.getTimeInMillis(), thisYear.getTimeInMillis(),
                                                           0, Integer.MAX_VALUE);

        return new ForwardResolution("/WEB-INF/bonus/bonusAccount.jsp");
    }

    @DontValidate
    public Resolution creditDetails()
    throws RemoteException, UnsupportedEncodingException, CreditPurchaseExceptionException,
    com.criticalsoftware.mobics.proxy.customer.CustomerNotFoundExceptionException {
        CustomerWSServiceStub customerWSServiceStub = new CustomerWSServiceStub(
                Configuration.INSTANCE.getCustomerEndpoint());
        customerWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));

        credit = customerWSServiceStub.getCreditDetailsByCdrCode(activityCode);

        return new ForwardResolution("/WEB-INF/recent/creditDetails.jsp");
    }

    public String getOrderBy() {
        return orderBy;
    }

    public BonusAccountBalanceListDTO getBalanceList() {
        return balanceList;
    }

    public BonusAccountStatementListDTO[] getDetailedList() {
        return detailedList;
    }

    public Date getToday(){
        return new Date();
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public CreditPurchaseDetailsDTO getCredit() {
        return credit;
    }
}