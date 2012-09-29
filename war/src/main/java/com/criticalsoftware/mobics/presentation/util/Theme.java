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
package com.criticalsoftware.mobics.presentation.util;

/**
 * @author ltiago
 * @version $Revision: $
 */
public enum Theme {

    DEFAULT("", "", "red"),
    GREEN("", "krakatoa", "green"),
    ORANGE("", "localhost", "orange");

    private String carClub;

    private String host;

    private String theme;

    private Theme(String carClub, String host, String theme) {
        this.carClub = carClub;
        this.host = host;
        this.theme = theme;
    }

    public static String getTheme(String host, String carClub) {
        String theme = DEFAULT.theme;
        boolean weHaveHost = false;
        for (Theme e : Theme.values()) {
            if (e.getHost().equals(host)) {
                theme = e.getTheme();
                weHaveHost = true;
                break;
            }
        }

        if (!weHaveHost && carClub != null) {
            for (Theme e : Theme.values()) {
                if (e.getCarClub().equals(carClub)) {
                    theme = e.getTheme();
                    break;
                }
            }
        }

        return theme;
    }

    /**
     * @return the carClub
     */
    public String getCarClub() {
        return carClub;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @return the theme
     */
    public String getTheme() {
        return theme;
    }
}
