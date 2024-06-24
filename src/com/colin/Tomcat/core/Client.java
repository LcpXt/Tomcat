package com.colin.Tomcat.core;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * 2024年06月24日10:11
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8080);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("hello world".getBytes(StandardCharsets.UTF_8));
    }
}
