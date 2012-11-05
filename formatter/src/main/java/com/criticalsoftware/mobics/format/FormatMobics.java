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

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.taglibs.standard.resources.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Formats distance, prices
 * 
 * @author ltiago
 * @version $Revision: $
 */
public class FormatMobics extends BodyTagSupport {

    /** */
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(FormatMobics.class);

    private static final String DISTANCE = "distance";

    private static final String TIME = "time";

    private static final String TIME_WEEK = "timeWeek";

    private static final String CURRENCY_HOUR = "currencyHour";

    private static final String CURRENCY_SYMBOL = "currencySymbol";

    private Object value;
    private String type;
    private ResourceBundle resources;

    public FormatMobics() {
        super();
        init();
    }

    private void init() {
        value = type = null;
        resources = ResourceBundle.getBundle("Resources");
    }

    public int doEndTag() throws JspException {
        String formatted = null;

        if (value == null || value.equals("") || type == null || type.equals("")) {
            LOGGER.warn("One or more required fields are not present");
            return EVAL_PAGE;
        }
        /*
         * If 'value' is a String, it is first parsed into an instance of java.lang.Number
         */

        if (value instanceof String) {
            try {
                if (((String) value).indexOf('.') != -1) {
                    value = BigDecimal.valueOf(Double.valueOf((String) value));
                } else {
                    value = BigDecimal.valueOf(Long.valueOf((String) value));
                }
            } catch (NumberFormatException nfe) {
                throw new JspException(Resources.getMessage("FORMAT_NUMBER_PARSE_ERROR", value), nfe);
            }
        } else if (value instanceof Integer) {
            value = BigDecimal.valueOf(((Integer) value).longValue());
        }

        // If locale is null check for the system property
        Locale locale = null;
        if (System.getProperty("mobics.config.locale") != null) {
            locale = new Locale(System.getProperty("mobics.config.locale"));
        } else {
            locale = pageContext.getRequest().getLocale();
        }

        // If locale is null, use the default locale
        if (locale == null) {
            locale = Locale.getDefault();
        }

        if (locale != null) {
            // Create formatter
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
                    formatPattern = System.getProperty("mobics.config.time.pattern");
                    if (formatPattern == null) {
                        LOGGER.warn("System property «mobics.config.time.pattern» is missing!");
                        return EVAL_PAGE;
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
                    formatPattern = System.getProperty("mobics.config.week.pattern");
                    if (formatPattern == null) {
                        LOGGER.warn("System property «mobics.config.day.week.pattern» is missing!");
                        return EVAL_PAGE;
                    }
                    dateFormatter = new SimpleDateFormat("EEEE", locale);
                    formatted = dateFormatter.format(c.getTime());
                } else {
                    // otherwise
                    formatPattern = System.getProperty("mobics.config.date.pattern");
                    if (formatPattern == null) {
                        LOGGER.warn("System property «mobics.config.date.pattern» is missing!");
                        return EVAL_PAGE;
                    }
                    dateFormatter = new SimpleDateFormat(formatPattern, locale);
                    formatted = dateFormatter.format(c.getTime());
                }

            } else

            /* --------------------- Format distance ------------------- */
            if (DISTANCE.equals(type)) {
                key = "distance.meter";
                formatPattern = System.getProperty("mobics.config.meter.pattern");
                if (formatPattern == null) {
                    LOGGER.warn("System property «mobics.config.meter.pattern» is missing!");
                    return EVAL_PAGE;
                }
                formatter = new DecimalFormat(formatPattern, symbols);

                if (((BigDecimal) value).compareTo(new BigDecimal(0)) == 0
                        || ((BigDecimal) value).compareTo(new BigDecimal(1000)) >= 0) {
                    key = "distance.kilometer";
                    formatPattern = System.getProperty("mobics.config.kilometer.pattern");
                    if (formatPattern == null) {
                        LOGGER.warn("System property «mobics.config.kilometer.pattern» is missing!");
                        return EVAL_PAGE;
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
                formatted = MessageFormat.format(resources.getString(key), minutes);
                if (((BigDecimal) value).intValue() >= 3600) {
                    key = "time.hours";
                    Integer hours = ((BigDecimal) value).intValue() / 3600;
                    formatted = MessageFormat.format(resources.getString(key), hours, minutes);
                }
            } else
                
            /* --------------------- Format currency ------------------- */
            if (CURRENCY_SYMBOL.equals(type)) {
                key = "price.currency";

                formatPattern = System.getProperty("mobics.config.currency.pattern");
                if (formatPattern == null) {
                    LOGGER.warn("System property «mobics.config.currency.pattern» is missing!");
                    return EVAL_PAGE;
                }
                formatter = new DecimalFormat(formatPattern, symbols);
                formatted = MessageFormat.format(resources.getString(key), formatter.format(value));
            } else
                
            /* --------------------- Format currency per hour ------------------- */
            if (CURRENCY_HOUR.equals(type)) {
                key = "price.hour";

                formatPattern = System.getProperty("mobics.config.currency.pattern");
                if (formatPattern == null) {
                    LOGGER.warn("System property «mobics.config.currency.pattern» is missing!");
                    return EVAL_PAGE;
                }
                formatter = new DecimalFormat(formatPattern, symbols);
                formatted = MessageFormat.format(resources.getString(key), formatter.format(value));
            }
        } else {
            // no formatting locale available, use toString()
            formatted = value.toString();
        }

        try {
            pageContext.getOut().print(formatted);
        } catch (IOException ioe) {
            throw new JspTagException(ioe.toString(), ioe);
        }

        return EVAL_PAGE;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

}
