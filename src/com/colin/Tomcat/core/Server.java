package com.colin.Tomcat.core;

import com.colin.Tomcat.impl.TomcatHttpServletRequest;
import com.colin.Tomcat.impl.TomcatHttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 2024年06月24日10:11
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("客户端收到请求");

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            TomcatHttpServletRequest request = new TomcatHttpServletRequest(inputStream);
            TomcatHttpServletResponse response = new TomcatHttpServletResponse(outputStream);
            System.out.println("-----------------------------------");

            PrintWriter writer = response.getWriter();
            response.setHeader("A", "B");
            response.setStatus(200);
            writer.write("这是自己封装的响应报文对象");
            System.out.println("-----------------------------------");

            StringBuffer sb = new StringBuffer();
            sb.append("HTTP/1.1 200 OK \n")//拼接响应行
              .append("Content-Type: text/plain;charset=utf8\n")//响应头
              .append("Content-Length: 110\n")
//              .append("Content-Length: " + "hello world".getBytes(StandardCharsets.UTF_8).length + "\n")
              .append("A: B\n")
              .append("\n")//空行
              .append("hello world!");//响应体
//            outputStream.write(sb.toString().getBytes(StandardCharsets.UTF_8));

            System.out.println("已发给服务端响应");

            response.finishedResponse();
            outputStream.close();
            inputStream.close();
            socket.close();
        }
    }
}
