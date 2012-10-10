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
public enum CarClazz {
    A("A"), B("B"), C("C"), NOT_SPECIFIED("");

    private String clazz;

    /**
     * @param class
     */
    private CarClazz(String clazz) {
        this.clazz = clazz;
    }

    /**
     * @return the clazz
     */
    public String getClazz() {
        return clazz;
    }
}
