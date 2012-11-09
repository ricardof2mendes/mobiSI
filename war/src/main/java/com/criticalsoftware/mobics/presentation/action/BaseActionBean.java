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
     * Get the active menu
     * 
     * @return
     */
    public String getActiveMenu(){
        String name = getClass().getPackage().getName(); 
        return name.substring(name.lastIndexOf('.') + 1);
    }

    /**
     * Get the splash screen style
     * @return
     */
    public final String getSplashScreenStyle() {
        String style;
        if(this.getContext().getUser() != null) {
            style = new StringBuilder(this.getContext().getUser().getCarClubColor()).append(" ").append(this.getContext().getUser().getCarClubTheme()).toString().toLowerCase();
        } else {
            style = Theme.getTheme(this.getContext().getRequest().getServerName());
        }
        
        if(style.indexOf(Theme.WARM) > 0) {
            style.replaceAll(" ", "");
        }
        
        return style;
    }
    
    /**
     * Get the header style
     * @return
     */
    public final String getHeaderStyle() {
        String style;
        if(this.getContext().getUser() != null) {
            style = this.getContext().getUser().getCarClubColor();
        } else {
            style = Theme.getColor(this.getContext().getRequest().getServerName());
        }
        return style;
    }
    
    /**
     * Check if action bean it has validation field errors
     * @return
     */
    public final boolean getFieldErrors(){
        return this.context.getValidationErrors().hasFieldErrors();
    }

}
