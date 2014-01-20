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
package com.criticalsoftware.mobics.format;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ltiago
 * @version $Revision: $
 */
public class FormatUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormatUtils.class);

    public static final String DISTANCE = "distance";

    public static final String TIME = "time";
    
    public static final String MILLISECONDS = "milliseconds";

    public static final String TIME_WEEK = "timeWeek";

    public static final String CURRENCY_HOUR = "currencyHour";

    public static final String CURRENCY_SYMBOL = "currencySymbol";

    public static final String PERCENTAGE = "percentage";

    public static String format(String type, Object value, Locale locale, Map<String, String> patterns) {
        String formatted = null;
        // Create formatter
        ResourceBundle resources = ResourceBundle.getBundle("Resources", locale);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        NumberFormat formatter;
        DateFormat dateFormatter;
        String formatPattern;
        String key;

        /* --------------------- Format week ------------------- */
        if (TIME_WEEK.equals(type)) {
            key = "time.yesterday";

            Calendar c = (Calendar) value;
            Calendar today = Calendar.getInstance(), yesterday = Calendar.getInstance();
            yesterday.add(Calendar.DAY_OF_WEEK, -1);

            // if today
            if (today.get(Calendar.DAY_OF_WEEK) == c.get(Calendar.DAY_OF_WEEK)) {
                formatPattern = patterns.get("mobics.config.time.pattern");
                if (formatPattern == null) {
                    LOGGER.warn("Pattern «mobics.config.time.pattern» is missing!");
                    return null;
                }
                dateFormatter = new SimpleDateFormat(formatPattern, locale);
                formatted = dateFormatter.format(c.getTime());
            } else
            // if yesterday
            if (yesterday.get(Calendar.DAY_OF_WEEK) == c.get(Calendar.DAY_OF_WEEK)) {
                formatted = MessageFormat.format(resources.getString(key), c.getTime());
            } else
            // this week
            if (yesterday.get(Calendar.WEEK_OF_YEAR) == c.get(Calendar.WEEK_OF_YEAR)) {
                formatPattern = patterns.get("mobics.config.week.pattern");
                if (formatPattern == null) {
                    LOGGER.warn("Pattern «mobics.config.week.pattern» is missing!");
                    return null;
                }
                dateFormatter = new SimpleDateFormat(formatPattern, locale);
                formatted = dateFormatter.format(c.getTime());
            } else {
                // otherwise
                formatPattern = patterns.get("mobics.config.date.pattern");
                if (formatPattern == null) {
                    LOGGER.warn("Pattern «mobics.config.date.pattern» is missing!");
                    return null;
                }
                dateFormatter = new SimpleDateFormat(formatPattern, locale);
                formatted = dateFormatter.format(c.getTime());
            }

        } else

        /* --------------------- Format distance ------------------- */
        if (DISTANCE.equals(type)) {
            key = "distance.meter";
            formatPattern = patterns.get("mobics.config.meter.string.pattern");
            if (formatPattern == null) {
                LOGGER.warn("Pattern «mobics.config.meter.pattern» is missing!");
                return null;
            }
            formatter = new DecimalFormat(formatPattern, symbols);

            if (((BigDecimal) value).compareTo(new BigDecimal(0)) == 0
                    || ((BigDecimal) value).compareTo(new BigDecimal(1000)) >= 0) {
                key = "distance.kilometer";
                formatPattern = patterns.get("mobics.config.kilometer.string.pattern");
                if (formatPattern == null) {
                    LOGGER.warn("Pattern «mobics.config.kilometer.pattern» is missing!");
                    return null;
                }
                formatter = new DecimalFormat(formatPattern, symbols);
                value = ((BigDecimal) value).multiply(new BigDecimal(0.001));
            }
            formatted = MessageFormat.format(resources.getString(key), formatter.format(value));
        } else

        /* --------------------- Format time ------------------- */
        if (TIME.equals(type)) {
            key = "time.minutes";
            Integer minutes = (((BigDecimal) value).intValue() % 3600) / 60;
            if (((BigDecimal) value).intValue() >= 3600) {
                Integer hours = ((BigDecimal) value).intValue() / 3600;
                if (new Integer(0).equals(minutes)) {
                    key = hours == 1 ? "time.hour" : "time.hours";
                } else {
                    key = "time.hours.minutes";
                }
                formatted = MessageFormat.format(resources.getString(key), hours, minutes);
            } else {
                formatted = MessageFormat.format(resources.getString(key), minutes);
            }
        } else

        /* --------------------- Format currency ------------------- */
        if (CURRENCY_SYMBOL.equals(type)) {
            key = "price.currency";
            String currencySymbol = patterns.get("mobics.config.currency.symbol");
            formatPattern = patterns.get("mobics.config.currency.pattern");
            if (formatPattern == null) {
                LOGGER.warn("Pattern «mobics.config.currency.pattern» is missing!");
                return null;
            }
            formatter = new DecimalFormat(formatPattern, symbols);
            formatted = MessageFormat.format(resources.getString(key), formatter.format(value), currencySymbol);
        } else

        /* --------------------- Format currency per hour ------------------- */
        if (CURRENCY_HOUR.equals(type)) {
            key = "price.hour";
            String currencySymbol = patterns.get("mobics.config.currency.symbol");
            formatPattern = patterns.get("mobics.config.currency.pattern");
            if (formatPattern == null) {
                LOGGER.warn("Pattern «mobics.config.currency.pattern» is missing!");
                return null;
            }
            formatter = new DecimalFormat(formatPattern, symbols);
            formatted = MessageFormat.format(resources.getString(key), formatter.format(value), currencySymbol);
        } else 
        /* ----------------- Format miliseconds to seconds or minutes -------------------*/
        if (MILLISECONDS.equals(type)){
            key = "time.seconds";
            Integer seconds = ((BigDecimal) value).intValue() / 1000;
            if (seconds >= 60) {
                Integer minutes = seconds / 60;
                seconds = seconds % 60;
                if (seconds == 0) {
                    key = "time.minutes";
                } else {
                    key = "time.minutes.seconds";
                }
                formatted = MessageFormat.format(resources.getString(key), minutes, seconds);
            } else {
                formatted = MessageFormat.format(resources.getString(key), seconds);                
            }
            
        } else
        if(PERCENTAGE.equals(type)) {
            formatted = MessageFormat.format(resources.getString("value.percentage"),
                                             ((BigDecimal) value).doubleValue() * 100);
        }

        return formatted;
    }

}
