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
package com.criticalsoftware.mobics.presentation.action.addons;

import com.criticalsoftware.mobics.carclub.AddOnsListDetailsDTO;
import com.criticalsoftware.mobics.carclub.CalendarRestrictionsDTO;
import com.criticalsoftware.mobics.carclub.DayRestrictionsDTO;
import com.criticalsoftware.mobics.fleet.ZoneWithPolygonDTO;
import com.criticalsoftware.mobics.presentation.action.BaseActionBean;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.presentation.util.CoordinateZonesDTO;
import com.criticalsoftware.mobics.presentation.util.DayRestrictionDTO;
import com.criticalsoftware.mobics.proxy.carclub.CarClubWSServiceStub;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.ajax.JavaScriptResolution;
import net.sourceforge.stripes.validation.Validate;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Account action bean
 * 
 * @author ltiago
 * @version $Revision$
 */
@MobiCSSecure
public class AddOnsActionBean extends BaseActionBean {

    private static final Logger LOGGER = Logger.getLogger(AddOnsActionBean.class);

    @Validate(required = true, on = {"detail", "zoneLocation"})
    private String code;

    private AddOnsListDetailsDTO addon;

    /**
     * Add on detail page
     *
     * @return the page resolution
     */
    @DefaultHandler
    @DontValidate
    public Resolution detail() throws RemoteException, UnsupportedEncodingException {
        CarClubWSServiceStub stub = new CarClubWSServiceStub(Configuration.INSTANCE.getCarClubEndpoint());
        stub._getServiceClient().addHeader(AuthenticationUtil.getAuthenticationHeader(
                getContext().getUser().getUsername(), getContext().getUser().getPassword()));
        addon = stub.getAddOnDetails(getContext().getCarClub().getCode(), code);

        return new ForwardResolution("/WEB-INF/addons/addonDetail.jsp");
    }

    @DontValidate
    public Resolution zoneLocation() throws RemoteException, UnsupportedEncodingException {
        return new ForwardResolution("/WEB-INF/map/mapFullWidth.jsp").addParameter("zone", code);
    }

    /**
     * Data for build maps
     *
     * @return a json object containing car data for display in map
     * @throws RemoteException a jax-b webservice exception
     */
    public Resolution zoneData() throws RemoteException, UnsupportedEncodingException {
        // Get the first car
        CarClubWSServiceStub stub = new CarClubWSServiceStub(Configuration.INSTANCE.getCarClubEndpoint());
        stub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));
        getContext().getResponse().setHeader("Stripes-Success", "OK");
        return new JavaScriptResolution(stub.getZoneWithPolygonsByCode(code));
    }

    /**
     * Auxiliar method
     *
     * @return
     */
    public Map<String, List<DayRestrictionDTO>> getRestrictions() {
        Map<String, List<DayRestrictionDTO>> days = null;
        for (CalendarRestrictionsDTO dto : addon.getRestrictions()) {
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

    public void setCode(String code) {
        this.code = code;
    }

    public AddOnsListDetailsDTO getAddon() {
        return addon;
    }

}