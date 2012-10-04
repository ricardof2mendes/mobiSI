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

import com.criticalsoftware.mobics.fleet.CoordinateDTO;
import com.criticalsoftware.mobics.fleet.ZoneWithPolygonDTO;

/**
 * @author ltiago
 * @version $Revision: $
 */
public class CoordinateZonesDTO {
    private CoordinateDTO coordinate;
    
    private ZoneWithPolygonDTO[] zones;
    
    public CoordinateZonesDTO(CoordinateDTO coordinate, ZoneWithPolygonDTO[] zones) {
        this.coordinate=coordinate;
        this.zones = zones;
    }

    /**
     * @return the coordinate
     */
    public CoordinateDTO getCoordinate() {
        return coordinate;
    }

    /**
     * @return the zones
     */
    public ZoneWithPolygonDTO[] getZones() {
        return zones;
    }
}
