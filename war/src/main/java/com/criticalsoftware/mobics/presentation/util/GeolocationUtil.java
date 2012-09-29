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

import java.io.IOException;
import java.text.MessageFormat;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ltiago
 * @version $Revision: $
 */
public class GeolocationUtil {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GeolocationUtil.class);

    private static final String GEOLOCATION_URL = "http://{0}/reverse?format=json&lat={1}&lon={2}&addressdetails=1";

    private static final String DISPLAY_NAME = "display_name";

    /**
     * Get the full address name
     * 
     * @param latitude
     * @param longitude
     * @return address string
     */
    public static synchronized String getAddressFromCoordinates(String latitude, String longitude) {

        String address = "";

        String url = MessageFormat.format(GEOLOCATION_URL, Configuration.GEOLOCATION_SERVER_NAME, latitude, longitude);

        HttpMethod method = new GetMethod(url);

        try {
            if(LOGGER.isTraceEnabled()){
                LOGGER.trace("Making reverse geolocation call to: {}", url);
            }
            int statusCode = new HttpClient().executeMethod(method);

            if (statusCode == HttpStatus.SC_OK) {
                byte[] responseBody = method.getResponseBody();
                JSONObject jsonObject = (JSONObject) new JSONParser().parse(new String(responseBody));
                if(LOGGER.isTraceEnabled()){
                    LOGGER.trace(jsonObject.toJSONString());
                }
                address = (String) jsonObject.get(DISPLAY_NAME);
            } else {
              LOGGER.warn("Recived a HTTP status {}. Response was not good from {}", statusCode, url);  
            }
        } catch (HttpException e) {
            LOGGER.error("Error while making call.", e);
        } catch (IOException e) {
            LOGGER.error("Error while reading the response.", e);
        } catch (ParseException e) {
            LOGGER.error("Error while parsing json response.", e);
        }

        return address;
    }

}
