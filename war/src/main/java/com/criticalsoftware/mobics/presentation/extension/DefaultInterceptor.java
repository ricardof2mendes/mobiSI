/*
 * $Id: DefaultInterceptor.java,v 1.2 2012/05/04 14:35:36 ssantos Exp $
 *
 * Last changed on $Date: 2012/05/04 14:35:36 $
 * Last changed by $Author: ssantos $
 */
package com.criticalsoftware.mobics.presentation.extension;

import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.criticalsoftware.mobics.presentation.action.LoginActionBean;
import com.criticalsoftware.mobics.presentation.security.MobiCSSecure;
import com.criticalsoftware.mobics.presentation.security.User;

/**
 * Logs user actions.
 * 
 * @author Samuel Santos
 * @version $Revision: 1.2 $
 */
@Intercepts(LifecycleStage.HandlerResolution)
public class DefaultInterceptor implements Interceptor {

    private static Logger LOGGER;

    @Override
    public Resolution intercept(ExecutionContext ctx) throws Exception {
        Resolution resolution = ctx.proceed();

        LOGGER = LoggerFactory.getLogger(ctx.getActionBean().getClass());

        if (ctx.getActionBean().getClass().isAnnotationPresent(MobiCSSecure.class)) {

            User user = ((MobiCSActionBeanContext) ctx.getActionBeanContext()).getUser();
            if (user != null) {
                LOGGER.info("User [{}] executing method [{}].", user.getUsername(), ctx.getHandler().getName());
            } else {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("User not authenticated. Redirect to login");
                }
                String carClub = (String) ((MobiCSActionBeanContext) ctx.getActionBeanContext()).getServletContext()
                        .getAttribute("carClubCode");
                resolution = new RedirectResolution(LoginActionBean.class).addParameter("CC", carClub);
            }
        }
        return resolution;
    }
}
