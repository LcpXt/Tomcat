package com.colin.servlet.listener;

/**
 * 2024年06月28日08:40
 */
public interface HttpSessionListener extends Listener{
    default void destroyed(HttpSessionEvent sre) {
    }

    default void initHttpSession(HttpSessionEvent sre) {
    }
}
