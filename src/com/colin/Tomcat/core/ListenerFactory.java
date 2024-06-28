package com.colin.Tomcat.core;

import com.colin.Tomcat.exception.NotListenerException;
import com.colin.Tomcat.impl.TomcatListener;
import com.colin.servlet.listener.*;
import com.colin.servlet.servlet.HttpServletRequest;
import com.colin.servlet.servlet.HttpSession;
import com.colin.servlet.servlet.ServletContext;

/**
 * 2024年06月27日18:57
 */
public class ListenerFactory {

    private static Class<ServletRequestListener> requestListenerClass;

    private static Class<HttpSessionListener> httpSessionListenerClass;

    private static Class<ServletContextListener> servletContextListenerClass;

    private static Class<ServletRequestAttributeListener> servletRequestAttrbuteListenerClass;

    private static Class<HttpSessionAttributeListener> httpSessionAttributeListenerClass;

    private static Class<ServletContextAttributeListener> servletContextAttributeListenerClass;

    private static boolean notListener = true;

    public static void init(Class aClass) throws NotListenerException {
        if(ServletRequestListener.class.isAssignableFrom(aClass)){
            System.out.println("传来了servletRequestListener");
            requestListenerClass = aClass;
            notListener = false;

        }
        if(HttpSessionListener.class.isAssignableFrom(aClass)){
            System.out.println("传来了httpSessionListenerClass");
            httpSessionListenerClass = aClass;
            notListener = false;

        }
        if(ServletContextListener.class.isAssignableFrom(aClass)){
            System.out.println("传来了servletContextListenerClass");
            servletContextListenerClass = aClass;
            notListener = false;

        }
        if(ServletRequestAttributeListener.class.isAssignableFrom(aClass)){
            System.out.println("传来了servletRequestAttrbuteListenerClass");
            servletRequestAttrbuteListenerClass = aClass;
            notListener = false;

        }
        if(HttpSessionAttributeListener.class.isAssignableFrom(aClass)){
            System.out.println("传来了httpSessionAttributeListenerClass");
            httpSessionAttributeListenerClass = aClass;
            notListener = false;

        }
        if(ServletContextAttributeListener.class.isAssignableFrom(aClass)){
            System.out.println("传来了servletContextAttributeListenerClass");
            servletContextAttributeListenerClass = aClass;
            notListener = false;
        }
        if (notListener){
            throw new NotListenerException("当前类不是监听器类");
        }
    }

    /**
     * 借助于工厂模式，使用一个统一的getListener方法 获取不同监听器实例
     * @param o 当前域对象
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static Listener getListener(Object o) throws InstantiationException, IllegalAccessException {
        if (o instanceof HttpServletRequest && requestListenerClass != null){
            return requestListenerClass.newInstance();
        }
        if (o instanceof HttpSession && httpSessionListenerClass != null){
            return httpSessionListenerClass.newInstance();
        }
        if (o instanceof ServletContext && servletContextListenerClass != null){
            return servletContextListenerClass.newInstance();
        }
        return new TomcatListener();
    }


    public static Listener getAttributeListener(Object o) throws InstantiationException, IllegalAccessException {
        if (o instanceof HttpServletRequest && servletRequestAttrbuteListenerClass != null){
            return servletRequestAttrbuteListenerClass.newInstance();
        }
        if (o instanceof HttpSession && httpSessionAttributeListenerClass != null){
            return httpSessionAttributeListenerClass.newInstance();
        }
        if (o instanceof ServletContext && servletContextAttributeListenerClass != null){
            return servletContextAttributeListenerClass.newInstance();
        }
        return new TomcatListener();
    }
}
