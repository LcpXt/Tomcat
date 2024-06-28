package com.colin.user.listener;

import com.colin.servlet.annotation.WebListener;
import com.colin.servlet.listener.HttpSessionAttributeEvent;
import com.colin.servlet.listener.HttpSessionAttributeListener;
import com.colin.servlet.listener.HttpSessionEvent;
import com.colin.servlet.listener.HttpSessionListener;

/**
 * 2024年06月28日13:42
 */
@WebListener
public class MyHttpSessionListener implements HttpSessionListener
//                                    , HttpSessionAttributeListener
{
//    @Override
//    public void attributeAdded(HttpSessionAttributeEvent scae) {
//        System.out.println("添加了kv：" + scae.getKey() + "=" + scae.getValue());
//    }
//
//    @Override
//    public void attributeRemoved(HttpSessionAttributeEvent scae) {
//        System.out.println("删除了kv：" + scae.getKey() + "=" + scae.getValue());
//    }
//
//    @Override
//    public void attributeReplaced(HttpSessionAttributeEvent scae) {
//        System.out.println("更新了kv：" + scae.getKey() + "=" + scae.getValue());
//    }

    @Override
    public void destroyed(HttpSessionEvent sre) {
        System.out.println(sre.getSession() + "域对象销毁了");
    }

    @Override
    public void initHttpSession(HttpSessionEvent sre) {
        System.out.println(sre.getSession() + "域对象初始化了");
    }
}
