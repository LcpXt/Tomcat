package com.colin.servlet;

/**
 * 2024年06月27日09:13
 */
public interface HttpSession {

    /**
     * 获取session的id
     * @return
     */
    int getId();

    /**
     * 设置session域对象的kv
     * @param key
     * @param value
     */
    void setAttribute(String key, Object value);

    /**
     * 根据session域对象的k获取v
     * @param key
     * @return
     */
    Object getAttribute(String key);

    /**
     * 根据key移除session对象的value
     * @param key
     */
    void removeAttribute(String key);

    /**
     * 手动删除session，实际上是把当前session对象标记为过期
     */
    void invalidate();
}
