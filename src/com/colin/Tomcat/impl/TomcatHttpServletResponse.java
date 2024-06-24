package com.colin.Tomcat.impl;

import com.colin.servlet.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
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
    private String message;
    private Map<Integer, String> statusMessageMapping;

    /**
     * 响应头载体
     */
    private Map<String, String> headers;

    /**
     * 响应体的字符输出流
     */
    private PrintWriter printWriter;

    /**
     * 字符输出流和字节输出流之间的媒介
     */
    private ByteArrayOutputStream byteArrayOutputStream;

    /**
     * 拼接响应报文
     */
    private StringBuffer stringBuffer;

    /**
     * 预留
     */
    private OutputStream outputStream;

    public TomcatHttpServletResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.headers = new HashMap<String, String>();
        this.headers.put("Content-Type", "text/html; charset=utf-8");
        this.status = 200;
        this.message = "OK";

        this.byteArrayOutputStream = new ByteArrayOutputStream();
        this.printWriter = new PrintWriter(this.byteArrayOutputStream);
        this.initStatusMessage();
    }

    public void initStatusMessage() {
        this.statusMessageMapping = new HashMap<>();
        //参考HTTPStatus.txt
        this.statusMessageMapping.put(200,"OK");
        this.statusMessageMapping.put(400,"Bad Request");
        this.statusMessageMapping.put(404, "Not Found");
        this.statusMessageMapping.put(405, "Method Not Allowed");
        this.statusMessageMapping.put(500, "Internal Server Error");
    }

    /**
     * 获取操作响应体字符输出流
     *
     * @return
     */
    @Override
    public PrintWriter getWriter() {
        return this.printWriter;
    }

    /**
     * 设置响应头
     *
     * @param key
     * @param value
     */
    @Override
    public void setHeader(String key, String value) {
        this.headers.put(key, value);
    }

    /**
     * 设置响应行状态码
     *
     * @param status
     */
    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    public void prepareNoBodyResponse() throws IOException {
        this.stringBuffer = new StringBuffer();
        this.stringBuffer
                .append("HTTP/1.1 ").append(status).append(" ").append(this.statusMessageMapping.get(this.status)).append("\r\n");//拼接响应行
        //根据响应头载体的内容拼接到响应报文中
        for (String headerKey : headers.keySet()) {
            this.stringBuffer.append(headerKey).append(": ").append(headers.get(headerKey)).append("\r\n");
        }
        //拼接空行
        this.stringBuffer.append("\r\n");
        //不拼响应体
    }

    public void finishedResponse() throws IOException {
        this.printWriter.flush();
        byte[] bodyBytes = byteArrayOutputStream.toByteArray();
        //准备没有响应体的响应报文
        this.prepareNoBodyResponse();
        //写入没有响应体的响应报文
        this.outputStream.write(this.stringBuffer.toString().getBytes(StandardCharsets.UTF_8));
        //写入响应体
        this.outputStream.write(bodyBytes);

        this.printWriter.close();
        this.byteArrayOutputStream.close();
    }
}
