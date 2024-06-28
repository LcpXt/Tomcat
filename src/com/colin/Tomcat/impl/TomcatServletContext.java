package com.colin.Tomcat.impl;

import com.colin.servlet.listener.*;
import com.colin.servlet.servlet.ServletContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 2024年06月27日15:36
 */
public class TomcatServletContext implements ServletContext {

    /**
     * 作为域对象功能的存储attribute的Map
     */
    private Map<String, Object> attributes;

    private ServletContextEvent servletContextEvent;

    private ServletContextAttributeListener servletContextAttributeListener;

    /**
     * 借助 饿汉的单例模式 构建TomcatServletContext
     * TomcatServletContext域对象的作用域是 单例的，一个服务中只维护一个TomcatServletContext
     * 并且他的生命周期是在服务启动时创建，在服务关闭时销毁
     * <p>
     * 为什么不用懒汉式？
     * <p>
     *     他的生命周期是在服务启动时创建，饿汉会在服务启动时就创建这个对象，而懒汉得获取这个对象的时候才会创建
     */
    private static ServletContext application = new TomcatServletContext();
    private TomcatServletContext() {
        //初始化
        //单例的一定线程不安全，用ConcurrentHashMap保证线程安全
        this.attributes = new ConcurrentHashMap<>();
        this.servletContextEvent = new ServletContextEvent(this);
    }
    public static ServletContext getServletContext() {
        return application;
    }

    @Override
    public void setAttribute(String key, Object value) {
        ServletContextAttributeEvent servletContextAttributeEvent;
        //先看看存不存在重名key覆盖问题，有就是更新attribute，没有就是添加
        for (String temp : attributes.keySet()) {
            if (temp.equals(key)) {
                servletContextAttributeEvent = new ServletContextAttributeEvent(this, temp, this.attributes.get(temp));
                this.servletContextAttributeListener.attributeReplaced(servletContextAttributeEvent);
                this.attributes.put(key, value);
                return;
            }
        }
        servletContextAttributeEvent = new ServletContextAttributeEvent(this, key, value);
        this.servletContextAttributeListener.attributeAdded(servletContextAttributeEvent);
        this.attributes.put(key, value);
    }

    @Override
    public Object getAttribute(String key) {
        ServletContextAttributeEvent servletContextAttributeEvent = new ServletContextAttributeEvent(this, key, this.attributes.get(key));
        this.servletContextAttributeListener.attributeRemoved(servletContextAttributeEvent);
        return this.attributes.get(key);
    }

    @Override
    public void removeAttribute(String key) {
        this.attributes.remove(key);
    }

    public ServletContextEvent getServletContextEvent() {
        return this.servletContextEvent;
    }

    public void setServletContextAttributeListener(ServletContextAttributeListener attributeListener) {
        this.servletContextAttributeListener = attributeListener;
    }
}
