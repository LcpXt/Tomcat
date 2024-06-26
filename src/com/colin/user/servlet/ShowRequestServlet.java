package com.colin.user.servlet;

import com.colin.servlet.Cookie;
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
//        Cookie[] cookies = req.getCookies();
//        for (Cookie cookie : cookies) {
//            System.out.println(cookie.getKey() + ":" + cookie.getValue());
//        }
//        resp.addCookie(new Cookie("acs", "sssss"));
        Cookie cookie1 = new Cookie("sql", "111");
        cookie1.setMaxAge(360000);
        cookie1.setPath("/login");
        cookie1.setHttpOnly(true);
        resp.addCookie(cookie1);

        Cookie cookie2 = new Cookie("slss", "dwd");
        cookie2.setMaxAge(360000);
//        cookie2.setPath("/login");
        cookie2.setHttpOnly(false);
        resp.addCookie(cookie2);
    }
}
