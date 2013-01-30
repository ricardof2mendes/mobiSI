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
 * The car state 
 * There are several states, 
 * INCOMPLETE, UNAVAILABLE, AVAILABLE, BOOKED, IN_USE, ENGINE_RUNNING, DEACTIVATED
 * but only the ones needed are listed
 * 
 * @author ltiago
 * @version $Revision: $
 */
public enum CarState {
    AVAILABLE, BOOKED, IN_USE, ENGINE_RUNNING
}
