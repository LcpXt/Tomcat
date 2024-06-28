package com.colin.servlet.listener;

/**
 * 2024年06月28日08:40
 */
public interface ServletRequestAttributeListener extends Listener{
    default void attributeAdded(ServletRequestAttributeEvent srae) {
    }

    default void attributeRemoved(ServletRequestAttributeEvent srae) {
    }

    default void attributeReplaced(ServletRequestAttributeEvent srae) {
    }
}
