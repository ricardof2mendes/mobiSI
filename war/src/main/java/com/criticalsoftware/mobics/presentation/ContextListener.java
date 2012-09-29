/*
 * $Id: ContextListener.java,v 1.4 2012/05/22 11:17:39 ssantos Exp $
 *
 * Last changed on $Date: 2012/05/22 11:17:39 $
 * Last changed by $Author: ssantos $
 */
package com.criticalsoftware.mobics.presentation;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/**
 * Context Listener.
 *
 * @author Samuel Santos
 * @version $Revision: 1.4 $
 */
public class ContextListener implements ServletContextListener {

    private ServletContext context;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        context = event.getServletContext();
        // FIXME no configuration here
        //context.setAttribute("configuration", Configuration.getInstance());
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        context = null;
    }
}
