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

import java.util.Locale;
import java.util.ResourceBundle;

import com.criticalsoftware.mobics.fleet.CarDTO;
import com.criticalsoftware.mobics.format.FormatUtils;

/**
 * DTO to be used in javascript It has values allready formatted to be displayed directlly on content
 * 
 * @author ltiago
 * @version $Revision: $
 */
public class CarFormattedDTO extends CarDTO {

    /** */
    private static final long serialVersionUID = 6204466593688687576L;

    private String formattedPrice;

    private String formattedDistance;

    private String formattedFuel;

    /**
     * 
     */
    public CarFormattedDTO(CarDTO carDTO, Locale locale) {
        super();

        super.setLatitude(carDTO.getLatitude());
        super.setLongitude(carDTO.getLongitude());
        super.setCarBrandName(carDTO.getCarBrandName());
        super.setCarModelName(carDTO.getCarModelName());
        super.setLicensePlate(carDTO.getLicensePlate());
        super.setAddOns(carDTO.getAddOns());
        super.setPromotions(carDTO.getPromotions());

        this.formattedPrice = carDTO.getPriceInUse() != null ? FormatUtils.format(FormatUtils.CURRENCY_HOUR,
                carDTO.getPriceInUse(), locale) : "";
        this.formattedDistance = carDTO.getDistance() != null ? FormatUtils.format(FormatUtils.DISTANCE, carDTO
                .getDistance(), locale) : "";
        this.formattedFuel = ResourceBundle.getBundle("StripesResources", locale).getString(
                "FuelType." + carDTO.getFuelType().getName());
    }

    /**
     * @return the formattedPrice
     */
    public String getFormattedPrice() {
        return formattedPrice;
    }

    /**
     * @return the formattedDistance
     */
    public String getFormattedDistance() {
        return formattedDistance;
    }

    /**
     * @return the formattedFuel
     */
    public String getFormattedFuel() {
        return formattedFuel;
    }
}
