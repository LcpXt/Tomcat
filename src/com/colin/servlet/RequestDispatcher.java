package com.colin.servlet;

import java.io.IOException;

/**
 * 2024年06月26日09:45
 */
public interface RequestDispatcher {

    /**
     * include转发
     * @param req
     * @param resp
     */
    void include(HttpServletRequest req, HttpServletResponse resp) throws IOException;

    /**
     * forward转发
     * @param req
     * @param resp
     */
    void forward(HttpServletRequest req, HttpServletResponse resp) throws IOException;

}
