package com.colin.Tomcat.impl;

import com.colin.Tomcat.core.PreparedHandler;
import com.colin.Tomcat.core.Server;
import com.colin.servlet.filter.Filter;
import com.colin.servlet.filter.FilterChain;
import com.colin.servlet.servlet.HttpServlet;
import com.colin.servlet.servlet.HttpServletRequest;
import com.colin.servlet.servlet.HttpServletResponse;

import java.io.IOException;

/**
 * 2024年06月28日16:37
 */
public class TomcatFilterChain implements FilterChain {

    /**
     * 当前这次请求
     */
    private HttpServletRequest request;

    /**
     * 当前这次响应
     */
    private HttpServletResponse response;

    /**
     * 记录头节点
     */
    private FilterChain firstFilterChain;

    /**
     * 记录尾节点
     */
    private FilterChain lastFilterChain;

    /**
     * 记录上一个FilterChain
     */
    private FilterChain nextFilterChain;

    /**
     * 记录下一个FilterChain
     */
    private FilterChain previousFilterChain;

    /**
     * 当前FilterChain对应的过滤器Filter
     */
    private Filter currentFilter;

    /**
     * 当前过滤器Filter 对应@WebFilter注解中声明的过滤路径
     */
    private String urlPattern;

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        if (this == PreparedHandler.firstFilterChain) {
            this.nextFilterChain.doFilter(req, resp);
        }else if (this == PreparedHandler.lastFilterChain) {
            String remoteURI =req.getRemoteURI();
            boolean flag = true;
            if ("/".equals(remoteURI)) {
                resp.getWriter().write("欢迎来到首页");
            }
            for (String uri : Server.URIMappings.keySet()) {
                if (uri.equals(remoteURI)) {//在映射关系中找到了此次请求URI对应的全限定类名
                    String currentServletClassName = Server.URIMappings.get(remoteURI);
                    HttpServlet currentServlet = Server.servletMapping.get(currentServletClassName);
                    //保证只在第一次创建的时候初始化，保证单例
                    if (currentServlet == null) {
                        Class<?> aClass = Class.forName(currentServletClassName);
                        currentServlet = (HttpServlet) aClass.newInstance();
                        currentServlet.init();
                        Server.servletMapping.put(currentServletClassName, currentServlet);
                    }
                    currentServlet.service(req, resp);
                    flag = false;
                }
            }
            //没找到此次请求URI对应的全限定类名
            if (flag){
                resp.getWriter().write("<!doctype html>\n" +
                        "   <html lang=\"zh\">\n" +
                        "      <head>\n" +
                        "         <title>\n" +
                        "            HTTP状态 404 - 未找到\n" +
                        "         </title>\n" +
                        "         <style type=\"text/css\">\n" +
                        "            body {font-family:Tahoma,Arial,sans-serif;} h1, h2, h3, b {color:white;background-color:#525D76;} h1 {font-size:22px;} h2 {font-size:16px;} h3 {font-size:14px;} p {font-size:12px;} a {color:black;} .line {height:1px;background-color:#525D76;border:none;}\n" +
                        "         </style>\n" +
                        "</head>\n" +
                        "      <body>\n" +
                        "         <h1>\n" +
                        "            HTTP状态 404 - 未找到\n" +
                        "         </h1>\n" +
                        "         <hr class=\"line\" />\n" +
                        "         <p>\n" +
                        "            <b>类型</b> 状态报告\n" +
                        "         </p>\n" +
                        "         <p>\n" +
                        "            <b>消息</b> 请求的资源[" + req.getRemoteURI() + "]不可用\n" +
                        "         </p>\n" +
                        "         <p>\n" +
                        "            <b>描述</b> 源服务器未能找到目标资源的表示或者是不愿公开一个已经存在的资源表示。\n" +
                        "         </p>\n" +
                        "         <hr class=\"line\" />\n" +
                        "         <h3>\n" +
                        "            MyTomcat 0.0.1\n" +
                        "         </h3>\n" +
                        "</body>\n" +
                        "</html>");
            }
        }else {
            String uri = req.getRemoteURI();
            String[] split = uri.split("/");
            String[] filterPattern = this.urlPattern.split("/");
            //split.length 此次请求的层级个数
            //filterPattern.length 过滤器过滤路径的层级个数

            if (split.length >= filterPattern.length) {
                if (filterPattern[1].equals("*") && filterPattern.length == 2){
                    this.currentFilter.doFilter(req, resp, this.nextFilterChain);
                }else {
                    int i = 1;
                    for(; i < filterPattern.length && this.urlCheck(filterPattern[i], split[i]); i++) {

                    }
                    if (i == filterPattern.length && (filterPattern.length == split.length || filterPattern[i - 1].equals("*"))) {
                        this.currentFilter.doFilter(req, resp, this.nextFilterChain);
                    }else {
                        this.nextFilterChain.doFilter(req, resp);
                    }
                }
            }else {
                this.nextFilterChain.doFilter(req, resp);
            }
        }
    }

    /**
     * 校验多级路径，用于循环的判断每一级
     * @param currentFilterUri
     * @param currentRequestUri
     * @return
     */
    private boolean urlCheck(String currentFilterUri, String currentRequestUri) {
        return "*".equals(currentFilterUri) || currentFilterUri.equals(currentRequestUri);
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public FilterChain getFirstFilterChain() {
        return firstFilterChain;
    }

    public void setFirstFilterChain(FilterChain firstFilterChain) {
        this.firstFilterChain = firstFilterChain;
    }

    public FilterChain getLastFilterChain() {
        return lastFilterChain;
    }

    public void setLastFilterChain(FilterChain lastFilterChain) {
        this.lastFilterChain = lastFilterChain;
    }

    public FilterChain getNextFilterChain() {
        return nextFilterChain;
    }

    public void setNextFilterChain(FilterChain nextFilterChain) {
        this.nextFilterChain = nextFilterChain;
    }

    public FilterChain getPreviousFilterChain() {
        return previousFilterChain;
    }

    public void setPreviousFilterChain(FilterChain previousFilterChain) {
        this.previousFilterChain = previousFilterChain;
    }

    public Filter getCurrentFilter() {
        return currentFilter;
    }

    public void setCurrentFilter(Filter currentFilter) {
        this.currentFilter = currentFilter;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }
}
