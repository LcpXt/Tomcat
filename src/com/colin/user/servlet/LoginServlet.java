package com.colin.user.servlet;

import com.colin.servlet.servlet.HttpServlet;
import com.colin.servlet.servlet.HttpServletRequest;
import com.colin.servlet.servlet.HttpServletResponse;
import com.colin.servlet.annotation.WebServlet;

/**
 * 2024年06月24日18:25
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    public void init() {
//        System.out.println("执行LoginServlet的init方法");
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) {
//        String username = req.getParameter("username");
//        String pw = req.getParameter("password");
//        System.out.println(username);
//        System.out.println(pw);
        System.out.println("执行LoginServlet");
    }

    @Override
    public void destroy() {
        System.out.println("执行LoginServlet的destroy方法");
    }
}
