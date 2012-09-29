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
package com.criticalsoftware.mobics.presentation.security;

/**
 * @author ltiago
 * @version $Revision: $
 */
public class User {

    private String username;
    
    private String password;

    private String carClub;

    /**
     * Class constructor
     * 
     * @param username
     * @param carClub
     */
    public User(String username, String password, String carClub) {
        this.username = username;
        this.password = password;
        this.carClub = carClub;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the carClub
     */
    public String getCarClub() {
        return carClub;
    }

}
