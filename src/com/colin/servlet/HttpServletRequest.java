package com.colin.servlet;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

/**
 * 2024年06月24日09:59
 * 自己定义的servlet请求相关规范
 */
public interface HttpServletRequest {

    /**
     * 根据请求头的key获取对应value
     * @param key
     * @return
     */
    String getHeader(String key);

    /**
     * 获取所有请求头的key
     * @return
     */
    Map<String, String> getHeaders();

    /**
     * 获取这次请求的请求方法
     * @return
     */
    String getMethod();

    /**
     * 获取这次请求的URL
     * @return
     */
    String getRemoteURL();

    /**
     * 获取此次请求的URI
     * @return
     */
    String getRemoteURI();

    /**
     * 获取客户端ip地址
     * @return
     */
    String getRemoteAddr();

    /**
     * 获取字符串参数
     * @param key
     * @return
     */
    String getParameter(String key);

    /**
     * 获取当前请求对象的字符输入流
     * @return
     */
    BufferedReader getReader();

    /**
     * 获取请求体数据的字节流
     * @return
     */
    InputStream getInputStream();

    /**
     * 获取请求控制器
     * @param uri
     */
    RequestDispatcher getRequestDispatcher(String uri);

    /**
     *向域对象放入kv
     * @param key
     * @param value
     */
    void setAttribute(String key, Object value);

    /**
     * 根据k，从域对象获取v
     * @param key
     * @return
     */
    Object getAttribute(String key);

    /**
     * 获取cookie
     * @return
     */
    Cookie[] getCookies();
}
