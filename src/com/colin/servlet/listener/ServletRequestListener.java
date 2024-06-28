package com.colin.servlet.listener;

/**
 * 2024年06月28日08:39
 */
public interface ServletRequestListener extends Listener{
    default void destroyed(ServletRequestEvent sre) {
    }

    default void initRequest(ServletRequestEvent sre) {
    }
}
