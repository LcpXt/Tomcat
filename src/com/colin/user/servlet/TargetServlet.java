package com.colin.user.servlet;

import com.colin.servlet.servlet.HttpServlet;
import com.colin.servlet.servlet.HttpServletRequest;
import com.colin.servlet.servlet.HttpServletResponse;
import com.colin.servlet.annotation.WebServlet;

import java.io.IOException;

/**
 * 2024年06月26日09:31
 */
@WebServlet("/target")
public class TargetServlet extends HttpServlet {
    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        System.out.println("这里是targetServlet");
//        User user = (User) req.getAttribute("user");
//        System.out.println(user);
//        resp.getWriter().write(user.toString());
        int count = (int) req.getServletContext().getAttribute("count");
        System.out.println(count);
    }
}
