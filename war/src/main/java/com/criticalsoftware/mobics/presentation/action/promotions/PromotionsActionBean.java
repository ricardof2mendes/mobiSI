/*
 * $Id$
 *
 * Copyright (c) Critical Software S.A., All Rights Reserved.
 * (www.criticalsoftware.com)
 *
 * This software is the proprietary information of Critical Software S.A.
 * Use is subject to license terms.
 *
 * Last changed on: $Date$
 * Last changed by: $Author$
 */
package com.criticalsoftware.mobics.presentation.action.promotions;

import com.criticalsoftware.mobics.carclub.CalendarRestrictionsDTO;
import com.criticalsoftware.mobics.carclub.DayRestrictionsDTO;
import com.criticalsoftware.mobics.carclub.PromotionsDetailListDTO;
import com.criticalsoftware.mobics.customer.PromotionStatusEnum;
import com.criticalsoftware.mobics.customer.PromotionWebServiceListDTO;
import com.criticalsoftware.mobics.presentation.action.BaseActionBean;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.presentation.util.DayRestrictionDTO;
import com.criticalsoftware.mobics.proxy.carclub.CarClubWSServiceStub;
import com.criticalsoftware.mobics.proxy.carclub.PromotionCodeNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.customer.CustomerNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.customer.CustomerWSServiceStub;
import com.criticalsoftware.mobics.proxy.fleet.FleetWSServiceStub;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.*;

/**
 * Account action bean
 *
 * @author ltiago
 * @version $Revision$
 */
@MobiCSSecure
public class PromotionsActionBean extends BaseActionBean {

    protected final Logger LOG = LoggerFactory.getLogger(PromotionsActionBean.class);

    @Validate(required = true, on = {"detail", "getPromotionImage"})
    private String code;

    @Validate
    protected Integer width = 58;

    @Validate
    protected Integer height = 58;

    private PromotionsDetailListDTO promotion;

    private PromotionWebServiceListDTO[] promotions;

    /**
     * Account page
     *
     * @return the page resolution
     */
    @DefaultHandler
    @DontValidate
    public Resolution main() throws RemoteException, UnsupportedEncodingException, CustomerNotFoundExceptionException {
        CustomerWSServiceStub stub = new CustomerWSServiceStub(Configuration.INSTANCE.getCustomerEndpoint());
        stub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser().getPassword()));
        // FIXME nao posso enviar data
        Calendar a = Calendar.getInstance();
        a.add(Calendar.MONTH, 1);
        promotions = stub.getAvailablePromotionsForCustomer(Calendar.getInstance(), a, "", false);

        return new ForwardResolution("/WEB-INF/promotions/promotions.jsp");
    }

    @DontValidate
    public Resolution detail() throws RemoteException, PromotionCodeNotFoundExceptionException, UnsupportedEncodingException {
        CarClubWSServiceStub stub = new CarClubWSServiceStub(Configuration.INSTANCE.getCarClubEndpoint());
        promotion = stub.getPromotionDetail(code, false);
        return new ForwardResolution("/WEB-INF/promotions/promotionDetail.jsp");
    }

    @DontValidate
    public Resolution getPromotionImage() {
        Resolution resolution = null;
        try {
            int w = this.getContext().getRetina() ? 2 * width : width;
            int h = this.getContext().getRetina() ? 2 * height : height;

            DataHandler handler = new CarClubWSServiceStub(Configuration.INSTANCE.getCarClubEndpoint()).getPromotionThumbnail(
                    code, w, h);
            resolution = new StreamingResolution(handler.getContentType(), handler.getInputStream());
        } catch (Exception e) {
            LOG.warn("Could not load image", e.getMessage());
        }
        return resolution;
    }

    public List<PromotionWebServiceListDTO> getOngoingPromotions() {
        List<PromotionWebServiceListDTO> ongoing = null;
        if (promotions != null) {
            for (int i = 0; i < promotions.length; i++) {
                PromotionWebServiceListDTO promo = promotions[i];
                if (PromotionStatusEnum.ONGOING.equals(promo.getStatus())) {
                    if (ongoing == null)
                        ongoing = new ArrayList<PromotionWebServiceListDTO>();
                    ongoing.add(promo);
                }
            }
        }
        return ongoing;
    }

    public List<PromotionWebServiceListDTO> getFuturePromotions() {
        List<PromotionWebServiceListDTO> future = null;
        if (promotions != null) {
            for (int i = 0; i < promotions.length; i++) {
                PromotionWebServiceListDTO promo = promotions[i];
                if (PromotionStatusEnum.ENABLED.equals(promo.getStatus())) {
                    if (future == null)
                        future = new ArrayList<PromotionWebServiceListDTO>();
                    future.add(promo);
                }
            }
        }
        return future;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public PromotionsDetailListDTO getPromotion() {
        return promotion;
    }

    public Map<String, List<DayRestrictionDTO>> getRestrictions() {
        Map<String, List<DayRestrictionDTO>> days = null;
        for (CalendarRestrictionsDTO dto : promotion.getRestrictions()) {
            if (dto.getDayRestrictions() != null) {
                List<DayRestrictionDTO> restrictions = new ArrayList<DayRestrictionDTO>();
                for (DayRestrictionsDTO dr: dto.getDayRestrictions()) {
                    restrictions.add(
                            new DayRestrictionDTO(dto.getType().getValue(), dr.getStartTime(), dr.getEndTime()));
                }

                if (days == null) {
                    days = new HashMap<String, List<DayRestrictionDTO>>();
                }
                days.put(dto.getDayOfWeek().getValue(), restrictions);
            }
        }
        return days;
    }


    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}