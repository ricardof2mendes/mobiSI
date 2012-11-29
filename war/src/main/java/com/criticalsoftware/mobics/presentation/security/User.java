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

import com.criticalsoftware.mobics.carclub.CarClubSimpleDTO;

/**
 * @author ltiago
 * @version $Revision: $
 */
public class User {

    private String username;
    
    private String password;
    
    private CarClubSimpleDTO carClub;
    
    /**
     * Class constructor
     * 
     * @param username
     * @param carClubName
     */
    public User(String username, String password, CarClubSimpleDTO carClub) {
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
    public CarClubSimpleDTO getCarClub() {
        return carClub;
    }

    /**
     * Set the password
     * 
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
