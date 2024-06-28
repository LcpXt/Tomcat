package com.colin.servlet.listener;

import com.colin.servlet.servlet.HttpServletRequest;

/**
 * 2024年06月28日08:49
 */
public class ServletRequestAttributeEvent extends ServletRequestEvent {

    private String key;

    private Object value;

    public ServletRequestAttributeEvent(HttpServletRequest httpServletRequest, String key, Object value) {
        super(httpServletRequest);
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
