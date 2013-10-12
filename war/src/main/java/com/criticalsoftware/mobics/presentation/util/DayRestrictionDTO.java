/*
 * Id: $Id$
 *
 * Copyright (c) Critical Software S.A., All Rights Reserved.
 * (www.criticalsoftware.com)
 *
 * This software is the proprietary information of Critical Software S.A.
 * Use is subject to license terms.
 *
 * Last changed on : $Date$
 * Last changed by : $Author$
 */
package com.criticalsoftware.mobics.presentation.util;

import com.criticalsoftware.mobics.carclub.CalendarRestrictionsDTO;
import com.criticalsoftware.mobics.carclub.DayRestrictionsDTO;
import com.criticalsoftware.mobics.carclub.DaysOfWeekEnum;

import java.util.*;

/**
 * @author :    Luis Rico
 * @version :   $Rev$
 */

public class DayRestrictionDTO {
    private String type;
    private Calendar startDate;
    private Calendar endDate;

    public DayRestrictionDTO(String type, Calendar startDate, Calendar endDate){
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }
}

