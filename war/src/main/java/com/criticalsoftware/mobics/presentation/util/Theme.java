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

    DEFAULT("", "map darkblue"),
    GREEN("krakatoa", "map darkgreen"),
    ORANGE("localhost", "map darkred");

    private String host;

    private String style;

    private Theme(String host, String theme) {
        this.host = host;
        this.style = theme;
    }

    public static String getTheme(String host) {
        String theme = DEFAULT.style;
        for (Theme e : Theme.values()) {
            if (e.getHost().equals(host)) {
                theme = e.getStyle();
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
    
    
}
