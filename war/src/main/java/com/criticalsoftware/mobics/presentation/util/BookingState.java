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
 * The booking state
 * There are serveral states,
 * BLANK, WAIT_OBS_IMMEDIATE, WAIT_OBS_ADVANCE, OBS_ERROR, BOOKED, IN_USE, ON_TRIP, UNDER_REVIEW, CLOSED, CANCELED, INVOICED
 * but only the ones needed are listed
 * 
 * @author ltiago
 * @version $Revision: $
 */
public enum BookingState {
    WAIT_OBS_IMMEDIATE, WAIT_OBS_ADVANCE, BOOKED, IN_USE, OBS_ERROR, ON_TRIP
}
