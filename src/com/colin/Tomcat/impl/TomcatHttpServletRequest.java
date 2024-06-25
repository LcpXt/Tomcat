package com.colin.Tomcat.impl;

import com.colin.servlet.HttpServletRequest;

import java.io.*;
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
    private String requestBody;
    private Map<String, String> queryParamMap;
    private String queryParamString;
    private String uri;
    private String url;

    public TomcatHttpServletRequest(InputStream inputStream, String serverIp, int serverPort) throws IOException {
        try {
            byte[] temp = new byte[32784];
            int read = inputStream.read(temp);
            //            System.out.println("以下是请求报文：");
            this.requestContent = new String(temp, 0, read, StandardCharsets.UTF_8);
//            System.out.println(this.requestContent);
            this.requestHeaders = new HashMap<>();
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
            //解析请求行
            this.parseRequestLine(serverIp, serverPort);
            this.parseRequestBody(headerEndIndex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析请求体
     * @param headerEndIndex
     */
    private void parseRequestBody(int headerEndIndex) {
        this.requestBody = requestContent.substring(headerEndIndex + 4);
        String contentType = requestHeaders.get("Content-Type");
        if (contentType == null){
            return;
        }
        //为了能让getParamter同时能获取请求体中的普通表单数据，选择在这里解析请求体，并放入queryParamMap
        //当tomcat的使用者调用getParamter方法时，其实是queryParamMap.get
        //就能做到既能获取param也能获取请求体表单数据
        //只有请求体的数据格式是application/x-www-form-urlencoded才能解析kv
        if ("application/x-www-form-urlencoded".equals(contentType.trim())){
            this.parseUrlencodedToQueryParamMap(this.requestBody);
        }
    }

    private void parseUrlencodedToQueryParamMap(String str) {
        String[] entryArr = str.split("&");
        for (String entry : entryArr) {
            String[] keyValue = entry.split("=");
            this.queryParamMap.put(keyValue[0], keyValue[1]);
        }
    }

    /**
     * 解析请求行数据，拆分成URI、URL和param
     * @param serverIp
     * @param serverPort
     */
    private void parseRequestLine(String serverIp, int serverPort) {
        String[] temp = this.requestLine.split(" ");
        //这是 /test?username=abc
        String uriAndParam = this.requestLine.split(" ")[1];
        //最终的uri
        this.uri = uriAndParam;
        // 第一个问号的位置
        int questionMarkIndex = uriAndParam.indexOf("?");
        
        //有?的话就把前面的截出来，防止没有?或者只有?没有后面的kv
        if (questionMarkIndex == -1) {
            this.queryParamString = "";
            this.uri = uriAndParam;
        } else if (temp[1].endsWith("?")) {
            this.queryParamString = "";
            this.uri = uriAndParam.substring(0, uriAndParam.length() - 1);
        } else {
            this.uri = uriAndParam.substring(0, questionMarkIndex);
            //查询字符串参数
            this.queryParamString = uriAndParam.substring(questionMarkIndex + 1);
            //解析字符串参数的方法
            this.parseQueryParamToMap(this.queryParamString);
        }
        this.url = (temp[2].split("/"))[0].toLowerCase() + "://" + serverIp + ":" + serverPort + uriAndParam;
//        System.out.println("serverIp："+ serverIp);
//        System.out.println("serverport："+ serverPort);
//        System.out.println(this.url);
//        System.out.println(this.uri);
//        System.out.println(this.queryParamString);
    }

    /**
     * 解析字符串参数
     */
    private void parseQueryParamToMap(String str) {
        this.queryParamMap = new HashMap<>();
        String[] entryArr = str.split("&");
        for (String entry : entryArr) {
            String[] keyValue = entry.split("=");
            this.queryParamMap.put(keyValue[0], keyValue[1]);
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
        return this.url;
    }

    /**
     * 获取此次请求的URI
     *
     * @return
     */
    @Override
    public String getRemoteURI() {
        return this.uri;
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
        return this.queryParamMap.get(key);
    }

    /**
     * 获取当前请求对象的字符输入流
     *
     * @return
     */
    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new StringReader(this.requestBody));
    }
}
