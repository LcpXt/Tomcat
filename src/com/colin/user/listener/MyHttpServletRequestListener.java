package com.colin.user.listener;

import com.colin.servlet.annotation.WebListener;
import com.colin.servlet.listener.ServletRequestAttributeEvent;
import com.colin.servlet.listener.ServletRequestAttributeListener;
import com.colin.servlet.listener.ServletRequestEvent;
import com.colin.servlet.listener.ServletRequestListener;
import com.colin.servlet.servlet.HttpServletRequest;

/**
 * 2024年06月28日11:24
 */
//@WebListener
public class MyHttpServletRequestListener implements ServletRequestListener
//        , ServletRequestAttributeListener
{
//    @Override
//    public void attributeAdded(ServletRequestAttributeEvent srae) {
//        System.out.println("添加了kv：" + srae.getKey() + "=" + srae.getValue());
//    }
//
//    @Override
//    public void attributeReplaced(ServletRequestAttributeEvent srae) {
//        System.out.println("更新了kv：" + srae.getKey() + "=" + srae.getValue());
//    }
//
//    @Override
//    public void attributeRemoved(ServletRequestAttributeEvent srae) {
//        System.out.println("移除了kv：" + srae.getKey() + "=" + srae.getValue());
//    }

    @Override
    public void destroyed(ServletRequestEvent sre) {
        System.out.println(sre.getHttpServletRequest() + "域对象销毁了");
    }

    @Override
    public void initRequest(ServletRequestEvent sre) {
//        HttpServletRequest httpServletRequest = sre.getHttpServletRequest();
//        httpServletRequest.setAttribute("hello", "监听器中设置的hello");
        System.out.println(sre.getHttpServletRequest() + "域对象初始化了");
    }
}
