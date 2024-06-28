package com.colin.servlet.servlet;

import java.io.IOException;

/**
 * 2024年06月24日16:40
 */
public interface Servlet {

    public void init();

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, InstantiationException, IllegalAccessException;

    public void destroy();
}
