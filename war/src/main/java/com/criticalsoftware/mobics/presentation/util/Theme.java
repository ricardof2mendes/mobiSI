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

    DEFAULT("", "solid", "darkblue"),
    GREEN("krakatoa", "map", "darkgreen"),
    ORANGE("localhost", "warm", "darkred");

    public static final String WARM = "warm";
    
    private String host;
    
    private String style;
    
    private String color;

    private Theme(String host, String style, String color) {
        this.host = host;
        this.style = style;
        this.color = color;
    }

    public static String getTheme(String host) {
        String theme = DEFAULT.style.concat(" ").concat(DEFAULT.color);
        for (Theme e : Theme.values()) {
            if (e.getHost().equals(host)) {
                theme = e.getStyle().concat(" ").concat(e.getColor());
                break;
            }
        }
        return theme;
    }
    
    public static String getColor(String host) {
        String theme = DEFAULT.color;
        for (Theme e : Theme.values()) {
            if (e.getHost().equals(host)) {
                theme = e.getColor();
                break;
            }
        }
        return theme;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @return the style
     */
    public String getStyle() {
        return style;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }
}
