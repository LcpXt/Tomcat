package com.colin.servlet.listener;

import com.colin.servlet.servlet.HttpServletRequest;
import com.colin.servlet.servlet.ServletContext;

/**
 * 2024年06月28日08:50
 */
public class ServletRequestEvent {

    private HttpServletRequest httpServletRequest;

    public ServletRequestEvent(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public HttpServletRequest getHttpServletRequest() {
        return this.httpServletRequest;
    }

    public ServletContext getServletContext() {
        return this.httpServletRequest.getServletContext();
    }
}
