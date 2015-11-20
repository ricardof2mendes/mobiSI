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
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;

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

    private Object value;
    private String type;

    public FormatMobics() {
        super();
        init();
    }

    private void init() {
        value = type = null;
    }

    @Override
    public int doEndTag() throws JspException {
        String formatted;
        Map<String, String> patterns = (Map<String, String>) pageContext.getServletContext().getAttribute(
                "carClubConfiguration");
        if (value == null || value.equals("") || type == null || type.equals("") || patterns == null) {
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

        // check for the request
        Locale locale = pageContext.getRequest().getLocale();

        // If locale is null, use the default locale
        if (locale == null) {
            locale = Locale.getDefault();
        }

        if (locale != null) {
            formatted = FormatUtils.format(type, value, locale, patterns);
            if(formatted == null) {
                return EVAL_PAGE;
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
