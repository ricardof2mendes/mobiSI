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
package com.criticalsoftware.mobics.presentation.extension;

import net.sourceforge.stripes.validation.DateTypeConverter;

import com.criticalsoftware.mobics.presentation.util.Configuration;

/**
 * Mobics date time converter
 * 
 * @author ltiago
 * @version $Revision: $
 */
public class DatetimeTypeConverter extends DateTypeConverter {

    /**
     * Does not do nothing to the input string
     * 
     * {@inheritDoc}
     */
    @Override
    protected String preProcessInput(String string) {
        return string;
    }

    /**
     * Return the format string
     * {@inheritDoc}
     */
    @Override
    protected String[] getFormatStrings() {
        return new String[] { Configuration.INSTANCE.getDateTimePattern() };
    }
}
