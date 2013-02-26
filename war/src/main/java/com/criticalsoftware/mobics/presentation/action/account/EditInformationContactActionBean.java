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
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.ObjectTypeConverter;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;
import net.sourceforge.stripes.validation.ValidationState;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang.StringUtils;

import com.criticalsoftware.mobics.customer.EditCustomerDTO;
import com.criticalsoftware.mobics.customer.CountryDTO;
import com.criticalsoftware.mobics.presentation.security.AuthenticationUtil;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.util.Configuration;
import com.criticalsoftware.mobics.proxy.customer.CustomerEditionExceptionException;
import com.criticalsoftware.mobics.proxy.customer.CustomerNotFoundExceptionException;
import com.criticalsoftware.mobics.proxy.customer.CustomerServiceExceptionException;
import com.criticalsoftware.mobics.proxy.customer.CustomerWSServiceStub;

/**
 * Account action bean
 * 
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class EditInformationContactActionBean extends AskPinActionBean {

    @Validate(required = true, on = "saveData")
    private String address;

    @Validate(required = true, on = "saveData")
    private String locality;

    @Validate(required = true, on = "saveData", mask = "^[0-9]{4}-[0-9]{3}$")
    private String zipCode1;

    @Validate(required = true, on = "saveData", minlength = 9, maxlength = 9)
    private String phoneNumber;

    @Validate(required = true, on = "saveData")
    private String countryCode;

    @Validate(required = true, on = "saveData")
    private String fullName;

    @Validate(required = true, on = "saveData")
    private String taxNumber;

    @Validate(converter = ObjectTypeConverter.class)
    private CountryDTO[] countries;

    /**
     * Account page
     * 
     * @return the page resolution
     */
    @DefaultHandler
    @DontValidate
    public Resolution main() {
        return new ForwardResolution("/WEB-INF/account/askPin.jsp");
    }

    /**
     * Information page
     * 
     * @return the page resolution
     * @throws RemoteException
     * @throws CustomerNotFoundExceptionException
     * @throws UnsupportedEncodingException
     */
    public Resolution data() throws RemoteException, CustomerNotFoundExceptionException, UnsupportedEncodingException {

        CustomerWSServiceStub customerWSServiceStub = new CustomerWSServiceStub(
                Configuration.INSTANCE.getCustomerEndpoint());
        customerWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));

        EditCustomerDTO customer = customerWSServiceStub.getCustomerDetails(getContext().getLocale().getLanguage());

        if (customer != null) {
            this.address = customer.getAddress();
            if (customer.getCountry() != null) {
                this.countryCode = customer.getCountry().getCode();
            }
            this.fullName = customer.getFullName();
            this.locality = customer.getLocality();
            this.phoneNumber = customer.getPhoneNumber();
            this.taxNumber = customer.getTaxNumber();
            this.zipCode1 = customer.getZipCode1().concat("-").concat(customer.getZipCode2());
        }

        return new ForwardResolution("/WEB-INF/account/editInformationContact.jsp");
    }

    public Resolution saveData() throws RemoteException, CustomerNotFoundExceptionException,
            UnsupportedEncodingException, CustomerEditionExceptionException, CustomerServiceExceptionException {

        CustomerWSServiceStub customerWSServiceStub = new CustomerWSServiceStub(
                Configuration.INSTANCE.getCustomerEndpoint());
        customerWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));

        String zip = zipCode1.substring(0, zipCode1.indexOf('-'));
        String code = zipCode1.substring(zipCode1.indexOf('-') + 1, zipCode1.length());

        customerWSServiceStub.updateCustomerDetails(address, locality, zip, code, phoneNumber, countryCode, fullName,
                taxNumber, null);

        getContext().getMessages().add(new LocalizableMessage("account.information.edit.success"));
        return new RedirectResolution(AccountActionBean.class).flash(this);
    }

    @ValidationMethod(on = "saveData", when = ValidationState.NO_ERRORS)
    public void validate(ValidationErrors errors) {
        if (!isValidNIF(taxNumber)) {
            errors.add("taxNumber", new LocalizableError("account.information.taxNumber.invalid"));
        }
    }

    /**
     * Validates the NIF
     * 
     * @param nif
     * @return true if valid
     */
    private boolean isValidNIF(String nif) {
        // Verifica se é nulo, se é numérico e se tem 9 dígitos
        if (nif != null && StringUtils.isNumeric(nif) && nif.length() == 9) {
            // Obtem o primeiro número do NIF
            char c = nif.charAt(0);
            // Verifica se o primeiro número é (1, 2, 5, 6, 8 ou 9)
            if (c == '1' || c == '2' || c == '5' || c == '6' || c == '8' || c == '9') {
                // Calculo do Digito de Controle
                int checkDigit = c * 9;
                for (int i = 2; i <= 8; i++) {
                    checkDigit += nif.charAt(i - 1) * (10 - i);
                }
                checkDigit = 11 - (checkDigit % 11);

                // Se o digito de controle é maior que dez, coloca-o a zero
                if (checkDigit >= 10)
                    checkDigit = 0;

                // Compara o digito de controle com o último numero do NIF
                // Se igual, o NIF é válido.
                if (Character.forDigit(checkDigit, 10) == nif.charAt(8))
                    return true;
            }
        }
        return false;
    }

    /**
     * get countries
     * 
     * @return countries dto
     * @throws UnsupportedEncodingException
     * @throws CustomerNotFoundExceptionException
     * @throws RemoteException
     */
    public CountryDTO[] getCountries() throws UnsupportedEncodingException, RemoteException,
            CustomerNotFoundExceptionException {
        countries = new CountryDTO[1];
        CustomerWSServiceStub customerWSServiceStub = new CustomerWSServiceStub(
                Configuration.INSTANCE.getCustomerEndpoint());
        customerWSServiceStub._getServiceClient().addHeader(
                AuthenticationUtil.getAuthenticationHeader(getContext().getUser().getUsername(), getContext().getUser()
                        .getPassword()));
        EditCustomerDTO customer = customerWSServiceStub.getCustomerDetails(getContext().getLocale().getLanguage());
        if (customer != null) {
            countries[0] = customer.getCountry();
        }

        return countries;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the locality
     */
    public String getLocality() {
        return locality;
    }

    /**
     * @param locality the locality to set
     */
    public void setLocality(String locality) {
        this.locality = locality;
    }

    /**
     * @return the zipCode1
     */
    public String getZipCode1() {
        return zipCode1;
    }

    /**
     * @param zipCode1 the zipCode1 to set
     */
    public void setZipCode1(String zipCode1) {
        this.zipCode1 = zipCode1;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * @param countryCode the countryCode to set
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return the taxNumber
     */
    public String getTaxNumber() {
        return taxNumber;
    }

    /**
     * @param taxNumber the taxNumber to set
     */
    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

}
