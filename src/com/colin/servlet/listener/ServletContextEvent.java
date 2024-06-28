package com.colin.servlet.listener;

import com.colin.servlet.servlet.ServletContext;

/**
 * 2024年06月28日08:48
 */
public class ServletContextEvent {
    private ServletContext servletContext;
    public ServletContextEvent(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
