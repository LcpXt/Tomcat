package com.colin.Tomcat.core;

import com.colin.Tomcat.exception.NotListenerException;
import com.colin.Tomcat.exception.NotServeletException;
import com.colin.Tomcat.impl.TomcatHttpServletRequest;
import com.colin.Tomcat.impl.TomcatHttpServletResponse;
import com.colin.Tomcat.impl.TomcatServletContext;
import com.colin.servlet.listener.*;
import com.colin.servlet.servlet.Cookie;
import com.colin.servlet.servlet.HttpServlet;
import com.colin.servlet.servlet.ServletContext;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 2024年06月24日10:11
 */
public class Server {

    /**
     * 用于递归调出源文件夹下的文件
     */
    public static final String sourceFolder = "E:\\project\\Tomcat\\src";

    /**
     * URI和全限定类名的kv
     */
    public static Map<String, String> URIMappings;

    /**
     * 全限定类名 和 已经初始化过的servlet
     * 存储已经初始化过的servlet
     */
    public static Map<String, HttpServlet> servletMapping = new HashMap<>();

    public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NotServeletException, NotListenerException {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                200,
                200,
                30000,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(8196)
        );

        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress( "localhost",8080));
        String serverIp = serverSocket.getInetAddress().getHostName();
        int serverPort = serverSocket.getLocalPort();


        File file = new File(sourceFolder);
        List<String> currentProjectAllClassesName = PreparedHandler.getAllClasses(file);
        URIMappings = PreparedHandler.initURIMapping(currentProjectAllClassesName);

        //获取单例的servletContext
        TomcatServletContext servletContext = (TomcatServletContext) TomcatServletContext.getServletContext();
        //获取该servletContext的监听器
        ServletContextListener applicationListener = (ServletContextListener) ListenerFactory.getListener(servletContext);
        if (applicationListener != null){
            //执行监听器的init方法，如果开发人员重写了init方法，那执行的就是重写之后的init方法
            applicationListener.init(servletContext.getServletContextEvent());
        }

        ServletContextAttributeListener attributeListener = (ServletContextAttributeListener) ListenerFactory.getAttributeListener(servletContext);
        servletContext.setServletContextAttributeListener(attributeListener);
        //借助runtime运行时，嵌入JVM进程退出时的逻辑，调用servletContext域对象的销毁方法
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (applicationListener != null){
                applicationListener.destroyed(servletContext.getServletContextEvent());
            }
        }));

        while (true) {
            Socket socket = serverSocket.accept();

            threadPoolExecutor.execute(() ->{
                try {
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();
                    //request域对象创建完成
                    TomcatHttpServletRequest request = new TomcatHttpServletRequest(inputStream, serverIp, serverPort);
                    //准备一个暴露给开发人员的时间节点 是 request的初始化节点
                    ServletRequestListener listener = request.getListener();
                    if (listener != null){
                        //为了能在监听方法中拿到当前域对象
                        ServletRequestEvent servletRequestEvent = new ServletRequestEvent(request);
                        request.setServletRequestEvent(servletRequestEvent);
                        listener.initRequest(servletRequestEvent);
                    }

                    TomcatHttpServletResponse response = new TomcatHttpServletResponse(outputStream);

                    PreparedHandler.firstFilterChain.doFilter(request, response);


                    if (request.initSessionMark){
                        System.err.println("此次是创建session" + request.currentSession);
                        response.addCookie(new Cookie("JSESSIONID", request.currentSession.getId() + ""));
                    }
                    //即将完成响应时，执行request域对象的 destroy方法
                    //如果开发人员有自己的重写 走的是重写的逻辑
                    //如果没有 走的是默认的空方法
                    if (request.getListener() != null){
                        request.getListener().destroyed(request.getServletRequestEvent());
                    }
                    response.finishedResponse();
                    outputStream.close();
                    inputStream.close();
                    socket.close();
                } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
