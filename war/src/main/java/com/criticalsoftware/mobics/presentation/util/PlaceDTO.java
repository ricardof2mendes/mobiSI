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

import java.io.Serializable;

/**
 * @author ltiago
 * @version $Revision: $
 */
public class PlaceDTO implements Serializable{

    /** */
    private static final long serialVersionUID = 1026361261903941301L;

    private String displayName;
    
    private String latitude;
    
    private String longitude;

    /**
     * @param displayName
     * @param latitude
     * @param longitude
     */
    public PlaceDTO(String displayName, String latitude, String longitude) {
        this.displayName = displayName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return the latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * @return the longitude
     */
    public String getLongitude() {
        return longitude;
    }
    
}
