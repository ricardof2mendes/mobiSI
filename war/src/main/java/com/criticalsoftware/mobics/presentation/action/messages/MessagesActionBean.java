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
package com.criticalsoftware.mobics.presentation.action.messages;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import com.criticalsoftware.mobics.booking.BookingInterestMessageDTO;
import com.criticalsoftware.mobics.presentation.action.BaseActionBean;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.proxy.booking.BookingWSServiceStub;
import com.criticalsoftware.mobics.proxy.booking.CustomerNotFoundExceptionException;

/**
 * Messages action bean
 * 
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class MessagesActionBean extends BaseActionBean {

    private BookingInterestMessageDTO[] messages;

    @Validate(required = true, on = "detail")
    private String code;

    /**
     * Account page
     * 
     * @return the page resolution
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws CustomerNotFoundExceptionException
     */
    @DefaultHandler
    @DontValidate
    public Resolution main() throws RemoteException, UnsupportedEncodingException, CustomerNotFoundExceptionException {
        BookingWSServiceStub bookingWSServiceStub = new BookingWSServiceStub(
                Configuration.INSTANCE.getBookingEndpoint());
        bookingWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));

        messages = bookingWSServiceStub.getBookingMessages();

//        if (messages == null) {
//            messages = new BookingInterestMessageDTO[2];
//            BookingInterestMessageDTO m = new BookingInterestMessageDTO();
//            m.setCarName("carro de teste 123");
//            m.setCarPlate("aaaaaaaa");
//            m.setCode("code");
//            m.setIsRead(true);
//            messages[0] = m;
//            m = new BookingInterestMessageDTO();
//            m.setCarName("carro de teste 321");
//            m.setCarPlate("bbbbbbb");
//            m.setCode("code 2");
//            m.setIsRead(false);
//            messages[1] = m;
//        }

        return new ForwardResolution("/WEB-INF/messages/messages.jsp");
    }
    
    /**
     * Number of messages
     * 
     * @return
     */
    public int getSize() {
        return messages == null ? 0 : messages.length;
    }

    /**
     * @return the messages
     */
    public BookingInterestMessageDTO[] getMessages() {
        return messages;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }
}