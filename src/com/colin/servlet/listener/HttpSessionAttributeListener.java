package com.colin.servlet.listener;

/**
 * 2024年06月28日08:41
 */
public interface HttpSessionAttributeListener extends Listener{
    default void attributeAdded(HttpSessionAttributeEvent scae) {
    }

    default void attributeRemoved(HttpSessionAttributeEvent scae) {
    }

    default void attributeReplaced(HttpSessionAttributeEvent scae) {
    }
}
