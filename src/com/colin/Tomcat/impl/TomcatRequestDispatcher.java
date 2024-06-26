package com.colin.Tomcat.impl;

import com.colin.Tomcat.core.Server;
import com.colin.servlet.HttpServlet;
import com.colin.servlet.HttpServletRequest;
import com.colin.servlet.HttpServletResponse;
import com.colin.servlet.RequestDispatcher;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 2024年06月26日09:47
 */
public class TomcatRequestDispatcher implements RequestDispatcher {

    private String uri;

    public TomcatRequestDispatcher(String uri) {
        this.uri = uri;
    }

    /**
     * 获取转发后servlet对象实例的封装方法
     * @param req
     * @param resp
     * @return
     */
    private HttpServlet getHttpServlet(HttpServletRequest req, HttpServletResponse resp) {
        HttpServlet httpServlet = null;
        try {
            for (String uri : Server.URIMappings.keySet()) {
                if (uri.equals(this.uri)) {
                    String currentClassName = Server.URIMappings.get(uri);
                    httpServlet = Server.servletMapping.get(currentClassName);
                    if (httpServlet == null) {
                        Class<?> aClass = Class.forName(currentClassName);
                        httpServlet = (HttpServlet) aClass.newInstance();
                        httpServlet.init();
                        Server.servletMapping.put(currentClassName, httpServlet);
                    }
                    httpServlet.service(req, resp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpServlet;
    }

    /**
     * include转发
     * @param req
     * @param resp
     */
    @Override
    public void include(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.getHttpServlet(req, resp).service(req, resp);
    }

    /**
     * forward转发
     *
     * @param req
     * @param resp
     */
    @Override
    public void forward(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpServlet httpServlet = this.getHttpServlet(req, resp);
        //清空当前response中printWrite流中的内容
        PrintWriter writer = resp.getWriter();
        writer.flush();
        resp.reset();

        httpServlet.service(req, resp);
    }
}
