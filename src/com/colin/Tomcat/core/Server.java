package com.colin.Tomcat.core;

import com.colin.Tomcat.impl.TomcatHttpServletRequest;
import com.colin.Tomcat.impl.TomcatHttpServletResponse;
import com.colin.servlet.HttpServlet;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2024年06月24日10:11
 */
public class Server {

    /**
     * 用于递归调出源文件夹下的文件
     */
    public static final String sourceFolder = "E:\\project\\Tomcat\\src";

    private static Map<String, String> URIMappings;

    /**
     * 存储已经初始化过的servlet
     */
    private static Map<String, HttpServlet> servletMapping = new HashMap<>();

    public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress( "localhost",8080));
        String serverIp = serverSocket.getInetAddress().getHostName();
        int serverPort = serverSocket.getLocalPort();


        File file = new File(sourceFolder);
        List<String> currentProjectAllClassesName = PreparedHandler.getAllClasses(file);
        URIMappings = PreparedHandler.initURIMapping(currentProjectAllClassesName);

        while (true) {
            Socket socket = serverSocket.accept();
//            System.out.println("客户端收到请求");

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            TomcatHttpServletRequest request = new TomcatHttpServletRequest(inputStream, serverIp, serverPort);
            TomcatHttpServletResponse response = new TomcatHttpServletResponse(outputStream);

            String remoteURI = request.getRemoteURI();
            boolean flag = true;
            if ("/".equals(remoteURI)) {
                response.getWriter().write("欢迎来到首页");
            }
            for (String uri : URIMappings.keySet()) {
                if (uri.equals(remoteURI)) {//在映射关系中找到了此次请求URI对应的全限定类名
                    String currentServletClassName = URIMappings.get(remoteURI);
                    HttpServlet currentServlet = servletMapping.get(currentServletClassName);
                    //保证只在第一次创建的时候初始化，保证单例
                    if (currentServlet == null) {
                        Class<?> aClass = Class.forName(currentServletClassName);
                        currentServlet = (HttpServlet) aClass.newInstance();
                        currentServlet.init();
                    }
                    currentServlet.service(request, response);
                    flag = false;
                }
            }
            if (flag){
                response.getWriter().write("<!doctype html>\n" +
                        "   <html lang=\"zh\">\n" +
                        "      <head>\n" +
                        "         <title>\n" +
                        "            HTTP状态 404 - 未找到\n" +
                        "         </title>\n" +
                        "         <style type=\"text/css\">\n" +
                        "            body {font-family:Tahoma,Arial,sans-serif;} h1, h2, h3, b {color:white;background-color:#525D76;} h1 {font-size:22px;} h2 {font-size:16px;} h3 {font-size:14px;} p {font-size:12px;} a {color:black;} .line {height:1px;background-color:#525D76;border:none;}\n" +
                        "         </style>\n" +
                        "</head>\n" +
                        "      <body>\n" +
                        "         <h1>\n" +
                        "            HTTP状态 404 - 未找到\n" +
                        "         </h1>\n" +
                        "         <hr class=\"line\" />\n" +
                        "         <p>\n" +
                        "            <b>类型</b> 状态报告\n" +
                        "         </p>\n" +
                        "         <p>\n" +
                        "            <b>消息</b> 请求的资源[" + request.getRemoteURI() + "]不可用\n" +
                        "         </p>\n" +
                        "         <p>\n" +
                        "            <b>描述</b> 源服务器未能找到目标资源的表示或者是不愿公开一个已经存在的资源表示。\n" +
                        "         </p>\n" +
                        "         <hr class=\"line\" />\n" +
                        "         <h3>\n" +
                        "            MyTomcat 0.0.1\n" +
                        "         </h3>\n" +
                        "</body>\n" +
                        "</html>");
            }



//            System.out.println("已发给服务端响应");

            response.finishedResponse();
            outputStream.close();
            inputStream.close();
            socket.close();
        }
    }
}
