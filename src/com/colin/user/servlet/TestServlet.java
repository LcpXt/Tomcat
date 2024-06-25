package com.colin.user.servlet;

import com.colin.servlet.HttpServlet;
import com.colin.servlet.HttpServletRequest;
import com.colin.servlet.HttpServletResponse;
import com.colin.servlet.annotation.WebServlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 2024年06月24日16:51
 * 测试类
 * 模拟开发人员（用户）
 */
@WebServlet("/test")
public class TestServlet extends HttpServlet {
    @Override
    public void init() {
        System.out.println("执行TestServlet的init方法");
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader br = req.getReader();
        StringBuffer sb = new StringBuffer();
        String temp = "";
        while ((temp = br.readLine()) != null){
            sb.append(temp);
        }
        System.out.println(sb);
        br.close();
        PrintWriter writer = resp.getWriter();
        writer.write("这里是响应体");
        System.out.println("执行TestServlet");
    }

    @Override
    public void destroy() {
        System.out.println("执行TestServlet的destroy方法");
    }
}
