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

import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.format.DefaultFormatterFactory;

/**
 * @author ltiago
 * @version $Revision: $
 */
public class MobiCSFormatterFactory extends DefaultFormatterFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(Configuration configuration) throws Exception {
        super.init(configuration);
        add(BigDecimal.class, BigDecimalFormatter.class);
    }

}
