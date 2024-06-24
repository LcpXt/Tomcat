package com.colin.servlet;

/**
 * 2024年06月24日16:40
 */
public interface Servlet {

    public void init();

    public void service(HttpServletRequest req, HttpServletResponse res);

    public void destroy();
}
