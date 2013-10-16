/*
 * $Id: header.java,v 1.1 2011/01/24 14:32:30 pssilva Exp $
 *
 * Copyright (c) Critical Software S.A., All Rights Reserved.
 * (www.criticalsoftware.com)
 *
 * This software is the proprietary information of Critical Software S.A.
 * Use is subject to license terms.
 *
 * Last changed on: $Date: 2011/01/24 14:32:30 $
 * Last changed by: $Author: pssilva $
 */
package com.criticalsoftware.mobics.presentation.security;

/**
 * @author : hm-ferreira
 * @version : $Revision: 1.1 $
 */
public class CarClubSimple {
    private String name;
    private String code;
    private String phone;
    private String email;

    public CarClubSimple(String name, String code, String phone, String email) {
        this.name = name;
        this.code = code;
        this.phone = phone;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getCode() {
        return code;
    }
}
