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

    private String carClubName;
    
    private String carClubCode;
    
    private String carClubColor;
    
    private String carClubTheme;
    
    /**
     * Class constructor
     * 
     * @param username
     * @param carClubName
     */
    public User(String username, String password, String carClubName, String carClubCode, String carClubColor, String carClubTheme) {
        this.username = username;
        this.password = password;
        this.carClubName = carClubName;
        this.carClubCode = carClubCode;
        this.carClubColor = carClubCode;
        this.carClubTheme = this.carClubTheme;
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
     * @return the carClubName
     */
    public String getCarClubName() {
        return carClubName;
    }

    /**
     * @return the carClubColor
     */
    public String getCarClubColor() {
        return carClubColor;
    }

    /**
     * @return the carClubCode
     */
    public String getCarClubCode() {
        return carClubCode;
    }

    /**
     * @return the carClubTheme
     */
    public String getCarClubTheme() {
        return carClubTheme;
    }
}
