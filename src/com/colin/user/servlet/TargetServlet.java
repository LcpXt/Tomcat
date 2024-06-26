package com.colin.user.servlet;

import com.colin.servlet.HttpServlet;
import com.colin.servlet.HttpServletRequest;
import com.colin.servlet.HttpServletResponse;
import com.colin.servlet.annotation.WebServlet;

import java.io.IOException;

/**
 * 2024年06月26日09:31
 */
@WebServlet("/target")
public class TargetServlet extends HttpServlet {
    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
        System.out.println("这里是targetServlet");
    }
}
