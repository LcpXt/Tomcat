package com.colin.servlet.servlet;

import java.io.PrintWriter;

/**
 * 2024年06月24日13:57
 * 自己定义的servlet响应相关规范
 */
public interface HttpServletResponse {

    /**
     * 获取操作响应体字符输出流
     * @return
     */
    PrintWriter getWriter();

    /**
     * 设置响应头
     * @param key
     * @param value
     */
    void setHeader(String key, String value);

    /**
     * 设置响应行状态码
     * @param status
     */
    void setStatus(int status);

    /**
     * 重定向
     * @param location
     */
    void sendRedirect(String location);

    /**
     * 清空内容
     */
    void reset();

    /**
     * 添加cookie
     * @param cookie
     */
    void addCookie(Cookie cookie);
}
