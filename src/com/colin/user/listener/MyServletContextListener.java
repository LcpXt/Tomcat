package com.colin.user.listener;

import com.colin.servlet.annotation.WebListener;
import com.colin.servlet.listener.ServletContextAttributeEvent;
import com.colin.servlet.listener.ServletContextAttributeListener;
import com.colin.servlet.listener.ServletContextEvent;
import com.colin.servlet.listener.ServletContextListener;

/**
 * 2024年06月28日15:12
 */
@WebListener
public class MyServletContextListener implements ServletContextListener
//                                        , ServletContextAttributeListener
{
//    @Override
//    public void attributeAdded(ServletContextAttributeEvent scae) {
//        ServletContextAttributeListener.super.attributeAdded(scae);
//    }
//
//    @Override
//    public void attributeRemoved(ServletContextAttributeEvent scae) {
//        ServletContextAttributeListener.super.attributeRemoved(scae);
//    }
//
//    @Override
//    public void attributeReplaced(ServletContextAttributeEvent scae) {
//        ServletContextAttributeListener.super.attributeReplaced(scae);
//    }

    @Override
    public void init(ServletContextEvent sce) {
        System.out.println("servletContext初始化了");
    }

    @Override
    public void destroyed(ServletContextEvent sce) {
        System.out.println("servletContext销毁了");
    }
}
