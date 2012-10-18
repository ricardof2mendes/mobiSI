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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.taglibs.standard.resources.Resources;

/**
 * @author ltiago
 * @version $Revision: $
 */
public class FormatMobics extends BodyTagSupport {

    /** */
    private static final long serialVersionUID = 1L;

    private static final String DISTANCE = "distance";

    private Object value; // 'value' attribute
    private String type; // 'type' attribute
    private String pattern; // 'pattern' attribute
    private String pattern2;
    private String locale;
    private ResourceBundle resources;

    public FormatMobics() {
        super();
        init();
    }

    private void init() {
        value = type = pattern = locale = null;
        resources = ResourceBundle.getBundle("Resources");
    }

    public int doEndTag() throws JspException {
        String formatted = null;

        if ((value == null) || value.equals("") || type == null || type.equals("") || pattern == null
                || pattern.equals("")) {
            return EVAL_PAGE;
        }

        /*
         * If 'value' is a String, it is first parsed into an instance of java.lang.Number
         */
        if (value instanceof String) {
            try {
                if (((String) value).indexOf('.') != -1) {
                    value = Double.valueOf((String) value);
                } else {
                    value = Long.valueOf((String) value);
                }
            } catch (NumberFormatException nfe) {
                throw new JspException(Resources.getMessage("FORMAT_NUMBER_PARSE_ERROR", value), nfe);
            }
        }

        // If locale is null check for the system property
        Locale locale = null;
        if (this.locale != null) {
            locale = new Locale(this.locale);
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
            NumberFormat formatter = new DecimalFormat(pattern, symbols);
            String key = "price.hour";
            
            if(DISTANCE.equals(type)) {
                key = "distance.meter";
                formatter = new DecimalFormat(pattern2, symbols);
                if(value instanceof Double) {
                    if((Double) value > 1000){
                        key = "distance.kilometer";
                        value = (Double) value * 0.001; 
                    }
                } else if(value instanceof Long){
                    if((Long) value > 1000){
                        key = "distance.kilometer";
                        value = (Long) value * 0.001; 
                    }
                } else {
                    if(((BigDecimal) value).compareTo(new BigDecimal(1000)) >= 0) {
                        key = "distance.kilometer";
                        value = ((BigDecimal) value).multiply(new BigDecimal(0.001));
                    }
                }
            }
            
            formatted = MessageFormat.format(resources.getString(key), formatter.format(value));
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

    /**
     * @return the pattern
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * @param pattern the pattern to set
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * @return the locale
     */
    public String getLocale() {
        return locale;
    }

    /**
     * @param locale the locale to set
     */
    public void setLocale(String localeString) {
        this.locale = localeString;
    }

    /**
     * @return the pattern2
     */
    public String getPattern2() {
        return pattern2;
    }

    /**
     * @param pattern2 the pattern2 to set
     */
    public void setPattern2(String pattern2) {
        this.pattern2 = pattern2;
    }

}
