package com.colin.servlet.listener;

import com.colin.servlet.servlet.HttpSession;
import sun.security.mscapi.CKeyPairGenerator;

/**
 * 2024年06月28日08:51
 */
public class HttpSessionAttributeEvent extends HttpSessionEvent {

    private String key;
    private Object value;

    public HttpSessionAttributeEvent(HttpSession session, String key, Object value) {
        super(session);
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
