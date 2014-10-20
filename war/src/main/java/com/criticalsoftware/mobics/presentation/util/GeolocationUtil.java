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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.simple.JSONArray;
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

    private static final String GEOLOCATION_URL = "http://{0}/reverse?format=json&lat={1}&lon={2}&zoom=17&addressdetails=1";
    private static final String SEARCH_URL = "http://{0}/search?format=json&q={1}&countrycodes={2}&zoom=17&addressdetails=1&limit={3}";

    private static final String DISPLAY_NAME = "display_name";
    private static final String LATITUDE_NAME = "lat";
    private static final String LONGITUDE_NAME = "lon";

    private static String getFormattedAddress(JSONObject jsonObject){
        String result = null;

        if(jsonObject != null && jsonObject.containsKey("address")){
            result = "";
            JSONObject address = (JSONObject) jsonObject.get("address");
            if (address.containsKey("road")) {
                result += address.get("road").toString();
            }

            if (result.isEmpty() && address.containsKey("path")) {
                result += address.get("path").toString();
            }

            if (result.isEmpty() && address.containsKey("cycleway")) {
                result += address.get("cycleway").toString();
            }

            if (address.containsKey("house_number")) {
                if(result.isEmpty() == false){
                    result += " ";
                }
                result += address.get("house_number").toString();
            }

        }

        return result;
    }

    /**
     * Get the full address name
     *
     * @param latitude
     * @param longitude
     * @return address string
     */
    public static String getAddressFromCoordinates(String latitude, String longitude) {

        String address = null;

        String url = MessageFormat.format(GEOLOCATION_URL, Configuration.INSTANCE.getGeolocationServer(), latitude,
                longitude);

        HttpMethod method = new GetMethod(url);

        try {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Making reverse geolocation call to: {}", url);
            }
            int statusCode = new HttpClient().executeMethod(method);

            if (statusCode == HttpStatus.SC_OK) {
                byte[] responseBody = readResponse(method);
                JSONObject jsonObject = (JSONObject) new JSONParser().parse(new String(responseBody));
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace(jsonObject.toJSONString());
                }
                address = getFormattedAddress(jsonObject);
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

    public static List<PlaceDTO> getAddressFromText(String address) throws UnsupportedEncodingException {

        List<PlaceDTO> results = new ArrayList<PlaceDTO>();

        address = URLEncoder.encode(address, Configuration.INSTANCE.getUriEnconding());

        String url = MessageFormat.format(SEARCH_URL, Configuration.INSTANCE.getGeolocationServer(), address,
                Configuration.INSTANCE.getGeolocationServerAllowedCountries(), Configuration.INSTANCE.getMaxResults());
        HttpMethod method = new GetMethod(url);
        try {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Making search location call to: {}", url);
            }
            int statusCode = new HttpClient().executeMethod(method);

            if (statusCode == HttpStatus.SC_OK) {
                byte[] responseBody = readResponse(method);
                JSONArray jsonArray = (JSONArray) new JSONParser().parse(new String(responseBody));
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace(jsonArray.toJSONString());
                }

                @SuppressWarnings("unchecked")
                Iterator<JSONObject> it = jsonArray.iterator();
                while (it.hasNext()) {
                    JSONObject place = it.next();
                    results.add(new PlaceDTO((String) place.get(DISPLAY_NAME), (String) place.get(LATITUDE_NAME),
                            (String) place.get(LONGITUDE_NAME)));
                }

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

        return results;
    }

    private static byte[] readResponse(HttpMethod method) throws IOException {
        InputStream is = method.getResponseBodyAsStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        byte[] responseBody = buffer.toByteArray();
        return responseBody;
    }

}
