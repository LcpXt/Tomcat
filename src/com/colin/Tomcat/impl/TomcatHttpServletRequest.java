package com.colin.Tomcat.impl;

import com.colin.servlet.HttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 2024年06月24日10:09
 */
public class TomcatHttpServletRequest implements HttpServletRequest {

    private final String requestContent;
    private final Map<String, String> requestHeaders;
    private final String requestLine;
    private final String requestBody;

    public TomcatHttpServletRequest(InputStream inputStream) {
        try {
            byte[] temp = new byte[8196];
            int read = inputStream.read(temp);
            String s = new String(temp, 0, read, StandardCharsets.UTF_8);
            System.out.println("以下是请求报文：");
            this.requestContent = s;
            this.requestHeaders = new HashMap<>();
            System.out.println(s);
            int headerBeginIndex = this.requestContent.indexOf("\n");
            int headerEndIndex = this.requestContent.indexOf("\r\n\r\n");
            String substring = this.requestContent.substring(headerBeginIndex + 1, headerEndIndex);
            String[] split = substring.split("\r\n");
            for (String headerKeyAndValue : split) {
                int i = headerKeyAndValue.indexOf(":");
                String headerKey = headerKeyAndValue.substring(0, i);
                requestHeaders.put(headerKey, headerKeyAndValue.substring(i + 1));
            }
            this.requestLine = requestContent.substring(0, headerBeginIndex - 1);
            this.requestBody = requestContent.substring(headerEndIndex + 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据请求头的key获取对应value
     *
     * @param key
     * @return
     */
    @Override
    public String getHeader(String key) {
        return this.requestHeaders.get(key);
    }

    /**
     * 获取所有请求头的key
     *
     * @return
     */
    @Override
    public Map<String, String> getHeaders() {
        return this.requestHeaders;
    }

    /**
     * 获取这次请求的请求方法
     *
     * @return
     */
    @Override
    public String getMethod() {
        return this.requestLine.split(" ")[0];
    }

    /**
     * 获取这次请求的URL
     *
     * @return
     */
    @Override
    public String getRemoteURL() {
        return this.requestHeaders.get("Host");
    }

    /**
     * 获取此次请求的URI
     *
     * @return
     */
    @Override
    public String getRemoteURI() {
        return requestLine.split(" ")[1];
    }

    /**
     * 获取客户端ip地址
     *
     * @return
     */
    @Override
    public String getRemoteAddr() {
        return "";
    }

    /**
     * 获取字符串参数
     *
     * @param key
     * @return
     */
    @Override
    public String getParameter(String key) {
        return this.requestBody;
    }
}
