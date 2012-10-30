/*
 * $Id: BaseActionBean.java,v 1.4 2012/05/04 14:35:36 ssantos Exp $
 *
 * Last changed on $Date: 2012/05/04 14:35:36 $
 * Last changed by $Author: ssantos $
 */
package com.criticalsoftware.mobics.presentation.action;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.StrictBinding;

import com.criticalsoftware.mobics.presentation.extension.MobiCSActionBeanContext;
import com.criticalsoftware.mobics.presentation.util.Theme;

/**
 * Base ActionBean class.
 * 
 * @author Samuel Santos
 * @version $Revision: 1.4 $
 */
@StrictBinding
public abstract class BaseActionBean implements ActionBean {

    /**
     * The Stripes action bean context.
     */
    private MobiCSActionBeanContext context;

    /**
     * Gets the context.
     * 
     * @return the context
     */
    @Override
    public final MobiCSActionBeanContext getContext() {
        return context;
    }

    /**
     * Sets the context.
     * 
     * @param context the context to set
     */
    @Override
    public final void setContext(ActionBeanContext context) {
        this.context = (MobiCSActionBeanContext) context;
    }

    /**
     * Get the theme
     * @return
     */
    public final String getTheme() {
        String theme;
        if(this.getContext().getUser() != null) {
            theme = this.getContext().getUser().getCarClubStyle();
        } else {
            theme = Theme.getTheme(this.getContext().getRequest().getServerName());
        }
        return theme;
    }
    
    /**
     * Check if action bean it has validation field errors
     * @return
     */
    public boolean getFieldErrors(){
        return this.context.getValidationErrors().hasFieldErrors();
    }

}
