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
package com.criticalsoftware.mobics.presentation.action.booking;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import com.criticalsoftware.mobics.presentation.action.BaseActionBean;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;

/**
 * @author ltiago
 * @version $Revision: $
 */
@MobiCSSecure
public class RecentHistoryActionBean extends BaseActionBean {
    
    public Resolution recent() {
        return new ForwardResolution("/WEB-INF/book/recent.jsp");
    }
    
    public Resolution currentTrip() {
        return new ForwardResolution("/WEB-INF/book/currentTrip.jsp");
    }
    
    public Resolution lastTrip() {
        return new ForwardResolution("/WEB-INF/book/lastTrip.jsp");
    }

}
