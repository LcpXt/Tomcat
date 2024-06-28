package com.colin.servlet.listener;

/**
 * 2024年06月28日08:40
 */
public interface ServletContextListener extends Listener{
    default void init(ServletContextEvent sce) {
    }

    default void destroyed(ServletContextEvent sce) {
    }
}
