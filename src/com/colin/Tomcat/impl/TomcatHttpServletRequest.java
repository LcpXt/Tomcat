package com.colin.Tomcat.impl;

import com.colin.servlet.Cookie;
import com.colin.servlet.HttpServletRequest;
import com.colin.servlet.RequestDispatcher;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 2024年06月24日10:09
 */
public class TomcatHttpServletRequest implements HttpServletRequest {
    /**
     * 完整的请求报文
     */
    private final String requestContent;
    /**
     * 请求头
     */
    private final Map<String, String> requestHeaders;
    /**
     * 请求行
     */
    private final String requestLine;
    /**
     * 请求体
     */
    private String requestBody;
    /**
     * 查询字符串参数的map载体，方便getParamter根据key获取value
     */
    private final Map<String, String> queryParamMap;
    /**
     * 查询字符串参数，就是uri后以?分隔的参数字符串
     */
    private String queryParamString;
    /**
     * URI
     */
    private String uri;
    /**
     * URL
     */
    private String url;
    /**
     * 请求体的长度
     */
    private Integer contentLength;
    /**
     * 请求体数据的字节流
     */
    private InputStream inputStream;
    /**
     * 请求体的完整字节数据
     */
    private byte[] requestBodyByteArray;
    /**
     * 域对象存kv
     */
    private Map<String, Object> attributes;
    /**
     * Cookie数组
     */
    private Cookie[] cookies;

    public TomcatHttpServletRequest(InputStream inputStream, String serverIp, int serverPort) throws IOException {
        try {
            this.attributes = new HashMap<>();
            byte[] temp = new byte[8192];
            int read = inputStream.read(temp);
            this.requestContent = new String(temp, 0, read, StandardCharsets.ISO_8859_1);
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
            //解析cookie
            this.parseCookie();
            this.requestLine = requestContent.substring(0, headerBeginIndex - 1);
            //解析请求行
            this.queryParamMap = new HashMap<>();
            this.parseRequestLine(serverIp, serverPort);

            int requestBodyBeginIndex = headerEndIndex + 4;
            //从\r\n\r\n截到最后就是剩下的不完整请求体
            String incompleteBody = requestContent.substring(requestBodyBeginIndex);
            byte[] incompleteBodyBytes = incompleteBody.getBytes(StandardCharsets.ISO_8859_1);
            //得到请求体内容一共有多少字节
            int contentLength = getContentLength(substring);
            System.out.println("总大小："+ contentLength);
            this.parseRequestBody(headerEndIndex, incompleteBodyBytes, inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析cookie
     * <p>
     * cookie的形式
     * cookie  :   k1=v1;k2=v2;k3=v3
     */
    private void parseCookie() {

        String allCookie = this.requestHeaders.get("cookie");
        if (allCookie != null) {
            String[] cookieStr = allCookie.split(";");
            this.cookies = new Cookie[cookieStr.length];
            for (int i = 0; i < cookieStr.length; i++) {
                String[] entry = cookieStr[i].trim().split("=");
                String key = entry[0];
                String value = entry[1];
                TomcatCookie tomcatCookie = new TomcatCookie(key, value);
                this.cookies[i] = tomcatCookie;
            }
        }
    }

    /**
     * 获取请求体的长度
     * @param substring
     * @return
     */
    public int getContentLength(String substring) {
        String[] headerKeyAndValue = substring.split("\r\n");
        this.contentLength = 0;
        for (String kv : headerKeyAndValue) {
            if (kv.toLowerCase().contains("content-length")) {
                int delimiterIndex = kv.indexOf(":");
                String contentLengthStr = kv.substring(delimiterIndex + 1).trim();
                this.contentLength = Integer.parseInt(contentLengthStr);
                return this.contentLength;
            }
        }
        return this.contentLength;
    }

    /**
     * 解析请求体
     *
     * @param headerEndIndex
     * @param incompleteBodyBytes
     * @param inputStream
     */
    private void parseRequestBody(int headerEndIndex, byte[] incompleteBodyBytes, InputStream inputStream) throws IOException {
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
        //客户端会自动给一个随机的bindry分隔，无法用equals进行比较
        //multipart/form-data/bindry。。。。。
        if (contentType.trim().contains("multipart/form-data")){
            this.parseFormData(incompleteBodyBytes, inputStream);
        }
    }

    /**
     * 处理二进制数据
     */
    private void parseFormData(byte[] incompleteBodyBytes, InputStream inputStream) throws IOException {
        int tempLength = 0;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //把请求头后面的剩余二进制数据先写进来
        byteArrayOutputStream.write(incompleteBodyBytes);
        //剩余的二进制数据长度 = content-length头中表明的总长度 - 第一次读取时读进来的不完全的数据的长度
        int residueBodyBytesLength = this.contentLength - incompleteBodyBytes.length;
        System.out.println("剩余的大小：" + residueBodyBytesLength);
        System.out.println("第一次读进来的大小：" + incompleteBodyBytes.length);
        byte[] maxBufferBytes = new byte[65536];
        while (tempLength < residueBodyBytesLength) {
            int length = inputStream.read(maxBufferBytes);
            tempLength += length;
            byteArrayOutputStream.write(maxBufferBytes, 0, length);
        }
        this.requestBodyByteArray = byteArrayOutputStream.toByteArray();
        System.out.println("完整字节数组大小：" + this.requestBodyByteArray.length);
        byteArrayOutputStream.close();

    }
    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.requestBodyByteArray);
    }

    /**
     * 获取请求控制器
     *
     * @param uri
     */
    @Override
    public RequestDispatcher getRequestDispatcher(String uri) {
        return new TomcatRequestDispatcher(uri);
    }

    /**
     * 向域对象放入kv
     *
     * @param key
     * @param value
     */
    @Override
    public void setAttribute(String key, Object value) {
        this.attributes.put(key, value);
    }

    /**
     * 根据k，从域对象获取v
     *
     * @param key
     * @return
     */
    @Override
    public Object getAttribute(String key) {
        return this.attributes.get(key);
    }

    /**
     * 获取cookie
     *
     * @return
     */
    @Override
    public Cookie[] getCookies() {
        return this.cookies;
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
    }

    /**
     * 解析字符串参数
     */
    private void parseQueryParamToMap(String str) {
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
