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
package com.criticalsoftware.mobics.presentation.action.preferences;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import com.criticalsoftware.mobics.customer.CustomerColumnPreferenceDTO;
import com.criticalsoftware.mobics.customer.CustomerPreferencesDTO;
import com.criticalsoftware.mobics.miscellaneous.CountryDTO;
import com.criticalsoftware.mobics.presentation.action.BaseActionBean;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.proxy.customer.CustomerNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.customer.CustomerWSServiceStub;
import com.criticalsoftware.mobics.proxy.miscellaneous.MiscellaneousWSServiceStub;

/**
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class EditPreferencesActionBean extends BaseActionBean {

    private static final String EMAIL = "EMAIL";

    private static final String SMS = "SMS";

    /**
     * Sorting columns
     */
    @Validate(required = true, on = "save")
    private String[] columnNames = new String[3];

    @Validate(required = true, on = "save")
    private boolean[] sortAsc = new boolean[3];

    @Validate(required = true, on = "save")
    private int searchRadius;

    @Validate(required = true, on = "save")
    private String communicationChannel;

    @Validate(required = true, on = "save")
    private String[] communicationChannels = new String[3];

    @Validate(required = true, on = "save")
    private int timeToStartSending;

    @Validate(required = true, on = "save")
    private int timeToStopSending;

    @Validate(required = true, on = "save", maxvalue = 99, maxlength = 2)
    private int numberOfNotifications;

    @Validate(required = true, on = "save")
    private String language;

    @DefaultHandler
    @DontValidate
    public Resolution main() throws RemoteException, UnsupportedEncodingException, CustomerNotFoundExceptionException {
        CustomerWSServiceStub customerWSServiceStub = new CustomerWSServiceStub(
                Configuration.INSTANCE.getCustomerEndpoint());
        customerWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));
        CustomerPreferencesDTO customerPreferencesDTO = customerWSServiceStub.getCustomerPreferences();

        // update user preferences in session
        getContext().getUser().setCustomerPreferencesDTO(customerPreferencesDTO);

        if (customerPreferencesDTO != null) {

            columnNames = new String[3];
            sortAsc = new boolean[3];
            int i = 0;
            for (CustomerColumnPreferenceDTO column : customerPreferencesDTO.getFleetOrder()) {
                columnNames[i] = column.getColumnName();
                sortAsc[i++] = column.getSortAsc();
            }

            communicationChannels = new String[3];
            String[] comms = customerPreferencesDTO.getCommunicationChannels();
            for (String commChannel : comms) {
                if (EMAIL.equals(commChannel)) {
                    communicationChannels[0] = commChannel;
                } else if (SMS.equals(commChannel)) {
                    communicationChannels[1] = commChannel;
                } else {
                    communicationChannels[2] = commChannel;
                }
            }

            searchRadius = customerPreferencesDTO.getSearchRadius();
            communicationChannel = customerPreferencesDTO.getCommunicationChannel();
            timeToStartSending = customerPreferencesDTO.getTimeToStartSending();
            timeToStopSending = customerPreferencesDTO.getTimeToStopSending();
            numberOfNotifications = customerPreferencesDTO.getNumberOfNotifications();
            language = customerPreferencesDTO.getLanguage();
        }

        return new ForwardResolution("/WEB-INF/preferences/editPreferences.jsp");
    }

    /**
     * Save
     * 
     * @return
     * @throws RemoteException
     * @throws UnsupportedEncodingException
     * @throws CustomerNotFoundExceptionException
     */
    public Resolution save() throws RemoteException, UnsupportedEncodingException, CustomerNotFoundExceptionException {
        CustomerWSServiceStub customerWSServiceStub = new CustomerWSServiceStub(
                Configuration.INSTANCE.getCustomerEndpoint());
        customerWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));

        customerWSServiceStub.setCustomerPreferences(columnNames, sortAsc, getContext().getUser()
                .getCustomerPreferencesDTO().getSearchPosition().getLatitude().toString(), getContext().getUser()
                .getCustomerPreferencesDTO().getSearchPosition().getLongitude().toString(), searchRadius,
                communicationChannel, communicationChannels, timeToStartSending, timeToStopSending,
                numberOfNotifications, language);

        // update user preferences in session
        getContext().getUser().setCustomerPreferencesDTO(customerWSServiceStub.getCustomerPreferences());

        getContext().getMessages().add(new LocalizableMessage("preferences.updated.success"));
        return new RedirectResolution(this.getClass()).flash(this);

    }

    /**
     * @return the columnNames
     * @throws RemoteException
     */
    public CountryDTO[] getLanguages() throws RemoteException {
        return new MiscellaneousWSServiceStub(Configuration.INSTANCE.getMiscellaneousEnpoint())
                .getAllCountries(getContext().getUser().getCustomerPreferencesDTO().getLanguage());
    }

    /**
     * @return the columnNames
     */
    public String[] getColumnNames() {
        return columnNames;
    }

    /**
     * @param columnNames the columnNames to set
     */
    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    /**
     * @return the sortAsc
     */
    public boolean[] getSortAsc() {
        return sortAsc;
    }

    /**
     * @param sortAsc the sortAsc to set
     */
    public void setSortAsc(boolean[] sortAsc) {
        this.sortAsc = sortAsc;
    }

    /**
     * @return the searchRadius
     */
    public int getSearchRadius() {
        return searchRadius;
    }

    /**
     * @param searchRadius the searchRadius to set
     */
    public void setSearchRadius(int searchRadius) {
        this.searchRadius = searchRadius;
    }

    /**
     * @return the communicationChannel
     */
    public String getCommunicationChannel() {
        return communicationChannel;
    }

    /**
     * @param communicationChannel the communicationChannel to set
     */
    public void setCommunicationChannel(String communicationChannel) {
        this.communicationChannel = communicationChannel;
    }

    /**
     * @return the communicationChannels
     */
    public String[] getCommunicationChannels() {
        return communicationChannels;
    }

    /**
     * @param communicationChannels the communicationChannels to set
     */
    public void setCommunicationChannels(String[] communicationChannels) {
        this.communicationChannels = communicationChannels;
    }

    /**
     * @return the timeToStartSending
     */
    public int getTimeToStartSending() {
        return timeToStartSending;
    }

    /**
     * @param timeToStartSending the timeToStartSending to set
     */
    public void setTimeToStartSending(int timeToStartSending) {
        this.timeToStartSending = timeToStartSending;
    }

    /**
     * @return the timeToStopSending
     */
    public int getTimeToStopSending() {
        return timeToStopSending;
    }

    /**
     * @param timeToStopSending the timeToStopSending to set
     */
    public void setTimeToStopSending(int timeToStopSending) {
        this.timeToStopSending = timeToStopSending;
    }

    /**
     * @return the numberOfNotifications
     */
    public int getNumberOfNotifications() {
        return numberOfNotifications;
    }

    /**
     * @param numberOfNotifications the numberOfNotifications to set
     */
    public void setNumberOfNotifications(int numberOfNotifications) {
        this.numberOfNotifications = numberOfNotifications;
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }
}
