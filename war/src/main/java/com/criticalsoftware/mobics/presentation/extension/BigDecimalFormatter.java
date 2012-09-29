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

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Locale;

import net.sourceforge.stripes.format.Formatter;

/**
 * @author ltiago
 * @version $Revision: $
 */
public class BigDecimalFormatter implements Formatter<BigDecimal> {

    @Override
    public String format(BigDecimal input) {
        return new MessageFormat("#.###,##").format(input);
    }

    @Override
    public void setFormatType(String formatType) {
        // TODO ltiago: Auto-generated method stub
        
    }

    @Override
    public void setFormatPattern(String formatPattern) {
        // TODO ltiago: Auto-generated method stub
        
    }

    @Override
    public void setLocale(Locale locale) {
        // TODO ltiago: Auto-generated method stub
        
    }

    @Override
    public void init() {
        // TODO ltiago: Auto-generated method stub
        
    }

}
