package com.colin.servlet.listener;

/**
 * 2024年06月28日08:41
 */
public interface ServletContextAttributeListener extends Listener{
    default void attributeAdded(ServletContextAttributeEvent scae) {
    }

    default void attributeRemoved(ServletContextAttributeEvent scae) {
    }

    default void attributeReplaced(ServletContextAttributeEvent scae) {
    }
}
