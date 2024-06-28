package com.colin.Tomcat.impl;

import com.colin.Tomcat.core.ListenerFactory;
import com.colin.Tomcat.core.SessionManager;
import com.colin.servlet.listener.*;
import com.colin.servlet.servlet.HttpSession;

import java.util.HashMap;
import java.util.Map;

/**
 * 2024年06月27日09:22
 */
public class TomcatHttpSession implements HttpSession {



    private Map<String, Object> attributes;

    private HttpSessionAttributeListener attributeListener;


    public TomcatHttpSession() throws InstantiationException, IllegalAccessException {
        this.id = SessionManager.sessionIdManager.incrementAndGet();
        this.createTime = System.currentTimeMillis();
        this.ttl = DEFAULT_SESSION_TTL;
        this.ttlMark = false;
        this.attributes = new HashMap<String, Object>();

        this.httpSessionEvent = new HttpSessionEvent(this);
        this.sessionListener = (HttpSessionListener) ListenerFactory.getListener(this);
        if (this.sessionListener != null){
            this.sessionListener.initHttpSession(this.httpSessionEvent);
        }

        this.attributesListener = (HttpSessionAttributeListener) ListenerFactory.getAttributeListener(this);
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
//    private static final Long DEFAULT_SESSION_TTL = 100 * 60 * 30L;
    private static final Long DEFAULT_SESSION_TTL = 10000L;

    /**
     * 是否过期的标记
     */
    private Boolean ttlMark;

    private HttpSessionListener sessionListener;

    private final HttpSessionEvent httpSessionEvent;

    private final HttpSessionAttributeListener attributesListener;

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
        HttpSessionAttributeEvent httpSessionAttributeEvent;
        //先看看存不存在重名key覆盖问题，有就是更新attribute，没有就是添加
        for (String temp : attributes.keySet()) {
            if (temp.equals(key)) {
                httpSessionAttributeEvent = new HttpSessionAttributeEvent(this, temp, this.attributes.get(temp));
                this.attributeListener.attributeReplaced(httpSessionAttributeEvent);
                this.attributes.put(key, value);
                return;
            }
        }
        httpSessionAttributeEvent = new HttpSessionAttributeEvent(this, key, value);
        this.attributeListener.attributeAdded(httpSessionAttributeEvent);
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
        HttpSessionAttributeEvent httpSessionAttributeEvent = new HttpSessionAttributeEvent(this, key, this.attributes.get(key));
        this.attributeListener.attributeRemoved(httpSessionAttributeEvent);
        this.attributes.remove(key);
    }

    /**
     * 手动删除session，实际上是把当前session对象标记为过期
     */
    @Override
    public void invalidate() {
        this.sessionListener.destroyed(this.httpSessionEvent);
        this.setTtlMark(true);
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Long getTtl() {
        return ttl;
    }

    public void setTtl(Long ttl) {
        this.ttl = ttl;
    }

    public Boolean getTtlMark() {
        return ttlMark;
    }

    public void setTtlMark(Boolean ttlMark) {
        this.ttlMark = ttlMark;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public HttpSessionListener getSessionListener() {
        return sessionListener;
    }

    public HttpSessionEvent getHttpSessionEvent() {
        return httpSessionEvent;
    }

    public HttpSessionAttributeListener getAttributesListener() {
        return attributesListener;
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
