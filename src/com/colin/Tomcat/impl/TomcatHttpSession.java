package com.colin.Tomcat.impl;

import com.colin.Tomcat.core.SessionManager;
import com.colin.servlet.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 2024年06月27日09:22
 */
public class TomcatHttpSession implements HttpSession {

    private Map<String, Object> attributes;

    public TomcatHttpSession() {
        this.id = SessionManager.sessionIdManager.incrementAndGet();
        this.createTime = System.currentTimeMillis();
        this.ttl = DEFAULT_SESSION_TTL;
        this.ttlMark = false;
        this.attributes = new HashMap<String, Object>();
    }

    /**
     * 先用主键自增生成id
     * <p>
     * 后续可以改成 雪花算法 或者 SecureRandom 生成随机id
     */
    private Integer id;

    /**
     * 第一次getSession时，这个属性就是Session的创建时间
     * <p>
     * 后续请求再访问时，会给这个创建时间续期
     * <p>
     * 这个属性的含义就变成这个session最后一次使用时间
     */
    private  Long createTime;

    /**
     * time-to-live
     */
    private Long ttl;

    /**
     * 默认的session过期时间30分钟
     *<p>
     *可以剥离到配置文件
     */
    private static final Long DEFAULT_SESSION_TTL = 100 * 60 * 30L;

    /**
     * 是否过期的标记
     */
    private Boolean ttlMark;

    /**
     * 获取session的id
     *
     * @return
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * 设置session域对象的kv
     *
     * @param key
     * @param value
     */
    @Override
    public void setAttribute(String key, Object value) {
        this.attributes.put(key, value);
    }

    /**
     * 根据session域对象的k获取v
     *
     * @param key
     * @return
     */
    @Override
    public Object getAttribute(String key) {
        return this.attributes.get(key);
    }

    /**
     * 根据key移除session对象的value
     *
     * @param key
     */
    @Override
    public void removeAttribute(String key) {
        this.attributes.remove(key);
    }

    /**
     * 手动删除session，实际上是把当前session对象标记为过期
     */
    @Override
    public void invalidate() {

    }

    @Override
    public String toString() {
        return "TomcatHttpSession{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", ttl=" + ttl +
                ", ttlMark=" + ttlMark +
                '}';
    }
}
