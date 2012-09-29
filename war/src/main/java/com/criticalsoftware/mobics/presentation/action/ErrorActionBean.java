/*
 * $Id: ErrorActionBean.java,v 1.4 2012/05/04 14:35:36 ssantos Exp $
 *
 * Last changed on $Date: 2012/05/04 14:35:36 $
 * Last changed by $Author: ssantos $
 */
package com.criticalsoftware.mobics.presentation.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

/**
 * Error action bean.
 *
 * @author Samuel Santos
 * @version $Revision: 1.4 $
 */
public class ErrorActionBean extends BaseActionBean {

    @Validate
    private Integer httpError;

    /**
     * Gets the httpError.
     *
     * @return the httpError
     */
    public Integer getHttpError() {
        return httpError;
    }

    /**
     * Sets the httpError.
     *
     * @param httpError the httpError to set
     */
    public void setHttpError(Integer httpError) {
        this.httpError = httpError;
    }

    /**
     * Shows the error page.
     *
     * @return forward to the jsp
     */
    @DefaultHandler
    public Resolution main() {
        return new ForwardResolution("/WEB-INF/error.jsp");
    }
}
