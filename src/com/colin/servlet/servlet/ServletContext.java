package com.colin.servlet.servlet;

/**
 * 2024年06月27日15:27
 */
public interface ServletContext {

    void setAttribute(String key, Object value);

    Object getAttribute(String key);

    void removeAttribute(String key);
}
