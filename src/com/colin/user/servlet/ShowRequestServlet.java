package com.colin.user.servlet;

import com.colin.servlet.HttpServlet;
import com.colin.servlet.HttpServletRequest;
import com.colin.servlet.HttpServletResponse;
import com.colin.servlet.annotation.WebServlet;
import com.colin.user.bean.User;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 2024年06月26日09:31
 */
@WebServlet("/showRequest")
public class ShowRequestServlet extends HttpServlet {
    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = new User(1, "aaa", "bbb");
        req.setAttribute("user", user);
        req.getRequestDispatcher("/target").forward(req, resp);
    }
}
