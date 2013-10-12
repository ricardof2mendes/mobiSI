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
import com.criticalsoftware.mobics.presentation.action.BaseActionBean;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.presentation.util.OrderBy;
import com.criticalsoftware.mobics.proxy.billing.BillingWSServiceStub;
import com.criticalsoftware.mobics.proxy.billing.CustomerNotFoundExceptionException;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

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

    private String orderBy;

    private BonusAccountBalanceListDTO balanceList;

    private BonusAccountStatementListDTO[] detailedList;

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

        detailedList = stub.getClientBonusAccountStatement(null, Calendar.getInstance());

        return new ForwardResolution("/WEB-INF/bonus/bonusAccount.jsp");
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
}