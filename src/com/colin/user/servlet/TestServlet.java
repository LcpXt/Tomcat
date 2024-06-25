package com.colin.user.servlet;

import com.colin.servlet.HttpServlet;
import com.colin.servlet.HttpServletRequest;
import com.colin.servlet.HttpServletResponse;
import com.colin.servlet.annotation.WebServlet;

import java.io.*;

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
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        InputStream inputStream = req.getInputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, length);
        }
        System.out.println(byteArrayOutputStream.toString());
        byteArrayOutputStream.close();
        inputStream.close();
    }

    @Override
    public void destroy() {
        System.out.println("执行TestServlet的destroy方法");
    }
}
