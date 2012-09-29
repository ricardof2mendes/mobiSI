/*
 * $Id: HomeActionBean.java,v 1.3 2012/05/04 14:35:36 ssantos Exp $
 *
 * Last changed on $Date: 2012/05/04 14:35:36 $
 * Last changed by $Author: ssantos $
 */
package com.criticalsoftware.mobics.presentation.action;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;

import javax.xml.stream.XMLStreamException;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.proxy.booking.BookingWSServiceStub;
import com.criticalsoftware.mobics.proxy.carclub.CarClubWSServiceStub;
import com.criticalsoftware.mobics.proxy.customer.CustomerWSServiceStub;
import com.criticalsoftware.mobics.proxy.fleet.FleetWSServiceStub;

/**
 * Home action bean.
 *
 * @author Samuel Santos
 * @version $Revision: 1.3 $
 */
@MobiCSSecure
public class HomeActionBean extends BaseActionBean {
    
    private static Logger LOGGER = LoggerFactory.getLogger(HomeActionBean.class);

    @DefaultHandler
    public Resolution main() {
            try {
                LOGGER.trace("Creating header");
                OMElement header = AuthenticationUtil.getAuthenticationHeader("present@mobiag.pt", "critical");
                LOGGER.info("header "+header.toStringWithConsume());

                FleetWSServiceStub stub = new FleetWSServiceStub("http://mobics02.critical.pt:8080/mobics-webservices/Fleet");
                LOGGER.info("->" + stub.getFleet("1111")[0].getCarBrandName());
//                
//                BookingWSServiceStub p = new BookingWSServiceStub("http://mobics02.critical.pt:8080/mobics-webservices/Booking");
//                p._getServiceClient().addHeader(header);
//                LOGGER.trace("**********"+p.isCarInForbiddenZone());

                CarClubWSServiceStub st = new CarClubWSServiceStub("http://mobics02.critical.pt:8080/mobics-webservices/CarClub");
                st._getServiceClient().addHeader(header);
                LOGGER.trace("Making the call");
                LOGGER.trace("Car club name? "+st.getCustomerCarClub().getCarClubName());
//                
                CustomerWSServiceStub s = new CustomerWSServiceStub("http://mobics02.critical.pt:8080/mobics-webservices/Customer");
                s._getServiceClient().addHeader(AuthenticationUtil.getAuthenticationHeader("present@mobiag.pt", "critical"));
                LOGGER.info("Making the call");
                final Boolean isValidPin = new Boolean(s.isValidCustomerPin("lmlkm"));
                LOGGER.info("is valid pin " +isValidPin);
                
                
            } catch (AxisFault e) {
                System.out.println(e.getLocalizedMessage());
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace()[0].getClassName());
                e.printStackTrace();
            } catch (RemoteException e) {
                // TODO ltiago: Auto-generated catch block. This code MUST be changed to the appropriate statements in order to handle the exception.
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO ltiago: Auto-generated catch block. This code MUST be changed to the appropriate statements in order to handle the exception.
                e.printStackTrace();
            } catch (XMLStreamException e) {
                // TODO ltiago: Auto-generated catch block. This code MUST be changed to the appropriate statements in order to handle the exception.
                e.printStackTrace();
            }
            
        return new ForwardResolution("/WEB-INF/home.jsp");
    }
    
    public Resolution bookCarForNow() {
        return new ForwardResolution("/WEB-INF/book/bookCarForNow.jsp");
    }
    
    public Resolution findCarForLater() {
        return new ForwardResolution("/WEB-INF/book/findCarForLater.jsp");
    }
    
    public Resolution bookInAdvance() {
        return new ForwardResolution("/WEB-INF/book/findCarForLater.jsp");
    }
}
