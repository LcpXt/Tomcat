package com.colin.Tomcat.core;

import com.colin.Tomcat.impl.TomcatHttpSession;
import com.colin.servlet.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 2024年06月27日09:46
 */
public class SessionManager {

    /**
     * SessionId 生成器
     */
    public static AtomicInteger sessionIdManager = new AtomicInteger(0);

    /**
     * 用session的id作为key，用session对象作为value
     * <p>
     * 当需要获取时，前端会传过来一个cookie key JSESSIONID value session的id值
     */
    public static Map<Integer, HttpSession> sessionContainer = new ConcurrentHashMap<>();

    /**
     * 根据sessionId，从全局唯一的session容器中获取对象
     * <p>
     * 并在此处对session是否过期进行惰性检查
     * @param sessionId
     * @return
     */
    public static HttpSession getSession(Integer sessionId) {
        TomcatHttpSession httpSession = (TomcatHttpSession) sessionContainer.get(sessionId);
        //可能别标记为过期，但是还没有移除
        if (httpSession != null) {
            if (httpSession.getTtlMark()){
                return null;
            }
            //可能随机抽样还没抽到，但是已经过期了
            long currentTime = System.currentTimeMillis();
            if(currentTime - httpSession.getCreateTime() >= httpSession.getTtl()){
                httpSession.setTtlMark(true);
                return null;
            }//无需手动remove，标记以后交给后台线程，检测ttlMark为true时自动remove
            //每次获取时，给session续期
            httpSession.setCreateTime(System.currentTimeMillis());
        }
        return httpSession;
    }

    /**
     * 借助生产者消费者模型实现随机取样定期删除
     */
    public static ClearSessionProAndCon clearSessionProAndCon = new ClearSessionProAndCon(new ArrayBlockingQueue<>(100));

    /**
     * 初始化并返回session
     * @return
     */
    public static HttpSession initAndGetSession() {
        TomcatHttpSession tomcatHttpSession = new TomcatHttpSession();
        sessionContainer.put(tomcatHttpSession.getId(), tomcatHttpSession);
        return tomcatHttpSession;
    }

    /**
     * 最优先执行，且只执行一次
     */
    static {
        //生产者线程启动
        new Thread(() ->{
            System.out.println("生产者随机取样开始");
            try {
                clearSessionProAndCon.doProduct();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        //消费者线程启动
        new Thread(() ->{
            System.out.println("消费者开始标记过期key");
            try {
                clearSessionProAndCon.doConsume();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() ->{
            while (true){
                for (Integer sessionId : sessionContainer.keySet()) {
                    if (((TomcatHttpSession) sessionContainer.get(sessionId)).getTtlMark()) {
                        System.out.println(sessionContainer.get(sessionId) + "开始清理过期key");
                        sessionContainer.remove(sessionId);
                    }
                    try {
                        Thread.sleep(20000);
//                    Thread.sleep(2 * 60 * 60 * 1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }
}
