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
public enum FuelType {
    PETROL("PETROL"), DIESEL("DIESEL"), GPL("GPL"), HYBRID("HYBRID"), ELECTRIC("ELECTRIC"), NOT_SPECIFIED("");
    
    private String type;

    /**
     * @param type
     */
    private FuelType(String type) {
        this.type = type;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
}
