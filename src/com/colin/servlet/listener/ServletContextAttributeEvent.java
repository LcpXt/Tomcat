package com.colin.servlet.listener;

import com.colin.servlet.servlet.ServletContext;

/**
 * 2024年06月28日08:46
 */
public class ServletContextAttributeEvent extends ServletContextEvent {

    private String key;
    private Object value;

    public ServletContextAttributeEvent(ServletContext servletContext, String key, Object value) {
        super(servletContext);
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }

    public Object getValue() {
        return this.value;
    }
}
