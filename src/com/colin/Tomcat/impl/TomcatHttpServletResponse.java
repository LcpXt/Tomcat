package com.colin.Tomcat.impl;

import com.colin.servlet.HttpServletResponse;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 2024年06月24日14:02
 */
public class TomcatHttpServletResponse implements HttpServletResponse {

    /**
     * 状态码载体
     */
    private int status;

    /**
     * 响应头载体
     */
    private Map<String, String> headers;

    /**
     * 响应体的字符输出流
     */
    private PrintWriter printWriter;

    /**
     * 预留
     */
    private OutputStream outputStream;

    public TomcatHttpServletResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.headers = new HashMap<String, String>();
        this.headers.put("Content-Type", "text/html; charset=utf-8");
    }

    /**
     * 获取操作响应体字符输出流
     *
     * @return
     */
    @Override
    public PrintWriter getWriter() {
        return null;
    }

    /**
     * 设置响应头
     *
     * @param name
     * @param value
     */
    @Override
    public void setHeader(String name, String value) {

    }

    /**
     * 设置响应行状态码
     *
     * @param status
     */
    @Override
    public void setStatus(int status) {

    }
}
