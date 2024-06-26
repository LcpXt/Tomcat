package com.colin.user.servlet;

import com.colin.servlet.HttpServlet;
import com.colin.servlet.HttpServletRequest;
import com.colin.servlet.HttpServletResponse;
import com.colin.servlet.annotation.WebServlet;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 2024年06月26日09:31
 */
@WebServlet("/showRequest")
public class ShowRequestServlet extends HttpServlet {
    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("这里是showRequestServlet");
//        resp.sendRedirect("/target");
        PrintWriter out = resp.getWriter();
        out.write("sssssss");
        req.getRequestDispatcher("/target").forward(req, resp);
        System.out.println("这里是转发方法调用后执行的代码");
    }
}
