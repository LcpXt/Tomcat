package com.colin.servlet.listener;

import com.colin.servlet.servlet.HttpSession;

/**
 * 2024年06月28日08:52
 */
public class HttpSessionEvent {

    private HttpSession session;

    public HttpSessionEvent(HttpSession session) {
        this.session = session;
    }

    public HttpSession getSession() {
        return this.session;
    }
}
