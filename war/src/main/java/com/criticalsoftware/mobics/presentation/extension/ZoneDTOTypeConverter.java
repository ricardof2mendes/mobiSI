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

import java.util.Collection;

import net.sourceforge.stripes.validation.ObjectTypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import com.criticalsoftware.mobics.fleet.ZoneDTO;

/**
 * @author ltiago
 * @version $Revision: $
 */
public class ZoneDTOTypeConverter extends ObjectTypeConverter {

    /**
     * {@inheritDoc}
     */
    @Override
    public Object convert(String input, Class<? extends Object> targetType, Collection<ValidationError> errors) {

        ZoneDTO[] zones = new ZoneDTO[1];
        ZoneDTO zone  = new ZoneDTO();
        zone.setZone(input);
        zones[0] = zone;
        
        return zones;
    }

}
