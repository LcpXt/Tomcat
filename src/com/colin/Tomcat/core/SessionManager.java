package com.colin.Tomcat.core;

import com.colin.Tomcat.impl.TomcatHttpSession;
import com.colin.servlet.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 2024年06月27日09:46
 */
public class SessionManager {

    public static AtomicInteger sessionIdManager = new AtomicInteger(0);

    /**
     * 用session的id作为key，用session对象作为value
     * <p>
     * 当需要获取时，前端会传过来一个cookie key JSESSIONID value session的id值
     */
    private static Map<Integer, HttpSession> sessionContainer = new ConcurrentHashMap<>();

    /**
     * 根据sessionId，从全局唯一的session容器中获取对象
     * @param sessionId
     * @return
     */
    public static HttpSession getSession(Integer sessionId) {
        return sessionContainer.get(sessionId);
    }

    /**
     * 初始化并返回session
     * @return
     */
    public static HttpSession initAndGetSession() {
        TomcatHttpSession tomcatHttpSession = new TomcatHttpSession();
        sessionContainer.put(tomcatHttpSession.getId(), tomcatHttpSession);
        return tomcatHttpSession;
    }
}
